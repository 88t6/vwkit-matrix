package matrix.module.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.common.helper.Assert;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.RandomUtil;
import matrix.module.pay.enums.PayMode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: wangcheng
 * @date 2019/4/26
 */
@Data
@Accessors(chain = true)
public class PayVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String payId = MD5.get32(RandomUtil.getUUID());

    private String orderId;

    private BigDecimal price;

    private String title;

    private String desc;

    private Long qrCodeWidth;

    private String openId;

    public void validate(PayMode payMode) {
        Assert.isNotNull(this.orderId, "orderId");
        Assert.isNotNull(this.price, "price");
        Assert.isNotNull(this.title, "title");
        Assert.isNotNull(this.desc, "desc");
        if (PayMode.QrCode.equals(payMode)) {
            Assert.isNotNull(this.qrCodeWidth, "qrCodeWidth");
        }
        if (PayMode.WEJSAPI.equals(payMode)) {
            Assert.isNotNull(this.openId, "openId");
        }
    }
}
