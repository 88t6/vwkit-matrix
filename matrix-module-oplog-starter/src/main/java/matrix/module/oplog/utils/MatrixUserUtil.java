package matrix.module.oplog.utils;

/**
 * 用户ID存放，日志必须
 *
 * @author wangcheng
 * date 2020-03-14
 */
public class MatrixUserUtil {

    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    public static String getUserId() {
        return USER_ID.get();
    }

    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

}
