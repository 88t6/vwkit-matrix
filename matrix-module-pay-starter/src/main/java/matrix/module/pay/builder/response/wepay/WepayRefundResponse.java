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
public class WepayRefundResponse extends WepayBaseResponse {

    private static final long serialVersionUID = 1L;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("out_refund_no")
    private String outRefundNo;

    @JsonProperty("refund_id")
    private String refundId;

    @JsonProperty("refund_fee")
    private Long refundFee;

    @JsonProperty("settlement_refund_fee")
    private Long settlementRefundFee;

    @JsonProperty("total_fee")
    private Long totalFee;

    @JsonProperty("settlement_total_fee")
    private Long settlementTotalFee;

    @JsonProperty("fee_type")
    private String feeType;

    @JsonProperty("cash_fee")
    private Long cashFee;

    @JsonProperty("cash_fee_type")
    private String cashFeeType;

    @JsonProperty("cash_refund_fee")
    private Long cashRefundFee;

    @JsonProperty("coupon_refund_fee")
    private Long couponRefundFee;

    @JsonProperty("coupon_refund_count")
    private Long couponRefundCount;

    private List<Coupon> coupons;

    public static WepayRefundResponse parse(String json) {
        WepayRefundResponse response = JacksonUtil.parseJson(json, WepayRefundResponse.class);
        if (response.getCouponRefundCount() != null && response.getCouponRefundCount() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            response.setCoupons(new ArrayList<>());
            for (int i = 0; i < response.getCouponRefundCount(); i++) {
                Coupon coupon = new Coupon()
                        .setCouponId(jsonObject.getString("coupon_refund_id_" + i))
                        .setCouponType(jsonObject.getString("coupon_type_" + i))
                        .setCouponFee(jsonObject.getLong("coupon_refund_fee_" + i));
                response.getCoupons().add(coupon);
            }
        }
        return response;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public WepayRefundResponse setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayRefundResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public WepayRefundResponse setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
        return this;
    }

    public String getRefundId() {
        return refundId;
    }

    public WepayRefundResponse setRefundId(String refundId) {
        this.refundId = refundId;
        return this;
    }

    public Long getRefundFee() {
        return refundFee;
    }

    public WepayRefundResponse setRefundFee(Long refundFee) {
        this.refundFee = refundFee;
        return this;
    }

    public Long getSettlementRefundFee() {
        return settlementRefundFee;
    }

    public WepayRefundResponse setSettlementRefundFee(Long settlementRefundFee) {
        this.settlementRefundFee = settlementRefundFee;
        return this;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public WepayRefundResponse setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public Long getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public WepayRefundResponse setSettlementTotalFee(Long settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
        return this;
    }

    public String getFeeType() {
        return feeType;
    }

    public WepayRefundResponse setFeeType(String feeType) {
        this.feeType = feeType;
        return this;
    }

    public Long getCashFee() {
        return cashFee;
    }

    public WepayRefundResponse setCashFee(Long cashFee) {
        this.cashFee = cashFee;
        return this;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public WepayRefundResponse setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
        return this;
    }

    public Long getCashRefundFee() {
        return cashRefundFee;
    }

    public WepayRefundResponse setCashRefundFee(Long cashRefundFee) {
        this.cashRefundFee = cashRefundFee;
        return this;
    }

    public Long getCouponRefundFee() {
        return couponRefundFee;
    }

    public WepayRefundResponse setCouponRefundFee(Long couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
        return this;
    }

    public Long getCouponRefundCount() {
        return couponRefundCount;
    }

    public WepayRefundResponse setCouponRefundCount(Long couponRefundCount) {
        this.couponRefundCount = couponRefundCount;
        return this;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public WepayRefundResponse setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
        return this;
    }

}
