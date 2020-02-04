package matrix.module.common.bean;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author wangcheng
 * @content 生成的验证码内容
 * @image 验证码的图片
 */
public class VerifyCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;

    private BufferedImage image;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public VerifyCode(String content, BufferedImage image) {
        super();
        this.content = content;
        this.image = image;
    }
}
