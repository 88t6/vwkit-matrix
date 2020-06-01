package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangcheng
 */
public class ThreadUtil {

    private static final int CORE_POOL_SIZE = 10;

    private static final int MAX_POOL_SIZE = 20;

    private static final long KEEP_ALIVE_TIME = 60;

    private static final ThreadPoolExecutor DEFAULT_THREAD_POOL = newThreadPool();

    /**
     * 获取线程池
     *
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor newThreadPool() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 开启一个线程
     */
    public static void startThread(Runnable runnable) {
        DEFAULT_THREAD_POOL.execute(runnable);
    }

    /**
     * 睡眠线程
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
