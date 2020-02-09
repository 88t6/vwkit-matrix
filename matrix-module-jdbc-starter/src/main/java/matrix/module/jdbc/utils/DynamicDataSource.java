package matrix.module.jdbc.utils;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author wangcheng
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final static Logger logger = LogManager.getLogger(DynamicDataSource.class);

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Object key = DynamicDataSourceHolder.getDataSource();
        logger.info("==> select datasource key [{}]", key);
        return key;
    }

    /**
     * 切换数据源
     *
     * @param target
     * @param jdbcProperties
     */
    public static void switchDatabase(Object target, JdbcProperties jdbcProperties) {
        if (jdbcProperties.getMaster().isEnabled()) {
            String basePackages = jdbcProperties.getMaster().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.master.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.MASTER_DB);
                return;
            }
        }
        Map<String, JdbcProperties.JdbcParam> dbList = jdbcProperties.getDbList();
        if (!CollectionUtils.isEmpty(dbList)) {
            for (String key : dbList.keySet()) {
                JdbcProperties.JdbcParam jdbcParam = dbList.get(key);
                String basePackages = jdbcParam.getBasePackages();
                Assert.isNotNull(basePackages, "jdbc." + key + ".base-packages");
                if (isHasValue(target.getClass().getName(), basePackages)) {
                    DynamicDataSourceHolder.setDataSource(key + "DB");
                    break;
                }
            }
        }
    }

    private static boolean isHasValue(String className, String basePackages) {
        for (String basePackage : basePackages.split(",")) {
            if (className.startsWith(basePackage)) {
                return true;
            }
        }
        return false;
    }

}
