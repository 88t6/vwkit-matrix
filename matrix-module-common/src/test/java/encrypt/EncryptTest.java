package encrypt;

import matrix.module.common.utils.encrypt.Base64;
import matrix.module.common.utils.encrypt.MD5;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class EncryptTest {

    @Test
    public void base64Test() throws UnsupportedEncodingException {
        String content = "123456";
        String base64 = Base64.encryptForString(content);
        System.out.println(base64);
        System.out.println(Base64.decryptForString(base64));
    }

    @Test
    public void md5Test() throws NoSuchAlgorithmException {
        System.out.println(MD5.get32("123456"));
    }

}
