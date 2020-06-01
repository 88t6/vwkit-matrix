package matrix.module.based.config;

import matrix.module.based.properties.AsyncProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@Configuration
@EnableConfigurationProperties(AsyncProperties.class)
@EnableAsync
public class AsyncAutoConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        // 定义线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程
        taskExecutor.setCorePoolSize(10);
        // 设置最大线程
        taskExecutor.setMaxPoolSize(30);
        // 设置线程队列最大线程数
        taskExecutor.setQueueCapacity(2000);
        // 初始化
        taskExecutor.initialize();
        return taskExecutor;
    }
}
