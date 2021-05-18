package matrix.module.common.utils.encrypt;

import matrix.module.common.exception.ServiceException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangcheng
 */
public class MD5 {

    public static String get32(String content) {
        StringBuilder str = new StringBuilder();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
        md.update(content.getBytes());
        byte[] b = md.digest();
        int i;
        for (byte value : b) {
            i = value;
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                str.append("0");
            }
            str.append(Integer.toHexString(i));
        }
        return str.toString().toUpperCase();
    }

    public static String get16(String content) {
        String md5 = MD5.get32(content);
        return md5.substring(8, 24);
    }
}
