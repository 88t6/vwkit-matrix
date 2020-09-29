package matrix.module.common.helper;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.StringUtil;

import java.util.regex.Pattern;

/**
 * @author wangcheng
 */
public class Assert {

    /**
     * 判断不为空（带提示语）
     *
     * @param content 内容
     * @param varName 参数名称
     */
    public static void notNullTip(Object content, String varName) {
        Assert.state(content != null && StringUtil.isNotEmpty(String.valueOf(content)), varName + " not be null!");
    }

    /**
     * 判断不为空
     *
     * @param content 内容
     * @param msg 错误信息
     */
    public static void notNull(Object content, String msg) {
        Assert.state(content != null && StringUtil.isNotEmpty(String.valueOf(content)), msg);
    }

    /**
     * 判断有内容
     *
     * @param content 内容
     * @param msg 错误信息
     */
    public static void hasText(String content, String msg) {
        Assert.state(StringUtil.isNotEmpty(content), msg);
    }

    /**
     * 不满足正则
     *
     * @param regex 正则
     * @param content 内容
     * @param msg 信息
     * @param flag 标记
     */
    public static void matchRegex(String regex, String content, String msg, Integer flag) {
        if (flag == null) {
            Assert.state(Pattern.compile(regex).matcher(content).matches(), msg);
        } else {
            Assert.state(Pattern.compile(regex, flag).matcher(content).matches(), msg);
        }
    }

    /**
     * 状态不为True
     */
    public static void state(Boolean value, String msg) {
        if (!value) {
            throw new ServiceException(msg);
        }
    }

}
