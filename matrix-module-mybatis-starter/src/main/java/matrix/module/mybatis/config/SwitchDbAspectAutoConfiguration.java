package matrix.module.mybatis.config;

import matrix.module.common.exception.GlobalControllerException;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author WangCheng
 * @date 2020/2/10
 */
@Component
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
@Order(1)
public class SwitchDbAspectAutoConfiguration {

    @Around("within(com.baomidou.mybatisplus.core.mapper.BaseMapper) " +
            "|| target(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public Object dbSwitchAround(ProceedingJoinPoint joinPoint) {
        try {
            return DynamicDataSourceHolder.processDataSource(joinPoint);
        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        }
    }
}
