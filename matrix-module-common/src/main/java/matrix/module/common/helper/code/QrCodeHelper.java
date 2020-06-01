package matrix.module.common.helper.code;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import matrix.module.common.constant.BaseCodeConstant;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成
 *
 * @author: wangcheng
 */
public class QrCodeHelper {

    private static final Map<EncodeHintType, Object> hints = new HashMap<>();

    private static final String FORMAT = "png";

    private Integer width;

    private Integer height;

    private String filePath;

    static {
        hints.put(EncodeHintType.CHARACTER_SET, BaseCodeConstant.DEFAULT_CHARSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0);
    }

    public QrCodeHelper(int width, int height, String filePath) {
        this.width = width;
        this.height = height;
        this.filePath = filePath;
    }

    public static QrCodeHelper getInstance(int width, int height, String filePath) {
        return new QrCodeHelper(width, height, filePath);
    }

    /**
     * 返回一个 BufferedImage 对象
     *
     * @param content 二维码内容
     */
    public BufferedImage getBufferedImage(String content) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 将二维码图片输出到一个流中
     *
     * @param content 二维码内容
     * @param stream  输出流
     */
    public void writeToStream(String content, OutputStream stream) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, stream);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public String getImageBase64(String content) {
    	BufferedImage image = getBufferedImage(content);
    	return ImageUtil.getImageBase64(image);
    }

    /**
     * 将二维码输出到文件返回文件名
     */
    public String writeToFile(String content) {
        Assert.isNotNull(filePath, "QrCodeHelper:filePath");
        FileOutputStream fos = null;
        try {
            BufferedImage bufferedImage = getBufferedImage(content);
            String fileName = RandomUtil.getUUID() + "." + FORMAT;
            fos = new FileOutputStream(new File(filePath, fileName));
            ImageIO.write(bufferedImage, FORMAT, fos);
            return fileName;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
        }
    }

    /**
     * 解析QrCode
     * @param fileName 参数
     * @return String
     */
    public String parseFile(String fileName) {
        Assert.isNotNull(this.filePath, "QrCodeHelper:filePath");
        Assert.isNotNull(fileName, "QrCodeHelper:fileName");
        try {
            BufferedImage image = ImageIO.read(new File(filePath, fileName));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>(1);
            hints.put(DecodeHintType.CHARACTER_SET, BaseCodeConstant.DEFAULT_CHARSET);
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return result.toString();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
