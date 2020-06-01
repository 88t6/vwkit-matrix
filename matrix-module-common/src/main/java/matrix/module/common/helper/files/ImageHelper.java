package matrix.module.common.helper.files;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.FileUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.SingletonUtil;
import matrix.module.common.utils.StreamUtil;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

/**
 * @author wangcheng
 */
public class ImageHelper {

    private String filePath;

    public ImageHelper(String filePath) {
        this.filePath = filePath;
    }

    public static ImageHelper getInstance(String filePath) {
        return SingletonUtil.get(ImageHelper.class, () -> new ImageHelper(filePath));
    }

    /**
     * 获取图片
     *
     * @param fileName 参数
     * @return BufferedImage
     */
    public BufferedImage getBufferedImage(String fileName) {
        Assert.isNotNull(fileName, "fileName");
        try {
            return ImageIO.read(new File(filePath, fileName));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 裁剪图片
     *
     * @param fileName 参数
     * @param x 参数
     * @param y 参数
     * @param width 参数
     * @param height 参数
     * @return String
     */
    public String cutImage(String fileName, int x, int y, int width, int height) {
        FileInputStream fis = null;
        ImageInputStream iis = null;
        try {
            String suffix = FileUtil.getSuffix(fileName);
            String type = suffix.replace(".", "");
            String result = RandomUtil.getUUID() + suffix;
            fis = new FileInputStream(new File(filePath, fileName));
            Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(type);
            ImageReader reader = iterator.next();
            iis = ImageIO.createImageInputStream(fis);
            reader.setInput(iis, true);
            ImageReadParam imageReadParam = reader.getDefaultReadParam();
            imageReadParam.setSourceRegion(new Rectangle(x, y, width, height));
            BufferedImage bi = reader.read(0, imageReadParam);
            ImageIO.write(bi, type, new File(filePath, result));
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(iis);
            StreamUtil.closeStream(fis);
        }
    }

    /**
     * 缩放图片
     *
     * @param fileName 参数
     * @param width 参数
     * @param height 参数
     * @return String
     */
    public String zoomImage(String fileName, Integer width, Integer height) {
        Assert.isNotNull(fileName, "fileName");
        Assert.state(width != null || height != null, "width和height不允许同时为空");
        BufferedImage bufferedImage = this.getBufferedImage(fileName);
        if (width == null) {
            width = getAdaptiveWidth(bufferedImage, height);
        }
        if (height == null) {
            height = getAdaptiveHeight(bufferedImage, width);
        }
        FileOutputStream fos = null;
        try {
            String suffix = FileUtil.getSuffix(fileName);
            String type = suffix.replace(".", "");
            String result = RandomUtil.getUUID() + suffix;
            fos = new FileOutputStream(new File(filePath, result));
            double widthRate = 0, heightRate = 0;
            widthRate = width * 1.0 / bufferedImage.getWidth();
            heightRate = height * 1.0 / bufferedImage.getHeight();
            AffineTransformOp transformOp = new AffineTransformOp(AffineTransform.getScaleInstance(widthRate, heightRate), null);
            BufferedImage image = transformOp.filter(bufferedImage, null);
            ImageIO.write(image, type, fos);
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
        }
    }

    /**
     * 获取自适应宽度
     */
    private Integer getAdaptiveWidth(BufferedImage image, Integer height) {
        return Double.valueOf(height * 1.0 / image.getHeight() * image.getWidth()).intValue();
    }

    /**
     * 获取自适应高度
     */
    private Integer getAdaptiveHeight(BufferedImage image, Integer width) {
        return Double.valueOf(width * 1.0 / image.getWidth() * image.getHeight()).intValue();
    }
}
