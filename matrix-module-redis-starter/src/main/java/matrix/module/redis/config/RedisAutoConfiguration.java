package matrix.module.redis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangCheng
 * @date 2020/2/13
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(value = {"redis.enabled"}, matchIfMissing = false)
public class RedisAutoConfiguration {


}
