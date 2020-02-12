package matrix.module.jdbc.config;

import matrix.module.common.exception.GlobalControllerException;
import matrix.module.common.exception.ServiceException;
import matrix.module.jdbc.utils.TransactionalHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
@Order(2)
public class TransactionAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LogManager.getLogger(TransactionAspectAutoConfiguration.class);

    @Resource
    private TransactionTemplate transactionTemplate;

    @Around("@within(org.springframework.transaction.annotation.Transactional) " +
            "|| @annotation(org.springframework.transaction.annotation.Transactional)")
    public Object transactionalAround(ProceedingJoinPoint joinPoint) {
        try {
            Transactional transactional = joinPoint.getTarget().getClass().getAnnotation(Transactional.class);
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            if (method.getAnnotation(Transactional.class) != null) {
                transactional = method.getAnnotation(Transactional.class);
            }
            transactionTemplate.setPropagationBehavior(transactional.propagation().value());
            transactionTemplate.setIsolationLevel(transactional.isolation().value());
            transactionTemplate.setTimeout(transactional.timeout());
            transactionTemplate.setReadOnly(transactional.readOnly());
            PlatformTransactionManager platformTransactionManager = transactionTemplate.getTransactionManager();
            if (platformTransactionManager == null) {
                logger.error("platformTransactionManager not found!");
                return joinPoint.proceed(joinPoint.getArgs());
            }
            //设置事务优先(设置失败则代表数据源已优先切换)
            if (!TransactionalHolder.setPriority(TransactionalHolder.TRANSACTIONAL_FLAG)) {
                return transactionTemplate.execute(transactionStatus -> {
                    try {
                        return joinPoint.proceed(joinPoint.getArgs());
                    } catch (Throwable e) {
                        throw new ServiceException(e);
                    }
                });
            } else {
                TransactionalHolder.setTransactionTemplate(transactionTemplate);
                return joinPoint.proceed(joinPoint.getArgs());
            }

        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        }
    }
}
