package matrix.module.common.helper.encrypt;

import matrix.module.common.utils.StringUtil;

/**
 * @author wangcheng
 * encrypt base64加密   bytes传入加密数据的字节
 * decrypt() base64解密  base64code传入需要解密的数据
 */
public class Base64 {

    public static String encrypt(byte[] bytes) {
        return new String(java.util.Base64.getEncoder().encodeToString(bytes));
    }

    public static byte[] decrypt(String base64Code) {
        return base64Code.equals("") ? null : java.util.Base64.getDecoder().decode(base64Code);
    }

    public static String encryptForString(String content) {
        return Base64.encrypt(StringUtil.stringToByte(content));
    }

    public static String decryptForString(String base64Code) {
        return StringUtil.byteToString(Base64.decrypt(base64Code));
    }

}
