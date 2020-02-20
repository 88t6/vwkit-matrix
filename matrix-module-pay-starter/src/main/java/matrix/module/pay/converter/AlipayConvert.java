package matrix.module.pay.converter;

import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.*;
import matrix.module.based.utils.JacksonUtil;
import matrix.module.common.exception.ServiceException;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.builder.request.alipay.AlipayPayRequest;
import matrix.module.pay.builder.request.alipay.AlipayQueryPayRequest;
import matrix.module.pay.builder.request.alipay.AlipayQueryRefundRequest;
import matrix.module.pay.builder.request.alipay.AlipayRefundRequest;
import matrix.module.pay.constants.AlipayConstant;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.vo.PayVo;
import matrix.module.pay.vo.RefundVo;

/**
 * @author wangcheng
 * @date 2019/4/26
 */
public class AlipayConvert {

    public static final AlipayRequest<? extends AlipayResponse> convertPayRequest(PayMode payMode, PayVo payVo, String notifyUrl, String returnUrl) {
        AlipayPayRequest payRequest = new AlipayPayRequest()
                .setOutTradeNo(payVo.getPayId())
                .setTotalAmount(payVo.getPrice().toPlainString())
                .setSubject(payVo.getTitle())
                .setBody(payVo.getDesc());
        AlipayRequest<? extends AlipayResponse> alipayRequest = null;
        if (PayMode.PC.equals(payMode) || PayMode.QrCode.equals(payMode)) {
            alipayRequest = new AlipayTradePagePayRequest();
            if (PayMode.QrCode.equals(payMode)) {
                payRequest.setQrPayMode(AlipayConstant.QR_PAY_MODE);
                payRequest.setQrcodeWidth(String.valueOf(payVo.getQrCodeWidth()));
            }
            payRequest.setProductCode(AlipayConstant.ALIPAY_PC_PRODUCT_CODE);
            ((AlipayTradePagePayRequest) alipayRequest).setBizContent(JacksonUtil.toJsonString(payRequest));
        } else if (PayMode.H5.equals(payMode)) {
            alipayRequest = new AlipayTradeWapPayRequest();
            payRequest.setProductCode(AlipayConstant.ALIPAY_H5_PRODUCT_CODE);
            ((AlipayTradeWapPayRequest) alipayRequest).setBizContent(JacksonUtil.toJsonString(payRequest));
        } else if (PayMode.APP.equals(payMode)) {
            alipayRequest = new AlipayTradeAppPayRequest();
            payRequest.setProductCode(AlipayConstant.ALIPAY_APP_PRODUCT_CODE);
            ((AlipayTradeAppPayRequest) alipayRequest).setBizContent(JacksonUtil.toJsonString(payRequest));
        }
        if (alipayRequest == null) {
            throw new ServiceException("No Pay Mode!");
        }
        alipayRequest.setNotifyUrl(notifyUrl);
        alipayRequest.setReturnUrl(returnUrl);
        return alipayRequest;
    }

    public static final AlipayTradeRefundRequest convertRefundRequest(RefundVo refundVo) {
        AlipayRefundRequest refundRequest = new AlipayRefundRequest()
                .setOutRequestNo(refundVo.getRefundId())
                .setOutTradeNo(refundVo.getPayId())
                .setTradeNo(refundVo.getOutTradeNo())
                .setRefundAmount(refundVo.getRefundAmount().toPlainString());
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizContent(JacksonUtil.toJsonString(refundRequest));
        return alipayRequest;
    }

    public static final AlipayTradeQueryRequest convertQueryPayRequest(String payId) {
        AlipayQueryPayRequest queryPayRequest = new AlipayQueryPayRequest().setOutTradeNo(payId);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(JacksonUtil.toJsonString(queryPayRequest));
        return request;
    }

    public static final AlipayTradeFastpayRefundQueryRequest convertQueryRefundRequest(QueryRefundRequest refundRequest) {
        AlipayQueryRefundRequest queryRefundRequest = new AlipayQueryRefundRequest();
        queryRefundRequest.setOutRequestNo(refundRequest.getRefundId());
        queryRefundRequest.setOutTradeNo(refundRequest.getPayId());
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizContent(JacksonUtil.toJsonString(queryRefundRequest));
        return request;
    }

}
