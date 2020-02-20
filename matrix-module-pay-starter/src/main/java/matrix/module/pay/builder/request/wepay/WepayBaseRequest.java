package matrix.module.pay.builder.request.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.modules.pay.builder.BasePayRequest;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayBaseRequest extends BasePayRequest {

    private static final long serialVersionUID = 1L;

    @JsonProperty("appid")
    private String appId;

    @JsonProperty("mch_id")
    private String mchId;

    @JsonProperty("nonce_str")
    private String nonceStr;

    private String sign;

    @JsonProperty("sign_type")
    private String signType;

    public String getAppId() {
        return appId;
    }

    public WepayBaseRequest setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public WepayBaseRequest setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public WepayBaseRequest setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public WepayBaseRequest setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getSignType() {
        return signType;
    }

    public WepayBaseRequest setSignType(String signType) {
        this.signType = signType;
        return this;
    }
}
