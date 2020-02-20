package matrix.module.pay.config;

import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.pay.properties.PayProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Configuration
@EnableConfigurationProperties(PayProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ConditionalOnProperty(value = "{pay.alipay.enabled}")
public class AlipayConfiguration {

}
