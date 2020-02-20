package matrix.module.pay.builder.response.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("return_code")
    private String returnCode;

    @JsonProperty("return_msg")
    private String returnMsg;

    @JsonProperty("appid")
    private String appId;

    @JsonProperty("mch_id")
    private String mchId;

    @JsonProperty("nonce_str")
    private String nonceStr;

    private String sign;

    @JsonProperty("result_code")
    private String resultCode;

    @JsonProperty("err_code")
    private String errCode;

    @JsonProperty("err_code_des")
    private String errCodeDes;

    public String getReturnCode() {
        return returnCode;
    }

    public WepayBaseResponse setReturnCode(String returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public WepayBaseResponse setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public WepayBaseResponse setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public WepayBaseResponse setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public WepayBaseResponse setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public WepayBaseResponse setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public WepayBaseResponse setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getErrCode() {
        return errCode;
    }

    public WepayBaseResponse setErrCode(String errCode) {
        this.errCode = errCode;
        return this;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public WepayBaseResponse setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
        return this;
    }

    public static class Coupon {

        private String couponType;

        private String couponId;

        private Long couponFee;

        public String getCouponType() {
            return couponType;
        }

        public Coupon setCouponType(String couponType) {
            this.couponType = couponType;
            return this;
        }

        public String getCouponId() {
            return couponId;
        }

        public Coupon setCouponId(String couponId) {
            this.couponId = couponId;
            return this;
        }

        public Long getCouponFee() {
            return couponFee;
        }

        public Coupon setCouponFee(Long couponFee) {
            this.couponFee = couponFee;
            return this;
        }
    }
}
