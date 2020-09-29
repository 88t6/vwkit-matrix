package matrix.module.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.RandomUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: wangcheng
 */
@Data
@Accessors(chain = true)
public class RefundVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //默认为随机值
    private String refundId = MD5.get32(RandomUtil.getUUID());

    //二选一
    private String payId;

    //二选一
    private String outTradeNo;

    private BigDecimal refundAmount;

    public void validate() {
        if (StringUtils.isEmpty(outTradeNo) && StringUtils.isEmpty(payId)) {
            throw new ServiceException("outTradeNo和payId不能同时为空");
        }
        Assert.notNullTip(refundAmount, "refundAmount");
    }
}
