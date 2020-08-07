package matrix.module.common.helper.encrypt;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * @author wangcheng
 * encrypt() AES加密 content:加密的内容  password:加密的密码
 * decrypt() AES解密 content:解密的内容  password:解密的密码
 */
public class AES {

    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = getCipher(password, Cipher.ENCRYPT_MODE);
            return Base64.encrypt(cipher.doFinal(StringUtil.stringToByte(content)));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public static String decrypt(String content, String password) {
        try {
            Cipher cipher = getCipher(password, Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Objects.requireNonNull(Base64.decrypt(content))));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private static Cipher getCipher(String password, Integer type) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(StringUtil.stringToByte(password)));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(type, secretKeySpec);
            return cipher;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
