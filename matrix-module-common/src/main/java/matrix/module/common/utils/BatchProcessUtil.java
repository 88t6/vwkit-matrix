package matrix.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import matrix.module.common.exception.ServiceException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 批处理工具类
 * @author wangcheng
 * 2021/4/21
 **/
@Slf4j
public class BatchProcessUtil {

    /**
     * 处理函数
     * @param data 原批处理数据
     * @param size 每次批处理数量
     * @param retryMaxTimes 最大重试次数
     * @param callBack 回调函数
     * @param <T> 传入参数类型
     * @param <S> 传出参数类型
     * @return 传出参数集合
     */
    public static <T, S> List<S> retry(List<T> data, int size, int retryMaxTimes, CallBack<T, S> callBack) {
        if (CollectionUtils.isEmpty(data)) {
            return new ArrayList<>();
        }
        //获取配置文件
        int failTime = 0;
        int batch = data.size() % size == 0 ? data.size() / size : ((data.size() / size) + 1);
        //定义返回参数
        List<S> result = new ArrayList<>();
        for (int i = 0; i < batch;) {
            try {
                List<T> batchData = data.subList(i * size, Math.min((i + 1) * size, data.size()));
                if (callBack != null) {
                    List<S> list = callBack.invoke(batchData);
                    if (!CollectionUtils.isEmpty(list)) {
                        result.addAll(list);
                    }
                }
                i++;
                //重新计数
                failTime = 0;
            } catch (Exception e) {
                if (failTime >= retryMaxTimes) {
                    log.error("处理数据失败", e);
                    throw new ServiceException(e.getMessage(), e);
                }
                //失败计数 +1
                failTime++;
            }
        }
        return result;
    }

    /**
     * 处理函数
     * @param retryMaxTimes 最大重试次数
     * @param callBack 回调函数
     * @param <T> 传出参数类型
     * @return 传出参数集合
     */
    public static <T> T retry(int retryMaxTimes, CallBackNoArgs<T> callBack) {
        //获取配置文件
        int failTime = 0;
        //定义返回参数
        T result = null;
        while (failTime < retryMaxTimes) {
            try {
                if (callBack != null) {
                    result = callBack.invoke();
                }
                return result;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                failTime++;
            }
        }
        throw new ServiceException("重试次数达到最大值");
    }

    /**
     * 回调无参
     * @param <T>
     */
    public interface CallBackNoArgs<T> {

        /**
         * 回调函数
         * @return 处理后需要返回的值
         */
        T invoke();
    }

    /**
     * 回调
     * @param <T>
     * @param <S>
     */
    @FunctionalInterface
    public interface CallBack<T, S> {

        /**
         * 调用函数
         * @param batchData 批处理数据
         * @return 处理后需要返回的值
         */
        List<S> invoke(List<T> batchData);

    }
}
