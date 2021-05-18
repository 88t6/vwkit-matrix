package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.encrypt.Base64;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class ImageUtil {

	public static String getImageBase64(BufferedImage image) {
		byte[] bytes = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", byteArrayOutputStream);
			bytes = byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			StreamUtil.closeStream(byteArrayOutputStream);
		}
		return "data:image/png;base64," + Base64.encrypt(bytes);
	}

}
