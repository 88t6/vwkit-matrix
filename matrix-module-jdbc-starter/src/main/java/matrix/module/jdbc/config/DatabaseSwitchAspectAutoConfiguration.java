package matrix.module.jdbc.config;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
public class DatabaseSwitchAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcProperties jdbcProperties;

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.stereotype.Service)")
    public void service(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.stereotype.Component)")
    public void component(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.stereotype.Repository)")
    public void repository(JoinPoint point) {
        process(point);
    }

    private void process(JoinPoint point) {
        Object target = point.getTarget();
        if (jdbcProperties.getDb1().isEnabled()) {
            String basePackage = jdbcProperties.getDb1().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db1.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB1);
                return;
            }
        }
        if (jdbcProperties.getDb2().isEnabled()) {
            String basePackage = jdbcProperties.getDb2().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db2.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB2);
                return;
            }
        }
        if (jdbcProperties.getDb3().isEnabled()) {
            String basePackage = jdbcProperties.getDb3().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db3.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB3);
                return;
            }
        }
        if (jdbcProperties.getDb4().isEnabled()) {
            String basePackage = jdbcProperties.getDb4().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db4.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB4);
                return;
            }
        }
        if (jdbcProperties.getDb5().isEnabled()) {
            String basePackage = jdbcProperties.getDb5().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db5.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB5);
                return;
            }
        }
        if (jdbcProperties.getDb6().isEnabled()) {
            String basePackage = jdbcProperties.getDb6().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db6.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB6);
                return;
            }
        }
        if (jdbcProperties.getDb7().isEnabled()) {
            String basePackage = jdbcProperties.getDb7().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db7.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB7);
                return;
            }
        }
        if (jdbcProperties.getDb8().isEnabled()) {
            String basePackage = jdbcProperties.getDb8().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db8.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB8);
                return;
            }
        }
        if (jdbcProperties.getDb9().isEnabled()) {
            String basePackage = jdbcProperties.getDb9().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db9.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB9);
                return;
            }
        }
        if (jdbcProperties.getDb10().isEnabled()) {
            String basePackage = jdbcProperties.getDb10().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db10.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB10);
            }
        }
    }
}
