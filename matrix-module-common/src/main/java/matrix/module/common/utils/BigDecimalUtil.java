package matrix.module.common.utils;

import java.math.BigDecimal;

/**
 * @author: wangcheng
 * @date 2019/5/4
 */
public class BigDecimalUtil {

    public static long yuanToCents(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(100L)).longValue();
    }

    public static BigDecimal centsToYuan(long price) {
        return BigDecimal.valueOf(price).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);
    }
}
