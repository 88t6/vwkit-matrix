package matrix.module.common.helper.code;

import matrix.module.common.bean.VerifyCode;
import matrix.module.common.utils.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author wangcheng
 */
public class VerifyCodeHelper {

    private BufferedImage image = null;

    private Integer lineCount = 0;

    private Random random = new Random();

    /**
     * @param lineCount 模糊线数
     * @return VerifyCodeHelper
     */
    public static VerifyCodeHelper getInstance(Integer lineCount) {
        return new VerifyCodeHelper(lineCount);
    }

    /**
     * @param lineCount 模糊线数
     */
    public VerifyCodeHelper(Integer lineCount) {
        this.lineCount = lineCount;
    }

    public VerifyCode getNumberCode(int width, int height, int num) {
        Graphics2D g = this.initComplement(width, height);
        String rand = RandomUtil.getNumCode(num);
        this.drawLine(g, height, width, lineCount);
        this.drawString(g, rand, height, width);
        return new VerifyCode(rand, image);
    }

    public VerifyCode getEnglishCode(int width, int height, int num) {
        Graphics2D g = this.initComplement(width, height);
        String rand = RandomUtil.getEnCode(num);
        this.drawLine(g, height, width, lineCount);
        this.drawString(g, rand, height, width);
        return new VerifyCode(rand, image);
    }

    public VerifyCode getNumberAndEnglishCode(int width, int height, int num) {
        Graphics2D g = this.initComplement(width, height);
        String rand = RandomUtil.getMixCode(num);
        this.drawLine(g, height, width, lineCount);
        this.drawString(g, rand, height, width);
        return new VerifyCode(rand, image);
    }

    public VerifyCode getChineseCode(int width, int height, int num) {
        Graphics2D g = this.initComplement(width, height);
        String rand = RandomUtil.getChCode(num);
        this.drawLine(g, height, width, lineCount);
        this.drawString(g, rand, height, width);
        return new VerifyCode(rand, image);
    }

    private Graphics2D initComplement(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        return g;
    }

    private void drawString(Graphics2D g, String rand, int height, int width) {
        String[] randArr = rand.split("");
        for (int i = 0; i < randArr.length; i++) {
            double rotatePercent = Math.PI * (random.nextInt(3) / 180D);
            if (random.nextBoolean()) {
                rotatePercent = -rotatePercent;
            }
            g.rotate(rotatePercent);
            g.setColor(this.getRandColor());
            g.setFont(new Font("stsong", Font.BOLD, 35));
            g.drawString(randArr[i], (width / randArr.length) * i + 2, height * 2 / 3);
        }
        g.dispose();
    }

    private void drawLine(Graphics2D g, int height, int width, int count) {
        for (int i = 0; i < count; i++) {
            g.setColor(this.getRandColor());
            int x1 = random.nextInt(width);
            int x2 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private Color getRandColor() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r, g, b);
    }

}
