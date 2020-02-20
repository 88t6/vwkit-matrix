package matrix.module.pay.builder.request.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayQueryRefundRequest extends WepayBaseRequest {

    private static final long serialVersionUID = 1L;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("out_refund_no")
    private String outRefundNo;

    @JsonProperty("refund_id")
    private String refundId;

    private Integer offset;

    public String getTransactionId() {
        return transactionId;
    }

    public WepayQueryRefundRequest setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayQueryRefundRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public WepayQueryRefundRequest setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
        return this;
    }

    public String getRefundId() {
        return refundId;
    }

    public WepayQueryRefundRequest setRefundId(String refundId) {
        this.refundId = refundId;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public WepayQueryRefundRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }
}
