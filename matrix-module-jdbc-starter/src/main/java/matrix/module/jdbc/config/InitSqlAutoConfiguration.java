package matrix.module.jdbc.config;

import matrix.module.jdbc.properties.JdbcProperties;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import matrix.module.jdbc.utils.InitializeSqlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/14
 */
@Configuration
@EnableConfigurationProperties({JdbcProperties.class})
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@ConditionalOnProperty(value = {"jdbc.enabled", "jdbc.init-sql.enabled"})
@Import(InitializeSqlContext.class)
public class InitSqlAutoConfiguration {

    @Autowired
    private JdbcProperties jdbcProperties;

    @Autowired
    private InitializeSqlContext initializeSqlContext;

    @PostConstruct
    public void init() {
        if (jdbcProperties.getMaster().isEnabled() && !StringUtils.isEmpty(jdbcProperties.getMaster().getInitSqlLocations())) {
            DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.MASTER_DB);
            initializeSqlContext.initialize(jdbcProperties.getInitSql(), jdbcProperties.getMaster().getInitSqlLocations());
        }
        Map<String, JdbcProperties.JdbcParam> dbList = jdbcProperties.getDbList();
        if (!CollectionUtils.isEmpty(dbList)) {
            for (String key : dbList.keySet()) {
                JdbcProperties.JdbcParam jdbcParam = dbList.get(key);
                if (jdbcParam.isEnabled() && !StringUtils.isEmpty(jdbcParam.getInitSqlLocations())) {
                    DynamicDataSourceHolder.setDataSource(key + "DB");
                    initializeSqlContext.initialize(jdbcProperties.getInitSql(), jdbcParam.getInitSqlLocations());
                }
            }
        }
        DynamicDataSourceHolder.clearDataSource();
    }
}
