package matrix.module.pay.builder.body;

import java.io.Serializable;

/**
 * @author wangcheng
 * @date 2019/5/5
 */
public class WepayPayBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prepayId;

    private String url;

    private Long qrCodeWidth;

    private String payModeCode;

    public String getPrepayId() {
        return prepayId;
    }

    public WepayPayBody setPrepayId(String prepayId) {
        this.prepayId = prepayId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WepayPayBody setUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getQrCodeWidth() {
        return qrCodeWidth;
    }

    public WepayPayBody setQrCodeWidth(Long qrCodeWidth) {
        this.qrCodeWidth = qrCodeWidth;
        return this;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public WepayPayBody setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode;
        return this;
    }
}
