package matrix.module.pay.builder.request.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.modules.pay.builder.BasePayRequest;

/**
 * @author wangcheng
 * @date 2019/4/28
 */
public class AlipayQueryRefundRequest extends BasePayRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 支付宝交易号
     */
    @JsonProperty("trade_no")
    private String tradeNo;

    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /**
     * 退款单号
     */
    @JsonProperty("out_request_no")
    private String outRequestNo;

    public String getTradeNo() {
        return tradeNo;
    }

    public AlipayQueryRefundRequest setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public AlipayQueryRefundRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public AlipayQueryRefundRequest setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
        return this;
    }
}
