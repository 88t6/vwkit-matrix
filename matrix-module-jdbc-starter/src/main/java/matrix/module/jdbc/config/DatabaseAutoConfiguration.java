package matrix.module.jdbc.config;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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

    @Bean(name = "masterDataSource")
    public DataSource master() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getMaster();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.master.url");
        Assert.isNotNull(username, "jdbc.master.username");
        Assert.isNotNull(password, "jdbc.master.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave1DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave1.enabled"})
    public DataSource slave1() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave1();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave1.url");
        Assert.isNotNull(username, "jdbc.slave1.username");
        Assert.isNotNull(password, "jdbc.slave1.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave2DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave2.enabled"})
    public DataSource slave2() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave2();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave2.url");
        Assert.isNotNull(username, "jdbc.slave2.username");
        Assert.isNotNull(password, "jdbc.slave2.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave3DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave3.enabled"})
    public DataSource slave3() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave3();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave3.url");
        Assert.isNotNull(username, "jdbc.slave3.username");
        Assert.isNotNull(password, "jdbc.slave3.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave4DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave4.enabled"})
    public DataSource slave4() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave4();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave4.url");
        Assert.isNotNull(username, "jdbc.slave4.username");
        Assert.isNotNull(password, "jdbc.slave4.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave5DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave5.enabled"})
    public DataSource slave5() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave5();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave5.url");
        Assert.isNotNull(username, "jdbc.slave5.username");
        Assert.isNotNull(password, "jdbc.slave5.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave6DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave6.enabled"})
    public DataSource slave6() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave6();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave6.url");
        Assert.isNotNull(username, "jdbc.slave6.username");
        Assert.isNotNull(password, "jdbc.slave6.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave7DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave7.enabled"})
    public DataSource slave7() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave7();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave7.url");
        Assert.isNotNull(username, "jdbc.slave7.username");
        Assert.isNotNull(password, "jdbc.slave7.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave8DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave8.enabled"})
    public DataSource slave8() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave8();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave8.url");
        Assert.isNotNull(username, "jdbc.slave8.username");
        Assert.isNotNull(password, "jdbc.slave8.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave9DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave9.enabled"})
    public DataSource slave9() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave9();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave9.url");
        Assert.isNotNull(username, "jdbc.slave9.username");
        Assert.isNotNull(password, "jdbc.slave9.password");
        return this.build(url, username, password);
    }

    @Bean(name = "slave10DataSource")
    @ConditionalOnProperty(value = {"jdbc.slave10.enabled"})
    public DataSource slave10() {
        JdbcProperties.JdbcParam jdbcParam = jdbcProperties.getSlave10();
        String url = jdbcParam.getUrl();
        String username = jdbcParam.getUsername();
        String password = jdbcParam.getPassword();
        Assert.isNotNull(url, "jdbc.slave10.url");
        Assert.isNotNull(username, "jdbc.slave10.username");
        Assert.isNotNull(password, "jdbc.slave10.password");
        return this.build(url, username, password);
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
