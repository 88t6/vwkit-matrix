package matrix.module.jdbc.utils;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.properties.JdbcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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
        if (jdbcProperties.getDb1().isEnabled()) {
            String basePackages = jdbcProperties.getDb1().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db1.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB1);
                return;
            }
        }
        if (jdbcProperties.getDb2().isEnabled()) {
            String basePackages = jdbcProperties.getDb2().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db2.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB2);
                return;
            }
        }
        if (jdbcProperties.getDb3().isEnabled()) {
            String basePackages = jdbcProperties.getDb3().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db3.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB3);
                return;
            }
        }
        if (jdbcProperties.getDb4().isEnabled()) {
            String basePackages = jdbcProperties.getDb4().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db4.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB4);
                return;
            }
        }
        if (jdbcProperties.getDb5().isEnabled()) {
            String basePackages = jdbcProperties.getDb5().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db5.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB5);
                return;
            }
        }
        if (jdbcProperties.getDb6().isEnabled()) {
            String basePackages = jdbcProperties.getDb6().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db6.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB6);
                return;
            }
        }
        if (jdbcProperties.getDb7().isEnabled()) {
            String basePackages = jdbcProperties.getDb7().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db7.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB7);
                return;
            }
        }
        if (jdbcProperties.getDb8().isEnabled()) {
            String basePackages = jdbcProperties.getDb8().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db8.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB8);
                return;
            }
        }
        if (jdbcProperties.getDb9().isEnabled()) {
            String basePackages = jdbcProperties.getDb9().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db9.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB9);
                return;
            }
        }
        if (jdbcProperties.getDb10().isEnabled()) {
            String basePackages = jdbcProperties.getDb10().getBasePackages();
            Assert.isNotNull(basePackages, "jdbc.db10.base-packages");
            if (isHasValue(target.getClass().getName(), basePackages)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB10);
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
