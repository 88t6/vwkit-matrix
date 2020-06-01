package matrix.module.pay.builder.response;

import java.io.Serializable;

/**
 * @author wangcheng
 */
public class QueryPayResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String payId;

    private String outTradeNo;

    private String actualPrice;

    public QueryPayResponse(String payId, String outTradeNo, String actualPrice) {
        this.payId = payId;
        this.outTradeNo = outTradeNo;
        this.actualPrice = actualPrice;
    }

    public String getPayId() {
        return payId;
    }

    public QueryPayResponse setPayId(String payId) {
        this.payId = payId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public QueryPayResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public QueryPayResponse setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
        return this;
    }
}
