package matrix.module.pay.converter;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.BigDecimalUtil;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.builder.request.wepay.WepayPayRequest;
import matrix.module.pay.builder.request.wepay.WepayQueryPayRequest;
import matrix.module.pay.builder.request.wepay.WepayQueryRefundRequest;
import matrix.module.pay.builder.request.wepay.WepayRefundRequest;
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

    public static WepayPayRequest convertPayRequest(PayMode payMode, PayVo payVo, String notifyUrl) {
        WepayPayRequest request = new WepayPayRequest()
                .setBody(payVo.getTitle())
                .setDetail(payVo.getDesc())
                .setOutTradeNo(payVo.getPayId())
                .setTotalFee(BigDecimalUtil.yuanToCents(payVo.getPrice()))
                .setSpbillCreateIp("127.0.0.1")
                .setOpenId(payVo.getOpenId())
                .setNotifyUrl(notifyUrl);
        if (PayMode.WEJSAPI.equals(payMode)) {
            request.setTradeType(WepayConstant.JSAPI_PAY);
        } else if (PayMode.H5.equals(payMode)) {
            request.setTradeType(WepayConstant.H5_PAY);
        } else if (PayMode.APP.equals(payMode)) {
            request.setTradeType(WepayConstant.APP_PAY);
        } else if (PayMode.QrCode.equals(payMode)) {
            request.setTradeType(WepayConstant.NATIVE_PAY);
        } else {
            throw new ServiceException("No Pay Mode!");
        }
        return request;
    }

    public static WepayQueryPayRequest convertPayQueryRequest(String payId) {
        return new WepayQueryPayRequest()
                .setOutTradeNo(payId);
    }

    public static WepayRefundRequest convertRefundRequest(RefundVo refundVo, MatrixPayEntity payEntity, String notifyUrl) {
        return new WepayRefundRequest()
                .setTransactionId(refundVo.getOutTradeNo())
                .setOutTradeNo(refundVo.getPayId())
                .setOutRefundNo(refundVo.getRefundId())
                .setTotalFee(BigDecimalUtil.yuanToCents(payEntity.getActualPrice()))
                .setRefundFee(BigDecimalUtil.yuanToCents(refundVo.getRefundAmount()))
                .setNotifyUrl(notifyUrl);
    }

    public static WepayQueryRefundRequest convertQueryRefundRequest(QueryRefundRequest queryRefundRequest) {
        return new WepayQueryRefundRequest()
                .setOutTradeNo(queryRefundRequest.getPayId())
                .setOutRefundNo(queryRefundRequest.getRefundId());
    }
}
