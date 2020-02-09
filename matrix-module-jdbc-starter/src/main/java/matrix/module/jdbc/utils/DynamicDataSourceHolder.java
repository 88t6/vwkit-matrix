package matrix.module.jdbc.utils;

import com.alibaba.druid.util.StringUtils;

/**
 * @author wangcheng
 */
public class DynamicDataSourceHolder {

    public static final String MASTER_DB = "masterDB";

    public static final String SLAVE_DB = "slaveDB";

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String getDataSource() {
        String key = holder.get();
        if (StringUtils.isEmpty(key)) {
            return MASTER_DB;
        }
        return key;
    }

    public static void clearDataSource() {
        holder.remove();
    }

    public static void setDataSource(String key) {
        holder.set(key);
    }
}
