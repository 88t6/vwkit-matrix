package matrix.module.pay.builder.response;

import java.io.Serializable;

/**
 * @author wangcheng
 */
public class QueryRefundResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String payId;

    private String refundId;

    private String outTradeNo;

    private String outRefundNo;

    private String refundFee;

    public QueryRefundResponse(String payId, String refundId, String outTradeNo, String outRefundNo, String refundFee) {
        this.payId = payId;
        this.refundId = refundId;
        this.outTradeNo = outTradeNo;
        this.outRefundNo = outRefundNo;
        this.refundFee = refundFee;
    }

    public String getPayId() {
        return payId;
    }

    public QueryRefundResponse setPayId(String payId) {
        this.payId = payId;
        return this;
    }

    public String getRefundId() {
        return refundId;
    }

    public QueryRefundResponse setRefundId(String refundId) {
        this.refundId = refundId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public QueryRefundResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public QueryRefundResponse setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
        return this;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public QueryRefundResponse setRefundFee(String refundFee) {
        this.refundFee = refundFee;
        return this;
    }
}
