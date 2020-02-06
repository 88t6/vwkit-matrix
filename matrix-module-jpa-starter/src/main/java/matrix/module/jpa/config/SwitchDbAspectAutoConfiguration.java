package matrix.module.jpa.config;

import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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
@Order(1)
public class SwitchDbAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcProperties jdbcProperties;

    @Before("")
    public void dbSwitchBefore(JoinPoint joinPoint) {
        DynamicDataSource.switchDatabase(joinPoint.getTarget(), jdbcProperties);
    }

}
