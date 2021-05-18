package matrix.module.pay.templates;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import matrix.module.based.utils.JacksonUtil;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.encrypt.MD5;
import matrix.module.common.utils.BigDecimalUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.pay.builder.body.WepayPayBody;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.builder.response.QueryRefundResponse;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.constants.WepayConstant;
import matrix.module.pay.converter.WepayConvert;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.service.MatrixPayService;
import matrix.module.pay.vo.PayVo;
import matrix.module.pay.vo.RefundVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wangcheng
 */
public class WepayTemplate extends AbstractTemplate {

    private static final Logger logger = LogManager.getLogger(WepayTemplate.class);

    @Autowired
    private PayProperties payProperties;

    @Autowired
    private MatrixPayService payService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WxPayConfig wxPayConfig;

    @Override
    protected String invokePay(PayMode payMode, PayVo payVo) {
        WxPayUnifiedOrderRequest request = WepayConvert.convertPayRequest(payMode, payVo,
                payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + WepayConstant.NOTIFY_URI);
        try {
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
            WepayPayBody payBody = new WepayPayBody()
                    .setQrCodeWidth(payVo.getQrCodeWidth())
                    .setPayModeCode(payMode.getCode());
            if (PayMode.H5.equals(payMode)) {
                payBody.setUrl(result.getMwebUrl());
            } else if (PayMode.PC.equals(payMode) || PayMode.QrCode.equals(payMode)) {
                payBody.setUrl(result.getCodeURL());
            } else if (PayMode.WEJSAPI.equals(payMode)) {
                Map<String, String> params = new HashMap<>();
                params.put("appId", wxPayConfig.getAppId());
                params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
                params.put("nonceStr", MD5.get32(UUID.randomUUID().toString()));
                params.put("package", "prepay_id=" + result.getPrepayId());
                params.put("signType", "MD5");
                String paySign = SignUtils.createSign(params, "MD5", wxPayConfig.getMchKey(), new String[0]);
                params.put("paySign", paySign);
                payBody.setJsApiParams(JacksonUtil.toJsonString(params));
            } else if (PayMode.APP.equals(payMode)) {
                Map<String, String> params = new HashMap<>();
                params.put("appid", wxPayConfig.getAppId());
                params.put("partnerid", wxPayConfig.getMchId());
                params.put("prepayid", result.getPrepayId());
                params.put("package", "Sign=WXPay");
                params.put("noncestr", MD5.get32(UUID.randomUUID().toString()));
                params.put("timestamp", String.valueOf(System.currentTimeMillis()));
                String paySign = SignUtils.createSign(params, "MD5", wxPayConfig.getMchKey(), new String[0]);
                params.put("sign", paySign);
                payBody.setAppParams(JacksonUtil.toJsonString(params));
            }
            return JacksonUtil.toJsonString(payBody);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    protected Integer invokeRefund(RefundVo refundVo) {
        MatrixPayEntity payEntity = payService.getPayEntityByPayId(refundVo.getPayId());
        WxPayRefundRequest request = WepayConvert.convertRefundRequest(refundVo, payEntity, payProperties.getNotifyDomain() + PayConstant.NOTIFY_RETURN_URL_PREFIX + WepayConstant.NOTIFY_URI);
        try {
            wxPayService.refund(request);
            return PayConstant.REFUNDING;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    protected List<QueryPayResponse> invokeQueryPay(List<String> payIds) {
        List<QueryPayResponse> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payIds)) {
            for (String payId : payIds) {
                try {
                    WxPayOrderQueryResult queryResult = wxPayService.queryOrder(WepayConvert.convertPayQueryRequest(payId));
                    if (WepayConstant.RETURN_CODE.equals(queryResult.getTradeState())) {
                        result.add(new QueryPayResponse(queryResult.getOutTradeNo(), queryResult.getTransactionId(),
                                BigDecimalUtil.centsToYuan(queryResult.getTotalFee()).toPlainString()));
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
        Set<String> result = new HashSet<>();
        if (!CollectionUtils.isEmpty(queryRefundRequests)) {
            for (QueryRefundRequest queryRefundRequest : queryRefundRequests) {
                WxPayRefundQueryRequest request = WepayConvert.convertQueryRefundRequest(queryRefundRequest);
                try {
                    WxPayRefundQueryResult queryResult = wxPayService.refundQuery(request);
                    if (WepayConstant.RETURN_CODE.equals(queryResult.getResultCode()) && queryResult.getRefundCount() > 0) {
                        List<WxPayRefundQueryResult.RefundRecord> refundRecords = queryResult.getRefundRecords();
                        for (WxPayRefundQueryResult.RefundRecord refundRecord: refundRecords) {
                            if (WepayConstant.RETURN_CODE.equals(refundRecord.getRefundStatus())) {
                                result.add(queryRefundRequest.getRefundId());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        return new ArrayList<>(result);
    }

    @Override
    public String parsePayNotify(HttpServletRequest request) {
        try {
            String xml = StreamUtil.streamToString(request.getInputStream());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xml);
            if (WepayConstant.RETURN_CODE.equals(result.getResultCode())) {
                if (!callbackPaySuccess(new QueryPayResponse(result.getOutTradeNo(), result.getTransactionId(),
                        BigDecimalUtil.centsToYuan(result.getTotalFee()).toPlainString()))) {
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
            WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xml);
            if (WepayConstant.RETURN_CODE.equals(result.getReturnCode())) {
                WxPayRefundNotifyResult.ReqInfo reqInfo = result.getReqInfo();
                if (WepayConstant.RETURN_CODE.equals(reqInfo.getRefundStatus())
                        && !callbackRefundSuccess(new QueryRefundResponse(reqInfo.getOutTradeNo(), reqInfo.getOutRefundNo(),
                        reqInfo.getTransactionId(), reqInfo.getRefundId(), String.valueOf(reqInfo.getTotalFee())))) {
                    return WepayConstant.FAIL_RETURN_CODE;
                }
            }
            return WepayConstant.SUCCESS_RETURN_CODE;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
