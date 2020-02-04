package matrix.module.based.config;

import matrix.module.common.exception.GlobalControllerException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author wangcheng
 */
@Configuration
@Aspect
public class ExceptionAspectAutoConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    @Pointcut("execution(matrix.module.common.bean.Result *(..))")
    public void result() {
    }

    @Around("result()")
    public Object resultAround(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        }
    }
}
