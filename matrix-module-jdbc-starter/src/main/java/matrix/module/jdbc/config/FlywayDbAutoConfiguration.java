package matrix.module.jdbc.config;

import matrix.module.jdbc.properties.FlywayDbProperties;
import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/14
 */
@Configuration
@EnableConfigurationProperties({FlywayDbProperties.class, JdbcProperties.class})
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@ConditionalOnProperty(value = {"jdbc.enabled"})
public class FlywayDbAutoConfiguration {

    @PostConstruct
    public void init(FlywayDbProperties flywayDbProperties,
                     JdbcProperties jdbcProperties,
                     DataSource dataSource) {
        Map<String, String> config = new HashMap<>();
        config.put("flyway.enabled", String.valueOf(flywayDbProperties.isEnabled()));
        config.put("flyway.sql-migration-prefix", flywayDbProperties.getSqlMigrationPrefix());
        config.put("flyway.sql-migration-separator", flywayDbProperties.getSqlMigrationSeparator());
        config.put("flyway.sql-migration-suffix", flywayDbProperties.getSqlMigrationSuffix());
        config.put("flyway.table", flywayDbProperties.getTable());
        config.put("flyway.validate-on-migrate", String.valueOf(flywayDbProperties.isValidateOnMigrate()));
        config.put("flyway.out-of-order", String.valueOf(flywayDbProperties.isOutOfOrder()));
        if (jdbcProperties.getMaster().isEnabled() && !StringUtils.isEmpty(jdbcProperties.getMaster().getFlywayLocations())) {
            // 脚本位置
            config.put("flyway.locations", jdbcProperties.getMaster().getFlywayLocations());
            DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.MASTER_DB);
            executeSql(config, dataSource);
        }
        Map<String, JdbcProperties.JdbcParam> dbList = jdbcProperties.getDbList();
        if (!CollectionUtils.isEmpty(dbList)) {
            for (String key : dbList.keySet()) {
                JdbcProperties.JdbcParam jdbcParam = dbList.get(key);
                if (jdbcParam.isEnabled() && !StringUtils.isEmpty(jdbcParam.getFlywayLocations())) {
                    config.put("flyway.locations", jdbcParam.getFlywayLocations());
                    DynamicDataSourceHolder.setDataSource(key + "DB");
                    executeSql(config, dataSource);
                }
            }
        }
    }

    private void executeSql(Map<String, String> config, DataSource dataSource) {
        Flyway flyway = Flyway.configure().configuration(config).dataSource(dataSource).load();


    }
}
