package matrix.module.pay.builder.request.wepay;

import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.module.pay.builder.BasePayRequest;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayPayRequest extends WepayBaseRequest {

    private static final long serialVersionUID = 1L;

    @JsonProperty("device_info")
    private String deviceInfo;

    private String body;

    private String detail;

    private String attach;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("fee_type")
    private String feeType;

    @JsonProperty("total_fee")
    private Long totalFee;

    @JsonProperty("spbill_create_ip")
    private String spbillCreateIp;

    @JsonProperty("time_start")
    private String timeStart;

    @JsonProperty("time_expire")
    private String timeExpire;

    @JsonProperty("goods_tag")
    private String goodsTag;

    @JsonProperty("notify_url")
    private String notifyUrl;

    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("limit_pay")
    private String limitPay;

    @JsonProperty("openid")
    private String openId;

    private String receipt;

    @JsonProperty("scene_info")
    private SceneInfo sceneInfo;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public WepayPayRequest setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String getBody() {
        return body;
    }

    public WepayPayRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public WepayPayRequest setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getAttach() {
        return attach;
    }

    public WepayPayRequest setAttach(String attach) {
        this.attach = attach;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WepayPayRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getFeeType() {
        return feeType;
    }

    public WepayPayRequest setFeeType(String feeType) {
        this.feeType = feeType;
        return this;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public WepayPayRequest setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public WepayPayRequest setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
        return this;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public WepayPayRequest setTimeStart(String timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public WepayPayRequest setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
        return this;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public WepayPayRequest setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public WepayPayRequest setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public WepayPayRequest setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public WepayPayRequest setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public WepayPayRequest setLimitPay(String limitPay) {
        this.limitPay = limitPay;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WepayPayRequest setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getReceipt() {
        return receipt;
    }

    public WepayPayRequest setReceipt(String receipt) {
        this.receipt = receipt;
        return this;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public WepayPayRequest setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
        return this;
    }

    public static class SceneInfo extends BasePayRequest {

        private static final long serialVersionUID = 1L;

        private String id;

        private String name;

        @JsonProperty("area_code")
        private String areaCode;

        private String address;

        public String getId() {
            return id;
        }

        public SceneInfo setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public SceneInfo setName(String name) {
            this.name = name;
            return this;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public SceneInfo setAreaCode(String areaCode) {
            this.areaCode = areaCode;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public SceneInfo setAddress(String address) {
            this.address = address;
            return this;
        }
    }
}
