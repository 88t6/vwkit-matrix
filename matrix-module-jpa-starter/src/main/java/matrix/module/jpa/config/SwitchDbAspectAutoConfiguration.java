package matrix.module.jpa.config;

import matrix.module.common.exception.GlobalControllerException;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author WangCheng
 * @date 2020/2/10
 */
@Configuration
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@ConditionalOnProperty(value = {"jpa.enabled"})
@Aspect
@Order(1)
public class SwitchDbAspectAutoConfiguration {

    @Around("within(org.springframework.data.repository.CrudRepository) " +
            "|| target(org.springframework.data.repository.CrudRepository)")
    public Object dbSwitchAround(ProceedingJoinPoint joinPoint) {
        try {
            return DynamicDataSourceHolder.processDataSource(joinPoint);
        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        }
    }
}
