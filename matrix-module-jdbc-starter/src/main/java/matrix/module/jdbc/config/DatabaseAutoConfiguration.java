package matrix.module.jdbc.config;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSource;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
public class DatabaseAutoConfiguration {

    private final static Logger logger = LogManager.getLogger(DatabaseAutoConfiguration.class);

    private static final String TYPE = "com.alibaba.druid.pool.DruidDataSource";

    @Autowired
    private JdbcProperties jdbcProperties;

    @Bean(name = "db1")
    @ConditionalOnProperty(value = {"jdbc.db1.enabled"})
    public DataSource db1() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb1();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db1.url");
        Assert.isNotNull(username, "jdbc.db1.username");
        Assert.isNotNull(password, "jdbc.db1.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db1Slave")
    @ConditionalOnProperty(value = {"jdbc.db1-slave.enabled"})
    public DataSource db1Slave() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb1Slave();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db1-slave.url");
        Assert.isNotNull(username, "jdbc.db1-slave.username");
        Assert.isNotNull(password, "jdbc.db1-slave.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db2")
    @ConditionalOnProperty(value = {"jdbc.db2.enabled"})
    public DataSource db2() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb2();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db2.url");
        Assert.isNotNull(username, "jdbc.db2.username");
        Assert.isNotNull(password, "jdbc.db2.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db3")
    @ConditionalOnProperty(value = {"jdbc.db3.enabled"})
    public DataSource db3() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb3();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db3.url");
        Assert.isNotNull(username, "jdbc.db3.username");
        Assert.isNotNull(password, "jdbc.db3.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db4")
    @ConditionalOnProperty(value = {"jdbc.db4.enabled"})
    public DataSource db4() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb4();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db4.url");
        Assert.isNotNull(username, "jdbc.db4.username");
        Assert.isNotNull(password, "jdbc.db4.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db5")
    @ConditionalOnProperty(value = {"jdbc.db5.enabled"})
    public DataSource db5() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb5();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db5.url");
        Assert.isNotNull(username, "jdbc.db5.username");
        Assert.isNotNull(password, "jdbc.db5.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db6")
    @ConditionalOnProperty(value = {"jdbc.db6.enabled"})
    public DataSource db6() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb6();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db6.url");
        Assert.isNotNull(username, "jdbc.db6.username");
        Assert.isNotNull(password, "jdbc.db6.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db7")
    @ConditionalOnProperty(value = {"jdbc.db7.enabled"})
    public DataSource db7() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb7();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db7.url");
        Assert.isNotNull(username, "jdbc.db7.username");
        Assert.isNotNull(password, "jdbc.db7.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db8")
    @ConditionalOnProperty(value = {"jdbc.db8.enabled"})
    public DataSource db8() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb8();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db8.url");
        Assert.isNotNull(username, "jdbc.db8.username");
        Assert.isNotNull(password, "jdbc.db8.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db9")
    @ConditionalOnProperty(value = {"jdbc.db9.enabled"})
    public DataSource db9() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb9();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db9.url");
        Assert.isNotNull(username, "jdbc.db9.username");
        Assert.isNotNull(password, "jdbc.db9.password");
        return this.build(url, username, password);
    }

    @Bean(name = "db10")
    @ConditionalOnProperty(value = {"jdbc.db10.enabled"})
    public DataSource db10() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getDb10();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.db10.url");
        Assert.isNotNull(username, "jdbc.db10.username");
        Assert.isNotNull(password, "jdbc.db10.password");
        return this.build(url, username, password);
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dynamicDataSource(@Qualifier("db1") DataSource db1,
                                               @Autowired(required = false) @Qualifier("db1Slave") DataSource db1Slave,
                                               @Autowired(required = false) @Qualifier("db2") DataSource db2,
                                               @Autowired(required = false) @Qualifier("db3") DataSource db3,
                                               @Autowired(required = false) @Qualifier("db4") DataSource db4,
                                               @Autowired(required = false) @Qualifier("db5") DataSource db5,
                                               @Autowired(required = false) @Qualifier("db6") DataSource db6,
                                               @Autowired(required = false) @Qualifier("db7") DataSource db7,
                                               @Autowired(required = false) @Qualifier("db8") DataSource db8,
                                               @Autowired(required = false) @Qualifier("db9") DataSource db9,
                                               @Autowired(required = false) @Qualifier("db10") DataSource db10) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceHolder.DB1, db1);
        if (jdbcProperties.getDb1Slave().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB1_SLAVE, db1Slave);
        }
        if (jdbcProperties.getDb2().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB2, db2);
        }
        if (jdbcProperties.getDb3().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB3, db3);
        }
        if (jdbcProperties.getDb4().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB4, db4);
        }
        if (jdbcProperties.getDb5().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB5, db5);
        }
        if (jdbcProperties.getDb6().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB6, db6);
        }
        if (jdbcProperties.getDb7().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB7, db7);
        }
        if (jdbcProperties.getDb8().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB8, db8);
        }
        if (jdbcProperties.getDb9().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB9, db9);
        }
        if (jdbcProperties.getDb10().isEnabled()) {
            targetDataSources.put(DynamicDataSourceHolder.DB10, db10);
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(db1);
        return dynamicDataSource;
    }

    private DataSource build(String url, String userName, String password) {
        String driverClass = jdbcProperties.getDriverClass();
        Assert.isNotNull(driverClass, "jdbc.driver-class");
        com.alibaba.druid.pool.DruidDataSource dataSource = new com.alibaba.druid.pool.DruidDataSource();
        dataSource.setDbType(DatabaseAutoConfiguration.TYPE);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
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
        dataSource.setMinIdle(5);
        dataSource.setInitialSize(5);
        try {
            dataSource.setFilters("stat");
        } catch (Exception e) {
            logger.debug(e);
        }
        return dataSource;
    }
}
