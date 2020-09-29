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
 */
@Data
@Accessors(chain = true)
public class PayVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //默认随机
    private String payId = MD5.get32(RandomUtil.getUUID());

    private String orderId;

    private BigDecimal price;

    private String title;

    private String desc;

    //可选(默认300)
    private Long qrCodeWidth = 300L;

    //可选
    private String openId;

    public void validate(PayMode payMode) {
        Assert.notNullTip(this.orderId, "orderId");
        Assert.notNullTip(this.price, "price");
        Assert.notNullTip(this.title, "title");
        Assert.notNullTip(this.desc, "desc");
        if (PayMode.QrCode.equals(payMode)) {
            Assert.notNullTip(this.qrCodeWidth, "qrCodeWidth");
        }
        if (PayMode.WEJSAPI.equals(payMode)) {
            Assert.notNullTip(this.openId, "openId");
        }
    }
}
