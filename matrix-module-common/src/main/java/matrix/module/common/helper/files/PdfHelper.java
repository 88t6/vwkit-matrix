package matrix.module.common.helper.files;

import com.lowagie.text.pdf.BaseFont;
import java.io.File;
import java.io.FileOutputStream;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.common.utils.SysFontUtil;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * pdf工具
 *
 * @author wangcheng
 * @date 2019/3/28
 */
public class PdfHelper {

    private String filePath;

    private String fontPath;

    public PdfHelper(String filePath, String fontPath) {
        this.filePath = filePath;
        this.fontPath = fontPath;
    }

    public static PdfHelper getInstance(String filePath) {
    	return new PdfHelper(filePath, SysFontUtil.getFontPath("宋体"));
    }

    public static PdfHelper getInstance(String filePath, String fontPath) {
        return new PdfHelper(filePath, fontPath);
    }

    public String htmlToPdf(String html) {
        Assert.isNotNull(html, "html");
        Assert.isNotNull(fontPath, "fontPath");
        FileOutputStream fos = null;
        try {
            File file = new File(filePath, RandomUtil.getUUID() + ".pdf");
            fos = new FileOutputStream(file);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(fos);
            return file.getName();
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
        }
    }
}
