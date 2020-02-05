package matrix.module.jdbc.utils;

import matrix.module.based.utils.WebUtil;
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
        logger.debug("==> select datasource key [{}]", key);
        return key;
    }

    /**
     * 切换数据源
     *
     * @param target
     */
    public static void switchDatabase(Object target) {
        JdbcProperties jdbcProperties = WebUtil.getBean(JdbcProperties.class);
        if (jdbcProperties.getDb1().isEnabled()) {
            String basePackage = jdbcProperties.getDb1().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db1.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB1);
                return;
            }
        }
        if (jdbcProperties.getDb2().isEnabled()) {
            String basePackage = jdbcProperties.getDb2().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db2.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB2);
                return;
            }
        }
        if (jdbcProperties.getDb3().isEnabled()) {
            String basePackage = jdbcProperties.getDb3().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db3.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB3);
                return;
            }
        }
        if (jdbcProperties.getDb4().isEnabled()) {
            String basePackage = jdbcProperties.getDb4().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db4.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB4);
                return;
            }
        }
        if (jdbcProperties.getDb5().isEnabled()) {
            String basePackage = jdbcProperties.getDb5().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db5.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB5);
                return;
            }
        }
        if (jdbcProperties.getDb6().isEnabled()) {
            String basePackage = jdbcProperties.getDb6().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db6.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB6);
                return;
            }
        }
        if (jdbcProperties.getDb7().isEnabled()) {
            String basePackage = jdbcProperties.getDb7().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db7.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB7);
                return;
            }
        }
        if (jdbcProperties.getDb8().isEnabled()) {
            String basePackage = jdbcProperties.getDb8().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db8.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB8);
                return;
            }
        }
        if (jdbcProperties.getDb9().isEnabled()) {
            String basePackage = jdbcProperties.getDb9().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db9.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB9);
                return;
            }
        }
        if (jdbcProperties.getDb10().isEnabled()) {
            String basePackage = jdbcProperties.getDb10().getBasePackage();
            Assert.isNotNull(basePackage, "jdbc.db10.base-package");
            if (target.getClass().getName().startsWith(basePackage)) {
                DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB10);
            }
        }
    }

}
