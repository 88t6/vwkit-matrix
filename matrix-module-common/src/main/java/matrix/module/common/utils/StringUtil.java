package matrix.module.common.utils;

import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.exception.ServiceException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 字符串工具类
 * @author 36509
 */
public class StringUtil {

	public static String encodeStr(String content) {
        try {
            return new String(content.getBytes(BaseCodeConstant.DEFAULT_CHARSET), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static String decodeStr(String content) {
        try {
            return new String(content.getBytes("ISO-8859-1"), BaseCodeConstant.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static String byteToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
    }

    public static byte[] stringToByte(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
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
    	if (content != null && !"".equals(content)) {
    		return false;
    	}
    	return true;
    }

    public static boolean isNotEmpty(String content) {
    	return !isEmpty(content);
    }

}
