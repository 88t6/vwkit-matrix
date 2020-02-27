package matrix.module.pay.converter;

import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.BigDecimalUtil;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.constants.WepayConstant;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.vo.PayVo;
import matrix.module.pay.vo.RefundVo;

/**
 * @author: wangcheng
 * @date 2019/5/4
 */
public class WepayConvert {

    public static WxPayUnifiedOrderRequest convertPayRequest(PayMode payMode, PayVo payVo, String notifyUrl) {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest()
                .setProductId(payVo.getOrderId())
                .setBody(payVo.getTitle())
                .setDetail(payVo.getDesc())
                .setOutTradeNo(payVo.getPayId())
                .setTotalFee(BigDecimalUtil.yuanToCents(payVo.getPrice()).intValue())
                .setSpbillCreateIp("127.0.0.1")
                .setOpenid(payVo.getOpenId());
        request.setNotifyUrl(notifyUrl);
        request.setSignType("MD5");
        if (PayMode.PC.equals(payMode)) {
            payVo.setQrCodeWidth(300L);
            request.setTradeType(WepayConstant.NATIVE_PAY);
        } else if (PayMode.H5.equals(payMode)) {
            request.setTradeType(WepayConstant.H5_PAY);
        } else if (PayMode.APP.equals(payMode)) {
            request.setTradeType(WepayConstant.APP_PAY);
        } else if (PayMode.QrCode.equals(payMode)) {
            request.setTradeType(WepayConstant.NATIVE_PAY);
        } else if (PayMode.WEJSAPI.equals(payMode)) {
            request.setTradeType(WepayConstant.JSAPI_PAY);
        } else {
            throw new ServiceException("No Pay Mode!");
        }
        return request;
    }

    public static WxPayOrderQueryRequest convertPayQueryRequest(String payId) {
        WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
        request.setOutTradeNo(payId);
        return request;
    }

    public static WxPayRefundRequest convertRefundRequest(RefundVo refundVo, MatrixPayEntity payEntity, String notifyUrl) {
        WxPayRefundRequest request = new WxPayRefundRequest();
        request.setTransactionId(refundVo.getOutTradeNo());
        request.setOutTradeNo(refundVo.getPayId());
        request.setOutRefundNo(refundVo.getRefundId());
        request.setTotalFee(BigDecimalUtil.yuanToCents(payEntity.getActualPrice()).intValue());
        request.setRefundFee(BigDecimalUtil.yuanToCents(refundVo.getRefundAmount()).intValue());
        request.setNotifyUrl(notifyUrl);
        return request;
    }

    public static WxPayRefundQueryRequest convertQueryRefundRequest(QueryRefundRequest queryRefundRequest) {
        WxPayRefundQueryRequest request = new WxPayRefundQueryRequest();
        request.setOutTradeNo(queryRefundRequest.getPayId());
        request.setOutRefundNo(queryRefundRequest.getRefundId());
        return request;
    }
}
