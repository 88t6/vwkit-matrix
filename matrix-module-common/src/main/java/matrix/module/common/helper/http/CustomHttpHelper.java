package matrix.module.common.helper.http;

import matrix.module.common.utils.CustomHttpUtil;
import matrix.module.common.utils.StreamUtil;

import java.io.InputStream;
import java.util.Map;

/**
 * @author wangcheng
 */
public class CustomHttpHelper {

    public static String sendGet(String url, String params, String cookie, String contentType) {
        InputStream inputStream = CustomHttpUtil.send(url, params, cookie, false, contentType);
        return StreamUtil.streamToString(inputStream);
    }

    public static InputStream sendGetForStream(String url, String params, String cookie, String contentType) {
        return CustomHttpUtil.send(url, params, cookie, false, contentType);
    }

    public static String sendPost(String url, String params, String cookie, String contentType) {
        InputStream inputStream = CustomHttpUtil.send(url, params, cookie, true, contentType);
        return StreamUtil.streamToString(inputStream);
    }

    public static InputStream sendPostForStream(String url, String params, String cookie, String contentType) {
        return CustomHttpUtil.send(url, params, cookie, true, contentType);
    }

    public static String sendGet(String url, Map<String, Object> params, String cookie, String contentType) {
        return CustomHttpHelper.sendGet(url, CustomHttpUtil.mapToParams(params, contentType), cookie, contentType);
    }

    public static InputStream sendGetForStream(String url, Map<String, Object> params, String cookie, String contentType) {
        return CustomHttpHelper.sendGetForStream(url, CustomHttpUtil.mapToParams(params, contentType), cookie, contentType);
    }

    public static String sendPost(String url, Map<String, Object> params, String cookie, String contentType) {
        return CustomHttpHelper.sendPost(url, CustomHttpUtil.mapToParams(params, contentType), cookie, contentType);
    }

    public static InputStream sendPostForStream(String url, Map<String, Object> params, String cookie, String contentType) {
        return CustomHttpHelper.sendPostForStream(url, CustomHttpUtil.mapToParams(params, contentType), cookie, contentType);
    }
}
