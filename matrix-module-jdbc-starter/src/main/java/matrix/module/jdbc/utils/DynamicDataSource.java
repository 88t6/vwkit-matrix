package matrix.module.jdbc.utils;

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

}
