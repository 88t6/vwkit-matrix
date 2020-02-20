package matrix.module.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.common.helper.Assert;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.RandomUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: wangcheng
 * @date 2019/4/28
 */
@Data
@Accessors(chain = true)
public class RefundVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String refundId = MD5.get32(RandomUtil.getUUID());

    private String payId;

    private String outTradeNo;

    private BigDecimal refundAmount;

    public void validate() {
        if (StringUtils.isEmpty(outTradeNo) && StringUtils.isEmpty(payId)) {
            throw new IllegalArgumentException("outTradeNo和payId不能同时为空");
        }
        Assert.isNotNull(refundAmount, "refundAmount");
    }
}
