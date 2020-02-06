package matrix.module.jpa.config;

import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.jpa.properties.JpaProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ConditionalOnProperty(value = {"jpa.enabled"})
public class JpaAutoConfiguration {
}
