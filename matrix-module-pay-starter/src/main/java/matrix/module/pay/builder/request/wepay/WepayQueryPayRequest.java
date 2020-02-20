package matrix.module.pay.builder.request.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayQueryPayRequest extends WepayBaseRequest {

    private static final long serialVersionUID = 1L;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    public String getTransactionId() {
        return transactionId;
    }

    public WepayQueryPayRequest setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayQueryPayRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }
}
