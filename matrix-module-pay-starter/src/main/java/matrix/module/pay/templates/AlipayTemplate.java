package matrix.module.pay.templates;

import com.alibaba.druid.util.StringUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.exception.ServiceException;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.constants.AlipayConstant;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.converter.AlipayConvert;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.utils.RequestUtil;
import matrix.module.pay.vo.PayVo;
import matrix.module.pay.vo.RefundVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangcheng
 * @date 2019/4/25
 */
public class AlipayTemplate extends AbstractTemplate {

    private static final Logger logger = LogManager.getLogger(AlipayTemplate.class);

    @Autowired
    private PayProperties payProperties;

    private AlipayClient alipayClient;

    public AlipayTemplate(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    @Override
    protected String invokePay(PayMode payMode, PayVo payVo) {
        try {
            AlipayRequest<? extends AlipayResponse> alipayRequest = AlipayConvert.convertPayRequest(payMode, payVo,
                    payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + AlipayConstant.NOTIFY_URI, payProperties.getAlipay().getReturnUrl());
            AlipayResponse resp;
            if (PayMode.APP.getCode().equals(payMode.getCode())) {
                resp = alipayClient.sdkExecute(alipayRequest);
            } else {
                resp = alipayClient.pageExecute(alipayRequest);
            }
            if (resp.isSuccess()) {
                return resp.getBody();
            }
            throw new ServiceException("invoke alipay error");
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    protected Integer invokeRefund(RefundVo refundVo) {
        try {
            AlipayTradeRefundRequest refundRequest = AlipayConvert.convertRefundRequest(refundVo);
            AlipayTradeRefundResponse refundResponse = alipayClient.execute(refundRequest);
            if (refundResponse.isSuccess() && AlipayConstant.REFUND_SUCCESS_FLAG.equals(refundResponse.getMsg().toUpperCase())) {
                return PayConstant.REFUNDED;
            }
            return PayConstant.REFUNDING;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    protected List<QueryPayResponse> invokeQueryPay(List<String> payIds) {
        List<QueryPayResponse> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payIds)) {
            for (String payId : payIds) {
                try {
                    AlipayTradeQueryRequest queryRequest = AlipayConvert.convertQueryPayRequest(payId);
                    AlipayTradeQueryResponse response = alipayClient.execute(queryRequest);
                    String payStatus = response.getTradeStatus();
                    if (AlipayConstant.PAY_SUCCESS_FLAG.equals(payStatus) || AlipayConstant.PAY_FINISHED_FLAG.equals(payStatus)) {
                        result.add(new QueryPayResponse(payId, response.getTradeNo(), response.getTotalAmount()));
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
                try {
                    AlipayTradeFastpayRefundQueryRequest queryRequest = AlipayConvert.convertQueryRefundRequest(queryRefundRequest);
                    AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(queryRequest);
                    if (response.isSuccess()
                            && AlipayConstant.REFUND_SUCCESS_FLAG.equals(response.getMsg().toUpperCase())
                            && !StringUtils.isEmpty(response.getRefundAmount())) {
                        result.add(queryRefundRequest.getRefundId());
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        return result;
    }

    @Override
    public String parsePayNotify(HttpServletRequest request) {
        try {
            Map<String, String> contentMap = RequestUtil.parse(request);
            //参数校验
            this.payNotifyParamValid(contentMap);
            //支付流水
            String payId = contentMap.get("out_trade_no");
            //签名校验
            boolean signVerified = AlipaySignature.rsaCheckV1(contentMap,
                    payProperties.getAlipay().getPublicKey(), BaseCodeConstant.DEFAULT_CHARSET,
                    payProperties.getAlipay().getSignType());
            if (!signVerified) {
                logger.debug("支付宝异步通知，签名校验失败：" + payId);
                return AlipayConstant.FAIL_RETURN_CDDE;
            }
            //第三方网关号
            String outTradeNo = contentMap.get("trade_no");
            //实际支付金额
            String actualPrice = contentMap.get("total_amount");
            //支付状态
            String payStatus = contentMap.get("trade_status");
            if (AlipayConstant.PAY_SUCCESS_FLAG.equals(payStatus) || AlipayConstant.PAY_FINISHED_FLAG.equals(payStatus)) {
                if (!callbackPaySuccess(new QueryPayResponse(payId, outTradeNo, actualPrice))) {
                    return AlipayConstant.FAIL_RETURN_CDDE;
                }
            }
            return AlipayConstant.SUCCESS_RETURN_CODE;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public String parseRefundNotify(HttpServletRequest request) {
        //支付宝无退款通知
        return null;
    }

    private void payNotifyParamValid(final Map<String, String> contentMap) {
        if (!contentMap.containsKey("trade_no")) {
            throw new ServiceException("trade_no不能为空");
        }
        if (!contentMap.containsKey("out_trade_no")) {
            throw new ServiceException("out_trade_no不能为空");
        }
        if (!contentMap.containsKey("total_amount")) {
            throw new ServiceException("total_amount不能为空");
        }
        if (!contentMap.containsKey("sign")) {
            throw new ServiceException("sign不能为空");
        }
    }
}
