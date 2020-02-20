package matrix.module.pay.builder.response.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayPayResponse extends WepayBaseResponse {

    private static final long serialVersionUID = 1L;

    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("prepay_id")
    private String prepayId;

    @JsonProperty("code_url")
    private String codeUrl;

    @JsonProperty("mweb_url")
    private String mwebUrl;

    public String getTradeType() {
        return tradeType;
    }

    public WepayPayResponse setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public WepayPayResponse setPrepayId(String prepayId) {
        this.prepayId = prepayId;
        return this;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public WepayPayResponse setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
        return this;
    }

    public String getMwebUrl() {
        return mwebUrl;
    }

    public WepayPayResponse setMwebUrl(String mwebUrl) {
        this.mwebUrl = mwebUrl;
        return this;
    }
}
