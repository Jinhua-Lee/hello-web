package com.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author Jinhua
 */
public class VerifyCode {
    private final int width = 70;
    private final int height = 35;
    private Random r = new Random();
    private final String[] fontNames = {"宋体", "黑体", "微软雅黑", "华文楷体", "楷体_G2312"};
    private static final String CODES = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ";
    private final Color bgColor = new Color(255, 255, 255);
    private String text;

    /**
     * 获取随机颜色
     *
     * @return 随机颜色
     */
    private Color randomColor() {
        int red = r.nextInt(150);
        int green = r.nextInt(150);
        int blue = r.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 获取随机字体
     *
     * @return 随机字体
     */
    private Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);
        int size = r.nextInt(5) + 24;
        return new Font(fontName, style, size);
    }

    /**
     * 添加干扰线
     *
     * @param image 图片
     */
    private void drawLine(BufferedImage image) {
        int num = 3;
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(width);
            int y1 = r.nextInt(height);
            int x2 = r.nextInt(width);
            int y2 = r.nextInt(height);
            g2.setStroke(new BasicStroke(1.5F));
            g2.setColor(Color.BLUE);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 获取随机字符
     *
     * @return 随机字符
     */
    private char randomChar() {
        int index = r.nextInt(CODES.length());
        return CODES.charAt(index);
    }

    /**
     * 获取初始绘制环境
     *
     * @return 绘制环境
     */
    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(this.bgColor);
        g2.fillRect(0, 0, width, height);
        return image;
    }

    public BufferedImage getImage() {
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            sb.append(s);
            float x = i * 1.0F * width / 4;
            g2.setFont(randomFont());
            g2.setColor(randomColor());
            g2.drawString(s, x, height - 5);
        }
        text = sb.toString();
        //	添加干扰线条
        drawLine(image);
        return image;
    }

    public String getText() {
        return text;
    }

    public static void output(BufferedImage image, OutputStream os) throws IOException {
        ImageIO.write(image, "JPEG", os);
    }
}
