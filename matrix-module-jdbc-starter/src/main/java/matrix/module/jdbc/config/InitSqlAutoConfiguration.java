package matrix.module.jdbc.config;

import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/14
 */
@Configuration
@EnableConfigurationProperties({JdbcProperties.class})
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@ConditionalOnProperty(value = {"jdbc.enabled", "jdbc.init-sql-enabled"})
public class InitSqlAutoConfiguration {

    @Autowired
    private JdbcProperties jdbcProperties;

    @PostConstruct
    public void init() {
        if (jdbcProperties.getMaster().isEnabled() && !StringUtils.isEmpty(jdbcProperties.getMaster().getInitSqlLocations())) {
            // 脚本位置
            //config.put("flyway.locations", jdbcProperties.getMaster().getFlywayLocations());
            DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.MASTER_DB);
            //executeSql(config, dataSource);
        }
        Map<String, JdbcProperties.JdbcParam> dbList = jdbcProperties.getDbList();
        if (!CollectionUtils.isEmpty(dbList)) {
            for (String key : dbList.keySet()) {
                JdbcProperties.JdbcParam jdbcParam = dbList.get(key);
                if (jdbcParam.isEnabled() && !StringUtils.isEmpty(jdbcParam.getInitSqlLocations())) {
                    //config.put("flyway.locations", jdbcParam.getFlywayLocations());
                    DynamicDataSourceHolder.setDataSource(key + "DB");
                    //executeSql(config, dataSource);
                }
            }
        }
    }

    private void executeSql(Map<String, String> config, DataSource dataSource) {

    }
}
