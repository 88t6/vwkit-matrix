package matrix.module.jdbc.config;

import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author WangCheng
 */
@Configuration
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
@EnableTransactionManagement
public class TransactionAutoConfiguration {

    @Resource
    private DataSource dataSource;

    @Autowired
    private JdbcProperties jdbcProperties;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource) {
            @Override
            protected void doBegin(Object transaction, TransactionDefinition definition) {
                if (DynamicDataSourceHolder.MASTER_DB.equals(DynamicDataSourceHolder.getDataSource())
                        && jdbcProperties.getSlave().isEnabled() && definition.isReadOnly()) {
                    DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.SLAVE_DB);
                }
                super.doBegin(transaction, definition);
            }
        };
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
