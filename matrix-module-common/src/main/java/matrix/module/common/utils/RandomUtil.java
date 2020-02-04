package matrix.module.common.utils;

import java.util.UUID;

/**
 * @author wangcheng
 */
public class RandomUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getTimeMillis() {
    	return String.valueOf(System.currentTimeMillis());
    }
}
