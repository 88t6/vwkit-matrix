package matrix.module.pay.builder.request.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayRefundRequest extends WepayBaseRequest {

    private static final long serialVersionUID = 1L;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("out_refund_no")
    private String outRefundNo;

    @JsonProperty("total_fee")
    private Long totalFee;

    @JsonProperty("refund_fee")
    private Long refundFee;

    @JsonProperty("refund_fee_type")
    private String refundFeeType;

    @JsonProperty("refund_desc")
    private String refundDesc;

    @JsonProperty("refund_account")
    private String refundAccount;

    @JsonProperty("notify_url")
    private String notifyUrl;

    public String getTransactionId() {
        return transactionId;
    }

    public WepayRefundRequest setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayRefundRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public WepayRefundRequest setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
        return this;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public WepayRefundRequest setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public Long getRefundFee() {
        return refundFee;
    }

    public WepayRefundRequest setRefundFee(Long refundFee) {
        this.refundFee = refundFee;
        return this;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public WepayRefundRequest setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
        return this;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public WepayRefundRequest setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
        return this;
    }

    public String getRefundAccount() {
        return refundAccount;
    }

    public WepayRefundRequest setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public WepayRefundRequest setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }
}
