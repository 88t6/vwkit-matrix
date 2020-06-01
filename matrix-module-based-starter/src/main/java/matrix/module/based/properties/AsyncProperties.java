package matrix.module.based.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@ConfigurationProperties(prefix = "async")
@Data
@Accessors(chain = true)
public class AsyncProperties {

    // 设置核心线程
    private Integer corePoolSize = 10;

    // 设置最大线程
    private Integer maxPoolSize = 30;

    // 设置线程队列最大线程数
    private Integer queueCapacity = 2000;

}
