package matrix.module.pay.builder.response.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author: wangcheng
 * @date 2019/5/5
 */
public class WepayNotifyRefundResponse implements Serializable {

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

    @JsonProperty("req_info")
    private String reqInfo;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getReqInfo() {
        return reqInfo;
    }

    public void setReqInfo(String reqInfo) {
        this.reqInfo = reqInfo;
    }

    public static class Info implements Serializable {

        private static final long serialVersionUID = 1L;

        @JsonProperty("transaction_id")
        private String transactionId;

        @JsonProperty("out_trade_no")
        private String outTradeNo;

        @JsonProperty("refund_id")
        private String refundId;

        @JsonProperty("out_refund_no")
        private String outRefundNo;

        @JsonProperty("total_fee")
        private Long totalFee;

        @JsonProperty("settlement_total_fee")
        private Long settlementTotalFee;

        @JsonProperty("refund_fee")
        private Long refundFee;

        @JsonProperty("settlement_refund_fee")
        private Long settlementRefundFee;

        @JsonProperty("refund_status")
        private String refundStatus;

        @JsonProperty("success_time")
        private String successTime;

        @JsonProperty("refund_recv_accout")
        private String refundRecvAccout;

        @JsonProperty("refund_account")
        private String refundAccount;

        @JsonProperty("refund_request_source")
        private String refundRequestSource;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getRefundId() {
            return refundId;
        }

        public void setRefundId(String refundId) {
            this.refundId = refundId;
        }

        public String getOutRefundNo() {
            return outRefundNo;
        }

        public void setOutRefundNo(String outRefundNo) {
            this.outRefundNo = outRefundNo;
        }

        public Long getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(Long totalFee) {
            this.totalFee = totalFee;
        }

        public Long getSettlementTotalFee() {
            return settlementTotalFee;
        }

        public void setSettlementTotalFee(Long settlementTotalFee) {
            this.settlementTotalFee = settlementTotalFee;
        }

        public Long getRefundFee() {
            return refundFee;
        }

        public void setRefundFee(Long refundFee) {
            this.refundFee = refundFee;
        }

        public Long getSettlementRefundFee() {
            return settlementRefundFee;
        }

        public void setSettlementRefundFee(Long settlementRefundFee) {
            this.settlementRefundFee = settlementRefundFee;
        }

        public String getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
        }

        public String getSuccessTime() {
            return successTime;
        }

        public void setSuccessTime(String successTime) {
            this.successTime = successTime;
        }

        public String getRefundRecvAccout() {
            return refundRecvAccout;
        }

        public void setRefundRecvAccout(String refundRecvAccout) {
            this.refundRecvAccout = refundRecvAccout;
        }

        public String getRefundAccount() {
            return refundAccount;
        }

        public void setRefundAccount(String refundAccount) {
            this.refundAccount = refundAccount;
        }

        public String getRefundRequestSource() {
            return refundRequestSource;
        }

        public void setRefundRequestSource(String refundRequestSource) {
            this.refundRequestSource = refundRequestSource;
        }
    }
}
