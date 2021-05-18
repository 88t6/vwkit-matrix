package matrix.module.common.utils;

import matrix.module.common.helper.Assert;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工具
 * @author wangcheng
 */
public class SingletonUtil {

    /**
     * 单例字典
     */
    private static final Map<Object, Object> SINGLETON_DICT = new ConcurrentHashMap<>();

    /**
     * 获取实例
     */
    @SuppressWarnings("unchecked")
	public static <T> T get(Object key, CallBack<T> callBack) {
        Assert.notNullTip(key, "key");
        T result = (T) SINGLETON_DICT.get(key);
        if (result != null) {
            return result;
        }
        if (callBack != null) {
            result = callBack.execute();
            if (result != null) {
                SINGLETON_DICT.put(key, result);
            }
        }
        return result;
    }

    /**
     * 回调函数
     */
    public interface CallBack<T> {

        /**
         * 执行函数
         *
         * @return T
         */
        T execute();
    }
}
