package matrix.module.pay.builder.response.wepay;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.module.based.utils.JacksonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayQueryPayResponse extends WepayBaseResponse {

    private static final long serialVersionUID = 1L;

    @JsonProperty("device_info")
    private String deviceInfo;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("is_subscribe")
    private String isSubscribe;

    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("trade_state")
    private String tradeState;

    @JsonProperty("bank_type")
    private String bankType;

    @JsonProperty("total_fee")
    private Long totalFee;

    @JsonProperty("settlement_total_fee")
    private Long settlementTotalFee;

    @JsonProperty("fee_type")
    private String feeType;

    @JsonProperty("cash_fee")
    private String cashFee;

    @JsonProperty("cash_fee_type")
    private String cashFeeType;

    @JsonProperty("coupon_fee")
    private Long couponFee;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    private String attach;

    @JsonProperty("time_end")
    private String timeEnd;

    @JsonProperty("trade_state_desc")
    private String tradeStateDesc;

    @JsonProperty("coupon_count")
    private Long couponCount;

    private List<Coupon> coupons;

    public static WepayQueryPayResponse parse(String json) {
        WepayQueryPayResponse response = JacksonUtil.parseJson(json, WepayQueryPayResponse.class);
        if (response.getCouponCount() != null && response.getCouponCount() > 0) {
            response.setCoupons(new ArrayList<>());
            JSONObject jsonObject = JSONObject.parseObject(json);
            for (int i = 0; i < response.getCouponCount(); i++) {
                Coupon coupon = new Coupon()
                        .setCouponId(jsonObject.getString("coupon_id_" + i))
                        .setCouponType(jsonObject.getString("coupon_type_" + i))
                        .setCouponFee(jsonObject.getLong("coupon_fee_" + i));
                response.getCoupons().add(coupon);
            }
        }
        return response;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public WepayQueryPayResponse setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WepayQueryPayResponse setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public WepayQueryPayResponse setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public WepayQueryPayResponse setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getTradeState() {
        return tradeState;
    }

    public WepayQueryPayResponse setTradeState(String tradeState) {
        this.tradeState = tradeState;
        return this;
    }

    public String getBankType() {
        return bankType;
    }

    public WepayQueryPayResponse setBankType(String bankType) {
        this.bankType = bankType;
        return this;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public WepayQueryPayResponse setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public Long getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public WepayQueryPayResponse setSettlementTotalFee(Long settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
        return this;
    }

    public String getFeeType() {
        return feeType;
    }

    public WepayQueryPayResponse setFeeType(String feeType) {
        this.feeType = feeType;
        return this;
    }

    public String getCashFee() {
        return cashFee;
    }

    public WepayQueryPayResponse setCashFee(String cashFee) {
        this.cashFee = cashFee;
        return this;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public WepayQueryPayResponse setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
        return this;
    }

    public Long getCouponFee() {
        return couponFee;
    }

    public WepayQueryPayResponse setCouponFee(Long couponFee) {
        this.couponFee = couponFee;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public WepayQueryPayResponse setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayQueryPayResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getAttach() {
        return attach;
    }

    public WepayQueryPayResponse setAttach(String attach) {
        this.attach = attach;
        return this;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public WepayQueryPayResponse setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public WepayQueryPayResponse setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
        return this;
    }

    public Long getCouponCount() {
        return couponCount;
    }

    public WepayQueryPayResponse setCouponCount(Long couponCount) {
        this.couponCount = couponCount;
        return this;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public WepayQueryPayResponse setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
        return this;
    }
}
