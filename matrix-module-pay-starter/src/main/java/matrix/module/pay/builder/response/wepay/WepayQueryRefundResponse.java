package matrix.module.pay.builder.response.wepay;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vwkit.springboot.utils.JacksonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayQueryRefundResponse extends WepayBaseResponse {

    private static final long serialVersionUID = 1L;

    @JsonProperty("total_refund_count")
    private Long totalRefundCount;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("total_fee")
    private Long totalFee;

    @JsonProperty("settlement_total_fee")
    private Long settlementTotalFee;

    @JsonProperty("fee_type")
    private String feeType;

    @JsonProperty("cash_fee")
    private Long cashFee;

    @JsonProperty("refund_count")
    private Long refundCount;

    private List<Refund> refunds;

    public static WepayQueryRefundResponse parse(String json) {
        WepayQueryRefundResponse response = JacksonUtil.parseJson(json, WepayQueryRefundResponse.class);
        if (response.getRefundCount() != null && response.getRefundCount() > 0) {
            response.setRefunds(new ArrayList<>());
            JSONObject jsonObject = JSONObject.parseObject(json);
            for (int i = 0; i < response.getRefundCount(); i++) {
                Refund refund = new Refund()
                        .setOutRefundNo(jsonObject.getString("out_refund_no_" + i))
                        .setRefundId(jsonObject.getString("refund_id_" + i))
                        .setRefundChannel(jsonObject.getString("refund_channel_" + i))
                        .setRefundFee(jsonObject.getLong("refund_fee_" + i))
                        .setSettlementRefundFee(jsonObject.getLong("settlement_refund_fee_" + i))
                        .setCouponRefundCount(jsonObject.getLong("coupon_refund_count_" + i))
                        .setCouponRefundFee(jsonObject.getLong("coupon_refund_fee_" + i))
                        .setRefundStatus(jsonObject.getString("refund_status_" + i))
                        .setRefundAccount(jsonObject.getString("refund_account_" + i))
                        .setRefundRecvAccout(jsonObject.getString("refund_recv_accout_" + i))
                        .setRefundSuccessTime(jsonObject.getString("refund_success_time_" + i));
                if (refund.getCouponRefundCount() != null && refund.getCouponRefundCount() > 0) {
                    refund.setCoupons(new ArrayList<>());
                    for (int j = 0; j < refund.getCouponRefundCount(); j++) {
                        Coupon coupon = new Coupon()
                                .setCouponId(jsonObject.getString("coupon_refund_id_" + i + "_" + j))
                                .setCouponType(jsonObject.getString("coupon_type_" + i + "_" + j))
                                .setCouponFee(jsonObject.getLong("coupon_refund_fee_" + i + "_" + j));
                        refund.getCoupons().add(coupon);
                    }
                }
                response.getRefunds().add(refund);
            }
        }
        return response;
    }

    public Long getTotalRefundCount() {
        return totalRefundCount;
    }

    public WepayQueryRefundResponse setTotalRefundCount(Long totalRefundCount) {
        this.totalRefundCount = totalRefundCount;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public WepayQueryRefundResponse setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayQueryRefundResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public WepayQueryRefundResponse setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public Long getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public WepayQueryRefundResponse setSettlementTotalFee(Long settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
        return this;
    }

    public String getFeeType() {
        return feeType;
    }

    public WepayQueryRefundResponse setFeeType(String feeType) {
        this.feeType = feeType;
        return this;
    }

    public Long getCashFee() {
        return cashFee;
    }

    public WepayQueryRefundResponse setCashFee(Long cashFee) {
        this.cashFee = cashFee;
        return this;
    }

    public Long getRefundCount() {
        return refundCount;
    }

    public WepayQueryRefundResponse setRefundCount(Long refundCount) {
        this.refundCount = refundCount;
        return this;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public WepayQueryRefundResponse setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
        return this;
    }

    public static class Refund {

        private String outRefundNo;

        private String refundId;

        private String refundChannel;

        private Long refundFee;

        private Long settlementRefundFee;

        private String refundStatus;

        private String refundAccount;

        private String refundRecvAccout;

        private String refundSuccessTime;

        private Long couponRefundCount;

        private Long couponRefundFee;

        private List<Coupon> coupons;

        public String getOutRefundNo() {
            return outRefundNo;
        }

        public Refund setOutRefundNo(String outRefundNo) {
            this.outRefundNo = outRefundNo;
            return this;
        }

        public String getRefundId() {
            return refundId;
        }

        public Refund setRefundId(String refundId) {
            this.refundId = refundId;
            return this;
        }

        public String getRefundChannel() {
            return refundChannel;
        }

        public Refund setRefundChannel(String refundChannel) {
            this.refundChannel = refundChannel;
            return this;
        }

        public Long getRefundFee() {
            return refundFee;
        }

        public Refund setRefundFee(Long refundFee) {
            this.refundFee = refundFee;
            return this;
        }

        public Long getSettlementRefundFee() {
            return settlementRefundFee;
        }

        public Refund setSettlementRefundFee(Long settlementRefundFee) {
            this.settlementRefundFee = settlementRefundFee;
            return this;
        }

        public String getRefundStatus() {
            return refundStatus;
        }

        public Refund setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
            return this;
        }

        public String getRefundAccount() {
            return refundAccount;
        }

        public Refund setRefundAccount(String refundAccount) {
            this.refundAccount = refundAccount;
            return this;
        }

        public String getRefundRecvAccout() {
            return refundRecvAccout;
        }

        public Refund setRefundRecvAccout(String refundRecvAccout) {
            this.refundRecvAccout = refundRecvAccout;
            return this;
        }

        public String getRefundSuccessTime() {
            return refundSuccessTime;
        }

        public Refund setRefundSuccessTime(String refundSuccessTime) {
            this.refundSuccessTime = refundSuccessTime;
            return this;
        }

        public Long getCouponRefundCount() {
            return couponRefundCount;
        }

        public Refund setCouponRefundCount(Long couponRefundCount) {
            this.couponRefundCount = couponRefundCount;
            return this;
        }

        public Long getCouponRefundFee() {
            return couponRefundFee;
        }

        public Refund setCouponRefundFee(Long couponRefundFee) {
            this.couponRefundFee = couponRefundFee;
            return this;
        }

        public List<Coupon> getCoupons() {
            return coupons;
        }

        public Refund setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
            return this;
        }
    }

}
