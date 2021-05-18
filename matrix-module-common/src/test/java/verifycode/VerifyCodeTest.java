package verifycode;

import matrix.module.common.bean.VerifyCode;
import matrix.module.common.helper.VerifyCodeHelper;
import org.junit.Test;

/**
 * @author wangcheng
 */
public class VerifyCodeTest {

    @Test
    public void testNumber() {
        VerifyCodeHelper tool = VerifyCodeHelper.getInstance(10);
        VerifyCode bean = tool.getNumberCode(200, 100, 5);
        System.out.println(bean.getContent());
        System.out.println(bean.getImage());
    }

    @Test
    public void testEnglish() {
        VerifyCodeHelper tool = VerifyCodeHelper.getInstance(10);
        VerifyCode bean = tool.getEnglishCode(200, 100, 5);
        System.out.println(bean.getContent());
        System.out.println(bean.getImage());
    }

    @Test
    public void testNumberAndEnglishCode() {
        VerifyCodeHelper tool = VerifyCodeHelper.getInstance(10);
        VerifyCode bean = tool.getNumberAndEnglishCode(200, 100, 5);
        System.out.println(bean.getContent());
        System.out.println(bean.getImage());
    }

    @Test
    public void testChineseCode() {
        VerifyCodeHelper tool = VerifyCodeHelper.getInstance(10);
        VerifyCode bean = tool.getChineseCode(200, 100, 5);
        System.out.println(bean.getContent());
        System.out.println(bean.getImage());
    }

}
