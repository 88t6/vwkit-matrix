package pdf;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.helper.files.PdfHelper;
import matrix.module.common.utils.SysFontUtil;
import org.junit.Test;

public class PdfTest {

    @Test
    public void testFontDict() {
        System.out.println(JSONObject.toJSONString(SysFontUtil.getSystemFontsDict()));
        System.out.println(SysFontUtil.getFontPath("宋体"));
    }

    @Test
    public void testPdf() {
        PdfHelper pdfHelper = PdfHelper.getInstance("D:\\");
        String html = "<html><head>" +
                "</head><body style='width:700px;text-align:center;'>" +
                "<a>asdfasfsadf</a><br /><a>kkasdkaskdasd</a>" +
                "</body></html>";
        System.out.println(pdfHelper.htmlToPdf(html));
    }
}
