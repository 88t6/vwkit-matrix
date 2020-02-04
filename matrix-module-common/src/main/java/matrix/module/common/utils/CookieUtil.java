package matrix.module.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangcheng
 * @addCookie() 加入自定义Cookie
 * @addSimpleCookie() 加入简单Cookie
 * @addSimpleHttpOnlyCookie() 加入简单得HttpOnlyCookie
 * @getCookie() 根据key获取Cookie对象
 * @getCookieValue() 根据key获取Cookie中得Value
 */
public class CookieUtil {

    public static boolean addSimpleCookie(HttpServletResponse response, String key, String value) {
        return CookieUtil.addCookie(response, key, value, -1, "/", null, false, false);
    }

    public static boolean addSimpleHttpOnlyCookie(HttpServletResponse response, String key, String value) {
        return CookieUtil.addCookie(response, key, value, -1, "/", null, false, true);
    }

    public static boolean addCookie(HttpServletResponse response, String key, String value, Integer maxAge, String path, String domain, boolean isSecure, boolean isHttpOnly) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(key).append("=").append(value).append(";");
        if (maxAge == 0) {
            buffer.append("Expires=Thu Jan 01 08:00:00 CST 1970;");
        } else if (maxAge > 0) {
            buffer.append("Max-Age=").append(maxAge).append(";");
        }
        if (domain != null) {
            buffer.append("domain=").append(domain).append(";");
        }
        if (path != null) {
            buffer.append("path=").append(path).append(";");
        }
        if (isSecure) {
            buffer.append("secure;");
        }
        if (isHttpOnly) {
            buffer.append("HTTPOnly;");
        }
        response.addHeader("Set-Cookie", buffer.toString());
        return true;
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
