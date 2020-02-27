package matrix.module.common.helper;

import java.util.regex.Pattern;

/**
 * @author wangcheng
 */
public class Assert {

    /**
     * 判断不为空
     *
     * @param content
     * @param varName
     */
    public static void isNotNull(Object content, String varName) {
        boolean isTrue = content == null || (content.getClass() == String.class && "".equals(content));
        if (isTrue) {
            throw new IllegalArgumentException(varName + "  not be null!");
        }
    }

    /**
     * 判断字符串长度最小值
     *
     * @param content
     * @param length
     * @param varName
     */
    public static void minLength(String content, Integer length, String varName) {
        Assert.isNotNull(content, varName);
        if (content.length() < length) {
            throw new IllegalArgumentException(varName + "长度不允许小于" + length + "!");
        }
    }

    /**
     * 判断字符串长度最大值
     *
     * @param content
     * @param length
     * @param varName
     */
    public static void maxLength(String content, Integer length, String varName) {
        Assert.isNotNull(content, varName);
        if (content.length() > length) {
            throw new IllegalArgumentException(varName + "长度不允许大于" + length + "!");
        }
    }

    /**
     * 判断数字最小值
     *
     * @param content
     * @param varName
     */
    public static void minNum(Integer content, Integer minNum, String varName) {
        Assert.isNotNull(content, varName);
        if (content < minNum) {
            throw new IllegalArgumentException(varName + "数字不允许小于" + minNum + "!");
        }
    }

    /**
     * 判断数字最大值
     *
     * @param content
     * @param varName
     */
    public static void maxNum(Integer content, Integer maxNum, String varName) {
        Assert.isNotNull(content, varName);
        if (content > maxNum) {
            throw new IllegalArgumentException(varName + "数字不允许大于" + maxNum + "!");
        }
    }

    /**
     * 不满足正则
     *
     * @param regex
     * @param content
     * @param msg
     * @param flag
     */
    public static void matchRegex(String regex, String content, String msg, Integer flag) {
        if (flag == null) {
            if (!Pattern.compile(regex).matcher(content).matches()) {
                throw new IllegalArgumentException(msg);
            }
        } else {
            if (!Pattern.compile(regex, flag).matcher(content).matches()) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

    /**
     * 状态不为True
     */
    public static void state(Boolean value, String msg) {
        if (!value) {
            throw new IllegalArgumentException(msg);
        }
    }

}
