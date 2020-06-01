package matrix.module.pay.builder.request.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.module.pay.builder.BasePayRequest;

/**
 * @author wangcheng
 */
public class AlipayRefundRequest extends BasePayRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /**
     * 支付宝交易号
     */
    @JsonProperty("trade_no")
    private String tradeNo;

    /**
     * 需要退款的金额(元)
     */
    @JsonProperty("refund_amount")
    private String refundAmount;

    /**
     * 退款币种信息
     */
    @JsonProperty("refund_currency")
    private String refundCurrency;

    /**
     * 退款原因
     */
    @JsonProperty("refund_reason")
    private String refundReason;

    /**
     * 退款订单号
     */
    @JsonProperty("out_request_no")
    private String outRequestNo;

    /**
     * 操作员编号
     */
    @JsonProperty("operator_id")
    private String operatorId;

    /**
     * 门店编号
     */
    @JsonProperty("store_id")
    private String storeId;

    /**
     * 终端编号
     */
    @JsonProperty("terminal_id")
    private String terminalId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public AlipayRefundRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public AlipayRefundRequest setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public AlipayRefundRequest setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
        return this;
    }

    public String getRefundCurrency() {
        return refundCurrency;
    }

    public AlipayRefundRequest setRefundCurrency(String refundCurrency) {
        this.refundCurrency = refundCurrency;
        return this;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public AlipayRefundRequest setRefundReason(String refundReason) {
        this.refundReason = refundReason;
        return this;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public AlipayRefundRequest setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public AlipayRefundRequest setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getStoreId() {
        return storeId;
    }

    public AlipayRefundRequest setStoreId(String storeId) {
        this.storeId = storeId;
        return this;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public AlipayRefundRequest setTerminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }
}
