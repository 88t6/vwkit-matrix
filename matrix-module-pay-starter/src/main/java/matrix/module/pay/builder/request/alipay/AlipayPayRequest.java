package matrix.module.pay.builder.request.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import matrix.module.based.utils.JacksonUtil;
import matrix.module.pay.builder.BasePayRequest;

/**
 * @author wangcheng
 * @date 2019/04/28
 */
public class AlipayPayRequest extends BasePayRequest {

    private static final long serialVersionUID = 1L;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("total_amount")
    private String totalAmount;

    private String subject;

    private String body;

    @JsonProperty("passback_params")
    private String passbackParams;

    @JsonProperty("goods_type")
    private String goodsType;

    @JsonProperty("timeout_express")
    private String timeoutExpress;

    @JsonProperty("qr_pay_mode")
    private String qrPayMode;

    @JsonProperty("qrcode_width")
    private String qrcodeWidth;

    @JsonProperty("promo_params")
    private String promoParams;

    @JsonProperty("store_id")
    private String storeId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public AlipayPayRequest setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public AlipayPayRequest setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public AlipayPayRequest setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public AlipayPayRequest setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public AlipayPayRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public String getPromoParams() {
        return promoParams;
    }

    public AlipayPayRequest setPromoParams(String promoParams) {
        this.promoParams = promoParams;
        return this;
    }

    public String getStoreId() {
        return storeId;
    }

    public AlipayPayRequest setStoreId(String storeId) {
        this.storeId = storeId;
        return this;
    }

    public String getPassbackParams() {
        return passbackParams;
    }

    public AlipayPayRequest setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
        return this;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public AlipayPayRequest setGoodsType(String goodsType) {
        this.goodsType = goodsType;
        return this;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public AlipayPayRequest setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
        return this;
    }

    public String getQrPayMode() {
        return qrPayMode;
    }

    public AlipayPayRequest setQrPayMode(String qrPayMode) {
        this.qrPayMode = qrPayMode;
        return this;
    }

    public String getQrcodeWidth() {
        return qrcodeWidth;
    }

    public AlipayPayRequest setQrcodeWidth(String qrcodeWidth) {
        this.qrcodeWidth = qrcodeWidth;
        return this;
    }

    @Override
    public String toString() {
        return JacksonUtil.toJsonString(this);
    }
}
