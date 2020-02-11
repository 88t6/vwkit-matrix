package matrix.module.jpa.config;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import matrix.module.jpa.properties.JpaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionDefinition;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Configuration
@EnableConfigurationProperties({JdbcProperties.class, JpaProperties.class})
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ConditionalOnProperty(value = {"jpa.enabled"})
@EnableJpaRepositories(basePackages = {"${jpa.base-package}"}, transactionManagerRef = "jpaTransactionManager")
public class JpaAutoConfiguration {

    @Autowired
    private JdbcProperties jdbcProperties;

    @Autowired
    private JpaProperties jpaProperties;

    @Resource
    private DataSource dataSource;

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory() {
        String basePackage = jpaProperties.getBasePackage();
        Assert.isNotNull(basePackage, "jpa.base-package");
        boolean showSql = jpaProperties.isShowSql();
        Assert.isNotNull(showSql, "jpa.show-sql");
        String dialect = jpaProperties.getDialect();
        Assert.isNotNull(dialect, "jpa.dialect");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(basePackage);
        factory.setDataSource(dataSource);
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.show_sql", String.valueOf(showSql));
        jpaProperties.put("hibernate.dialect", dialect);
        jpaProperties.put("hibernate.ejb.naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        jpaProperties.put("hibernate.jdbc.batch_size", 50);
        //jpaProperties.put("hibernate.transaction.auto_close_session", "true");
        //jpaProperties.put("hibernate.connection.autocommit", "true");
        //jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        factory.setJpaPropertyMap(jpaProperties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    @Primary
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf) {
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
}
