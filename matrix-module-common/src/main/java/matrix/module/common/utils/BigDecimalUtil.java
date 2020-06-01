package matrix.module.common.utils;

import java.math.BigDecimal;

/**
 * @author wangcheng
 */
public class BigDecimalUtil {

    public static Long yuanToCents(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(100L)).longValue();
    }

    public static BigDecimal centsToYuan(long price) {
        return BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);
    }
}
