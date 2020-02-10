package matrix.module.jdbc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@AutoConfigureAfter(TransactionAutoConfiguration.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
@Order(2)
public class TransactionAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LogManager.getLogger(TransactionAspectAutoConfiguration.class);

    @Resource
    private TransactionTemplate transactionTemplate;

//    @Around("@within(matrix.module.jdbc.annotation.DynamicTransactional) " +
//            "|| @annotation(matrix.module.jdbc.annotation.DynamicTransactional)")
//    public Object around(ProceedingJoinPoint joinPoint) {
//        try {
//            DynamicTransactional dynamicTransactional = joinPoint.getTarget().getClass().getAnnotation(DynamicTransactional.class);
//            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//            if (method.getAnnotation(DynamicTransactional.class) != null) {
//                dynamicTransactional = method.getAnnotation(DynamicTransactional.class);
//            }
//            transactionTemplate.setPropagationBehavior(dynamicTransactional.propagation().value());
//            transactionTemplate.setIsolationLevel(dynamicTransactional.isolation().value());
//            transactionTemplate.setTimeout(dynamicTransactional.timeout());
//            transactionTemplate.setReadOnly(dynamicTransactional.readOnly());
//            PlatformTransactionManager platformTransactionManager = transactionTemplate.getTransactionManager();
//            if (platformTransactionManager == null) {
//                logger.error("platformTransactionManager not found!");
//                return joinPoint.proceed(joinPoint.getArgs());
//            }
//            return transactionTemplate.execute(transactionStatus -> {
//                try {
//                    return joinPoint.proceed(joinPoint.getArgs());
//                } catch (Throwable e) {
//                    throw new ServiceException(e);
//                }
//            });
//        } catch (Throwable e) {
//            throw new GlobalControllerException(e);
//        }
//    }
}
