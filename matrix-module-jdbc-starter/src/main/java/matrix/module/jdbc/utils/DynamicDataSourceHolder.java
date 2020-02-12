package matrix.module.jdbc.utils;

import com.alibaba.druid.util.StringUtils;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.StringUtil;
import matrix.module.jdbc.annotation.TargetDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;

/**
 * @author wangcheng
 */
public class DynamicDataSourceHolder {

    private static Logger logger = LogManager.getLogger(DynamicDataSourceHolder.class);

    public static final String MASTER_DB = "masterDB";

    public static final String SLAVE_DB = "slaveDB";

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String getDataSource() {
        String key = holder.get();
        if (StringUtils.isEmpty(key)) {
            return MASTER_DB;
        }
        return key;
    }

    public static void clearDataSource() {
        holder.remove();
    }

    public static void setDataSource(String key) {
        holder.set(key);
    }

    public static Object processDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        TargetDataSource targetDataSource = joinPoint.getTarget().getClass().getAnnotation(TargetDataSource.class);
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method.getAnnotation(TargetDataSource.class) != null) {
            targetDataSource = method.getAnnotation(TargetDataSource.class);
        }
        if (targetDataSource == null) {
            targetDataSource = joinPoint.getTarget().getClass().getInterfaces()[0].getAnnotation(TargetDataSource.class);
        }
        if (targetDataSource != null && !StringUtil.isEmpty(targetDataSource.value())) {
            DynamicDataSourceHolder.setDataSource(targetDataSource.value() + "DB");
        }
        //是否设置成功
        boolean isSetSuccess = true;
        //设置数据源优先(设置失败代表事务优先需自行执行事务)
        if (!TransactionalHolder.setPriority(TransactionalHolder.DATASOURCE_FLAG) && TransactionalHolder.getTransactionTemplate() != null) {
            TransactionTemplate transactionTemplate = TransactionalHolder.getTransactionTemplate();
            TransactionManager transactionManager = transactionTemplate.getTransactionManager();
            if (transactionManager != null) {
                TransactionStatus transactionStatus = transactionTemplate.getTransactionManager().getTransaction(transactionTemplate);
                TransactionalHolder.setTransactionStatus(transactionStatus);
            } else {
                logger.error("no transaction manager");
            }
            isSetSuccess = false;
        }
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            if (isSetSuccess) {
                DynamicDataSourceHolder.clearDataSource();
                TransactionalHolder.clearTransactionParams();
            }
        }
    }
}
