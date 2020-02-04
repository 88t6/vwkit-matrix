package matrix.module.common.helper.encrypt;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.StringUtil;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author wangcheng
 */
public class DES {

    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = getCipher(password, Cipher.ENCRYPT_MODE);
            return Hex.byte2hex(cipher.doFinal(StringUtil.stringToByte(content)));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public static String decrypt(String content, String password) {
        try {
            Cipher cipher = getCipher(password, Cipher.DECRYPT_MODE);
            return StringUtil.byteToString(cipher.doFinal(Hex.hex2byte(StringUtil.stringToByte(content))));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private static Cipher getCipher(String password, Integer type) {
        try {
            password = password.length() >= 8 ? password : MD5.get16(password);
            DESKeySpec dks = new DESKeySpec(StringUtil.stringToByte(password));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            IvParameterSpec iv = new IvParameterSpec(StringUtil.stringToByte(password.substring(0, 8)));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(type, secretKey, iv);
            return cipher;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
