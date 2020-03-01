package matrix.module.jdbc.utils;

import com.alibaba.druid.util.StringUtils;
import matrix.module.based.utils.WebUtil;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.StringUtil;
import matrix.module.jdbc.annotation.TargetDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;

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
            String dbName = "";
            if (targetDataSource.value().startsWith("$")) {
                Environment environment = WebUtil.getBean(Environment.class);
                dbName = environment.getProperty(targetDataSource.value().replace("${", "").replace("}", ""));
                if (StringUtils.isEmpty(dbName)) {
                    dbName = "master";
                }
            } else {
                dbName = targetDataSource.value();
            }
            DynamicDataSourceHolder.setDataSource(dbName + "DB");
        }
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            DynamicDataSourceHolder.clearDataSource();
        }
    }
}
