package matrix.module.demo.aspect;

import matrix.module.jdbc.utils.DynamicDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@Aspect
public class SwitchDbAspect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Before("execution(* matrix.module.demo..*(..))")
    public void dbSwitchBefore(JoinPoint point) {
        DynamicDataSource.switchDatabase(point.getTarget());
    }
}
