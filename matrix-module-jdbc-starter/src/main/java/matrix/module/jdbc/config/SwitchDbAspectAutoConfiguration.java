package matrix.module.jdbc.config;

import matrix.module.common.exception.GlobalControllerException;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
@Order(1)
public class SwitchDbAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Around("@within(matrix.module.jdbc.annotation.TargetDataSource) " +
            "|| @annotation(matrix.module.jdbc.annotation.TargetDataSource)")
    public Object dbSwitchAround(ProceedingJoinPoint joinPoint) {
        try {
            return DynamicDataSourceHolder.processDataSource(joinPoint);
        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        }
    }

}
