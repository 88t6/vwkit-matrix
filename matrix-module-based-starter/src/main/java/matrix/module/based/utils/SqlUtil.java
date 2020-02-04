package matrix.module.based.utils;

import java.util.Collection;

/**
 * @author wangcheng
 */
public class SqlUtil {
    /**
     * SQL占位符?号生成
     */
    public static String getSQLPlace(Integer count) {
        if (count != null && count > 0) {
            String result = "";
            for (int i = 0; i < count; i++) {
                result += "?,";
            }
            return result.substring(0, result.length() - 1);
        }
        return "";
    }

    /**
     * SQL打印
     */
    public static String getSQL(String sql, Object[] args) {
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                sql = sql.replace("?", "'" + arg.toString() + "'");
            }
        }
        return sql;
    }

    public static String getSQL(String sql, Collection<Object> args) {
        if (args != null && args.size() > 0) {
            for (Object arg : args) {
                sql = sql.replace("?", "'" + arg.toString() + "'");
            }
        }
        return sql;
    }
}
