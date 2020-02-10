package matrix.module.jpa.config;

import matrix.module.common.exception.GlobalControllerException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.persistence.EntityManager;

/**
 * @author WangCheng
 * @date 2020/2/9
 */
@Configuration
@AutoConfigureAfter(JpaAutoConfiguration.class)
@ConditionalOnProperty(value = {"jpa.enabled"})
@Aspect
@Order(3)
public class JpaAspectAutoConfiguration {

    @Autowired
    private EntityManager entityManager;

    @Around("target(org.springframework.data.repository.CrudRepository)")
    public Object before(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        } finally {
            try {
                entityManager.unwrap(SessionImplementor.class).disconnect();
            } catch (Exception e) {
                System.out.println(123);
            }

        }

    }
}
