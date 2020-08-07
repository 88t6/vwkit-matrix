package matrix.module.common.utils;

import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.exception.ServiceException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 字符串工具类
 * author 36509
 */
public class StringUtil {

	public static String encodeStr(String content) {
        try {
            return new String(content.getBytes(BaseCodeConstant.DEFAULT_CHARSET), StandardCharsets.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static String decodeStr(String content) {
        try {
            return new String(content.getBytes(StandardCharsets.ISO_8859_1), BaseCodeConstant.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static String byteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] stringToByte(String content) {
        return content.getBytes(StandardCharsets.UTF_8);
    }

    public static String encodeUrl(String content) {
        try {
            return URLEncoder.encode(content, BaseCodeConstant.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static String decodeUrl(String content) {
        try {
            return URLDecoder.decode(content, BaseCodeConstant.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static boolean isEmpty(String content) {
        return content == null || "".equals(content);
    }

    public static boolean isNotEmpty(String content) {
    	return !isEmpty(content);
    }

}
