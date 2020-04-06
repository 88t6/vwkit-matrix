package matrix.module.jdbc.config;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSource;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
public class DatabaseAutoConfiguration {

    private final static Logger logger = LogManager.getLogger(DatabaseAutoConfiguration.class);

    private static final String TYPE = "com.alibaba.druid.pool.DruidDataSource";

    public DatabaseAutoConfiguration(JdbcProperties jdbcProperties, ConfigurableBeanFactory beanFactory) {
        if (!jdbcProperties.getMaster().isEnabled()) {
            throw new ServiceException("master db not found!");
        }
        String driverClass = jdbcProperties.getDriverClass();
        Assert.isNotNull(driverClass, "jdbc.driver-class");
        DataSource masterDataSource = build(jdbcProperties.getMaster(), DynamicDataSourceHolder.MASTER_DB, driverClass);
        beanFactory.registerSingleton(DynamicDataSourceHolder.MASTER_DB, masterDataSource);
        if (jdbcProperties.getSlave().isEnabled()) {
            DataSource slaveDataSource = build(jdbcProperties.getSlave(), DynamicDataSourceHolder.SLAVE_DB, driverClass);
            beanFactory.registerSingleton(DynamicDataSourceHolder.SLAVE_DB, slaveDataSource);
        }
        Map<String, JdbcProperties.JdbcParam> dbList = jdbcProperties.getDbList();
        if (!CollectionUtils.isEmpty(dbList)) {
            dbList.forEach((key, jdbcParam) -> {
                if (jdbcParam.isEnabled()) {
                    DataSource db = DatabaseAutoConfiguration.build(jdbcParam, "db-list." + key, driverClass);
                    beanFactory.registerSingleton(key + "DB", db);
                }
            });
        }
    }

    private static DataSource build(JdbcProperties.JdbcParam jdbcParam, String key, String driverClass) {
        String url = jdbcParam.getUrl();
        Assert.isNotNull(url, "jdbc." + key + ".url");
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(username, "jdbc." + key + ".username");
        Assert.isNotNull(password, "jdbc." + key + ".password");
        com.alibaba.druid.pool.DruidDataSource dataSource = new com.alibaba.druid.pool.DruidDataSource();
        dataSource.setDbType(DatabaseAutoConfiguration.TYPE);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        //打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        //通过connectProperties属性来打开mergeSql功能
        dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
        //合并多个DruidDataSource的监控数据
        dataSource.setUseGlobalDataSourceStat(true);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMaxWait(60000);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(10);
        dataSource.setInitialSize(5);
        dataSource.setAsyncInit(true);
        try {
            dataSource.setFilters("stat");
        } catch (Exception e) {
            logger.debug(e);
        }
        return dataSource;
    }

    @Bean("dynamicDataSource")
    @Qualifier("dynamicDataSource")
    @Primary
    public DataSource dynamicDataSource(ConfigurableBeanFactory beanFactory, JdbcProperties jdbcProperties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        DataSource masterDB = beanFactory.getBean(DynamicDataSourceHolder.MASTER_DB, DataSource.class);
        dynamicDataSource.setDefaultTargetDataSource(masterDB);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceHolder.MASTER_DB, masterDB);
        if (jdbcProperties.getSlave().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.SLAVE_DB, beanFactory.getBean(DynamicDataSourceHolder.SLAVE_DB, DataSource.class));
        }
        Map<String, JdbcProperties.JdbcParam> dbList = jdbcProperties.getDbList();
        if (!CollectionUtils.isEmpty(dbList)) {
            dbList.keySet().forEach(key -> {
                JdbcProperties.JdbcParam jdbcParam = dbList.get(key);
                if (jdbcParam.isEnabled()) {
                    targetDataSources.put(key + "DB", beanFactory.getBean(key + "DB", DataSource.class));
                }
            });
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }
}
