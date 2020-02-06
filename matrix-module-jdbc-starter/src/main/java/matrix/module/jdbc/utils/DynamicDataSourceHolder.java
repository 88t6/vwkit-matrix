package matrix.module.jdbc.utils;

import com.alibaba.druid.util.StringUtils;

/**
 * @author wangcheng
 */
public class DynamicDataSourceHolder {

    public static final String DB1 = "db1";

    public static final String DB1_SLAVE = "db1-slave";

    public static final String DB2 = "db2";

    public static final String DB3 = "db3";

    public static final String DB4 = "db4";

    public static final String DB5 = "db5";

    public static final String DB6 = "db6";

    public static final String DB7 = "db7";

    public static final String DB8 = "db8";

    public static final String DB9 = "db9";

    public static final String DB10 = "db10";

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String getDataSource() {
        String key = holder.get();
        if (StringUtils.isEmpty(key)) {
            return DB1;
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
