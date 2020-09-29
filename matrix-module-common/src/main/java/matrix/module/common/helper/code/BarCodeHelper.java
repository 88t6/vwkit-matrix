package matrix.module.common.helper.code;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.ImageUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author wangcheng
 */
public class BarCodeHelper {

    private static final String FORMAT = "png";

    private int width;

    private int height;

    private String filePath;

    public BarCodeHelper(int width, int height, String filePath) {
        this.width = width;
        this.height = height;
        this.filePath = filePath;
    }

    public static BarCodeHelper getInstance(int width, int height, String filePath) {
        return new BarCodeHelper(width, height, filePath);
    }

    public BufferedImage getBufferedImage(String content) {
        int codeWidth = Math.max(3 + (7 * 6) + 5 + (7 * 6) + 3, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, codeWidth, height, null);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void writeToStream(String content, OutputStream stream) {
        int codeWidth = Math.max(3 + (7 * 6) + 5 + (7 * 6) + 3, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, codeWidth, height, null);
            MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, stream);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public String getImageBase64(String content) {
    	BufferedImage image = getBufferedImage(content);
    	return ImageUtil.getImageBase64(image);
    }

    public String writeToFile(String content) {
        Assert.notNullTip(this.filePath, "BarCodeHelper:filePath");
        FileOutputStream fos = null;
        try {
            BufferedImage bufferedImage = this.getBufferedImage(content);
            File file = new File(this.filePath, RandomUtil.getUUID() + "." + "png");
            fos = new FileOutputStream(file);
            ImageIO.write(bufferedImage, FORMAT, fos);
            return file.getName();
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
        }
    }

    public String parseFile(String fileName) {
        Assert.notNullTip(this.filePath, "BarCodeHelper:filePath");
        Assert.notNullTip(fileName, "BarCodeHelper:fileName");
        try {
            BufferedImage image = ImageIO.read(new File(filePath, fileName));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
