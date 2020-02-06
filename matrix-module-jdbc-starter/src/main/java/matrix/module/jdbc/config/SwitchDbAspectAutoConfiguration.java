package matrix.module.jdbc.config;

import matrix.module.common.exception.GlobalControllerException;
import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
public class SwitchDbAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcProperties jdbcProperties;

    @Pointcut("!execution(* org..*.*(..)) " +
            "&& !execution(* java..*.*(..)) " +
            "&& !execution(* sun..*.*(..)) " +
            "&& !execution(* com.sun.jmx..*.*(..)) " +
            "&& !execution(* com.fasterxml..*.*(..)) " +
            "&& !execution(* matrix.module.jdbc..*.*(..)) ")
    public void dbSwitchPointCut() {
    }

    @Around("dbSwitchPointCut()")
    public Object dbSwitchAround(ProceedingJoinPoint joinPoint) {
        try {
            boolean isSwitch = switchDatabase(joinPoint.getTarget());
            Object result = joinPoint.proceed(joinPoint.getArgs());
            if (isSwitch) {
                DynamicDataSourceHolder.clearDataSource();
            }
            return result;
        } catch (Throwable e) {
            throw new GlobalControllerException(e);
        }
    }

    /**
     * 切换数据源
     *
     * @param target
     */
    public boolean switchDatabase(Object target) {
        if (jdbcProperties == null) {
            return false;
        }
        if (jdbcProperties.getDb1().isEnabled()) {
            String basePackages = jdbcProperties.getDb1().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db1.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB1);
                return true;
            }
        }
        if (jdbcProperties.getDb2().isEnabled()) {
            String basePackages = jdbcProperties.getDb2().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db2.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB2);
                return true;
            }
        }
        if (jdbcProperties.getDb3().isEnabled()) {
            String basePackages = jdbcProperties.getDb3().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db3.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB3);
                return true;
            }
        }
        if (jdbcProperties.getDb4().isEnabled()) {
            String basePackages = jdbcProperties.getDb4().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db4.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB4);
                return true;
            }
        }
        if (jdbcProperties.getDb5().isEnabled()) {
            String basePackages = jdbcProperties.getDb5().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db5.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB5);
                return true;
            }
        }
        if (jdbcProperties.getDb6().isEnabled()) {
            String basePackages = jdbcProperties.getDb6().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db6.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB6);
                return true;
            }
        }
        if (jdbcProperties.getDb7().isEnabled()) {
            String basePackages = jdbcProperties.getDb7().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db7.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB7);
                return true;
            }
        }
        if (jdbcProperties.getDb8().isEnabled()) {
            String basePackages = jdbcProperties.getDb8().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db8.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB8);
                return true;
            }
        }
        if (jdbcProperties.getDb9().isEnabled()) {
            String basePackages = jdbcProperties.getDb9().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db9.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB9);
                return true;
            }
        }
        if (jdbcProperties.getDb10().isEnabled()) {
            String basePackages = jdbcProperties.getDb10().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db10.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB10);
                return true;
            }
        }
        return false;
    }

    private boolean isHasValue(String className, String basePackages) {
        for (String basePackage : basePackages.split(",")) {
            if (className.startsWith(basePackage)) {
                return true;
            }
        }
        return false;
    }
}
