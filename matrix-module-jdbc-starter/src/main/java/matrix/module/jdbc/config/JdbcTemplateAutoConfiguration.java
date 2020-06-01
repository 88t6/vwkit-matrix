package matrix.module.jdbc.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author WangCheng
 */
@Configuration
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@ConditionalOnProperty(value = {"jdbc.enabled"})
public class JdbcTemplateAutoConfiguration {

    @Resource
    private DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
