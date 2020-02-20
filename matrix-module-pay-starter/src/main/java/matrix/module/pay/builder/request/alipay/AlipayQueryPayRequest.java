package matrix.module.pay.builder.request.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.modules.pay.builder.BasePayRequest;

/**
 * @author wangcheng
 * @date 2019/4/28
 */
public class AlipayQueryPayRequest extends BasePayRequest {

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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public AlipayQueryPayRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public AlipayQueryPayRequest setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }
}
