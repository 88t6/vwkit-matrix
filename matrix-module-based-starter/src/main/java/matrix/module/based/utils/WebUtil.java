package matrix.module.based.utils;

import matrix.module.common.utils.CookieUtil;
import matrix.module.common.utils.RandomUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用的容器工具
 *
 * @author 36509
 */
@Component
public class WebUtil implements ApplicationContextAware {

    /**
     * 用户唯一标识Key
     */
    public static final String LOGINKEY = "UserTokenID";
    private static ApplicationContext applicationContext;

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebUtil.applicationContext = applicationContext;
    }

    /**
     * 根据class获取bean
     *
     * @param clazz 类
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据beanName获取bean
     *
     * @param name 参数
     * @return Object
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 根据beanName和requiredType获取bean
     *
     * @param name 参数
     * @param requiredType 参数
     * @return T
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 根据beanName查找bean是否存在
     *
     * @param name 参数
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 根据beanName判断是否是单例
     *
     * @param name 参数
     * @return boolean
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 根据beanName获取类型
     *
     * @param name 参数
     * @return Class
     */
    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 根据beanName获取别名
     *
     * @param name 参数
     * @return String
     */
    public static String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }

    /**
     * 获取容器context
     *
     * @param request 参数
     * @return WebApplicationContext
     */
    public static WebApplicationContext getContext(HttpServletRequest request) {
        return WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
    }

    /**
     * 获取用户唯一标识
     *
     * @param request 参数
     * @param response 参数
     * @return String
     */
    public static String getUserTokenId(HttpServletRequest request, HttpServletResponse response) {
        String tokenId = CookieUtil.getCookieValue(request, WebUtil.LOGINKEY);
        if (tokenId != null && !"".equals(tokenId)) {
            return tokenId;
        } else {
            tokenId = RandomUtil.getUUID();
            if (CookieUtil.addSimpleHttpOnlyCookie(response, WebUtil.LOGINKEY, tokenId))
                return tokenId;
        }
        return null;
    }
}
