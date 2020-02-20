package matrix.module.pay.templates;

import matrix.module.based.utils.JacksonUtil;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.encrypt.Base64;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.BigDecimalUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.pay.builder.body.WepayPayBody;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.builder.request.wepay.WepayPayRequest;
import matrix.module.pay.builder.request.wepay.WepayQueryRefundRequest;
import matrix.module.pay.builder.request.wepay.WepayRefundRequest;
import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.builder.response.QueryRefundResponse;
import matrix.module.pay.builder.response.wepay.*;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.constants.WepayConstant;
import matrix.module.pay.converter.WepayConvert;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.sdk.WepayClient;
import matrix.module.pay.service.MatrixPayService;
import matrix.module.pay.vo.PayVo;
import matrix.module.pay.vo.RefundVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangcheng
 * @date 2019/4/29
 */
public class WepayTemplate extends AbstractTemplate {

    private static final Logger logger = LogManager.getLogger(WepayTemplate.class);

    @Autowired
    private PayProperties payProperties;

    @Autowired
    private MatrixPayService payService;

    private WepayClient wepayClient;

    public WepayTemplate(WepayClient wepayClient) {
        this.wepayClient = wepayClient;
    }

    @Override
    protected String invokePay(PayMode payMode, PayVo payVo) {
        WepayPayRequest request = WepayConvert.convertPayRequest(payMode, payVo,
                payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + WepayConstant.NOTIFY_URI);
        WepayPayResponse response = wepayClient.execute(request);
        WepayPayBody payBody = new WepayPayBody()
                .setPrepayId(response.getPrepayId())
                .setUrl(PayMode.H5.equals(payMode) ? response.getMwebUrl() : response.getCodeUrl())
                .setQrCodeWidth(payVo.getQrCodeWidth())
                .setPayModeCode(payMode.getCode());
        return JacksonUtil.toJsonString(payBody);
    }

    @Override
    protected Integer invokeRefund(RefundVo refundVo) {
        MatrixPayEntity payEntity = payService.getPayEntityByPayId(refundVo.getPayId());
        WepayRefundRequest request = WepayConvert.convertRefundRequest(refundVo, payEntity, payProperties.getNotifyDomain() + PayConstant.NOTIFY_RETURN_URL_PREFIX + WepayConstant.NOTIFY_URI);
        WepayRefundResponse response = wepayClient.execute(request);
        if (WepayConstant.RETURN_CODE.equals(response.getResultCode())) {
            return PayConstant.REFUNDING;
        }
        return PayConstant.REFUNDING;
    }

    @Override
    protected List<QueryPayResponse> invokeQueryPay(List<String> payIds) {
        List<QueryPayResponse> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payIds)) {
            for (String payId : payIds) {
                try {
                    WepayQueryPayResponse response = wepayClient.execute(WepayConvert.convertPayQueryRequest(payId));
                    if (WepayConstant.RETURN_CODE.equals(response.getTradeState())) {
                        result.add(new QueryPayResponse(response.getOutTradeNo(), response.getTransactionId(),
                                BigDecimalUtil.centsToYuan(response.getTotalFee()).toPlainString()));
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        return result;
    }

    @Override
    protected List<String> invokeQueryRefund(List<QueryRefundRequest> queryRefundRequests) {
        List<String> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(queryRefundRequests)) {
            for (QueryRefundRequest queryRefundRequest : queryRefundRequests) {
                WepayQueryRefundRequest request = WepayConvert.convertQueryRefundRequest(queryRefundRequest);
                WepayQueryRefundResponse response = wepayClient.execute(request);
                if (WepayConstant.RETURN_CODE.equals(response.getResultCode())) {
                    List<WepayQueryRefundResponse.Refund> refunds = response.getRefunds();
                    if (!CollectionUtils.isEmpty(refunds)
                            && WepayConstant.RETURN_CODE.equals(refunds.get(0).getRefundStatus())) {
                        result.add(queryRefundRequest.getRefundId());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String parsePayNotify(HttpServletRequest request) {
        try {
            String xml = StreamUtil.streamToString(request.getInputStream());
            WepayQueryPayResponse response = wepayClient.parseResponse(xml, WepayQueryPayResponse.class);
            if (WepayConstant.RETURN_CODE.equals(response.getResultCode())) {
                if (!callbackPaySuccess(new QueryPayResponse(response.getOutTradeNo(), response.getTransactionId(),
                        BigDecimalUtil.centsToYuan(response.getTotalFee()).toPlainString()))) {
                    return WepayConstant.FAIL_RETURN_CODE;
                }
            }
            return WepayConstant.SUCCESS_RETURN_CODE;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public String parseRefundNotify(HttpServletRequest request) {
        try {
            String xml = StreamUtil.streamToString(request.getInputStream());
            //解析xml
            WepayNotifyRefundResponse refundResponse = JacksonUtil.parseJson(XML.toJSONObject(xml).getJSONObject("xml").toString(),
                    WepayNotifyRefundResponse.class);
            //判断消息头
            if (WepayConstant.RETURN_CODE.equals(refundResponse.getReturnCode())) {
                //解密
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(MD5.get32(payProperties.getWepay().getKey()).toLowerCase().getBytes(), "AES"));
                String reqInfo = new String(cipher.doFinal(Objects.requireNonNull(Base64.decrypt(refundResponse.getReqInfo()))), StandardCharsets.UTF_8);
                //获取消息体
                WepayNotifyRefundResponse.Info info = JacksonUtil.parseJson(XML.toJSONObject(reqInfo).getJSONObject("root").toString(),
                        WepayNotifyRefundResponse.Info.class);
                if (WepayConstant.RETURN_CODE.equals(info.getRefundStatus())
                        && !callbackRefundSuccess(new QueryRefundResponse(info.getOutTradeNo(), info.getOutRefundNo(),
                        info.getTransactionId(), info.getRefundId(), String.valueOf(info.getTotalFee())))) {
                    return WepayConstant.FAIL_RETURN_CODE;
                }
            }
            return WepayConstant.SUCCESS_RETURN_CODE;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
