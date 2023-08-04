package cc.alpgo.sdtool.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class AutoLayoutGenerator {
    @Autowired
    private FontLoader fontLoader;
    private static final int MARGIN = 25; // 安全边距的大小
    private static final int SINGLE_SIZE = 108;
    public String generateImageByTextAutoLayout(String text, int fontSize, int canvasWidth, int canvasHeight) throws IOException {
        Font font = new Font(fontLoader.loadFont(), Font.BOLD, fontSize);
        Color backgroundColor = Color.BLACK;
        Color foregroundColor = Color.WHITE;

        return generateTextLayout(text, font, backgroundColor, foregroundColor, canvasWidth, canvasHeight);
    }

    public static String generateTextLayout(String text, Font font, Color backgroundColor, Color foregroundColor, int canvasWidth, int canvasHeight) throws IOException {
        // 创建黑底白字的图像
        int fontSize = font.getSize();
        String[] paragraphs = text.split(" {8}");
        Integer totalAreas = paragraphs.length;
        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(font);
        g.setBackground(backgroundColor);
        g.setColor(foregroundColor);
        g.clearRect(0, 0, canvasWidth, canvasHeight);
        Map<Integer, Dimension> dimensionMap = new HashMap<>();
        Map<Integer, Font> fontMap = new HashMap<>();

        // 绘制每个区域的边框，并自动调整字体大小
        for (int index = 0; index < totalAreas; index++) {
            // 单字放大
            Integer tempSize = paragraphs[index].length() == 1 ? SINGLE_SIZE : fontSize;
            Font tempFont = new Font(font.getFamily(), font.getStyle(), tempSize);
            Dimension regionSize = calculateRegionSize(paragraphs[index].length(), tempFont, g);
            double regionWidth = regionSize.getWidth();
            double regionHeight = regionSize.getHeight();
            // 自动调整字体大小，直到符合高度要求
            int minSize = 45;
//            while (regionHeight > canvasHeight) {
//                tempSize -= 2; // 递减字体大小
//                tempFont = new Font(font.getFamily(), font.getStyle(), tempSize);
//                regionSize = calculateRegionSize(paragraphs[index].length(), tempFont, g);
//                regionWidth = regionSize.getWidth();
//                regionHeight = regionSize.getHeight();
//            }
            // 自动调整字体大小，直到符合宽度要求
            while (regionWidth > canvasWidth) {
                if (tempSize <= minSize) {
                    break;
                }
                tempSize -= 2; // 递减字体大小
                tempFont = new Font(font.getFamily(), font.getStyle(), tempSize);
                regionSize = calculateRegionSize(paragraphs[index].length(), tempFont, g);
                regionWidth = regionSize.getWidth();
                regionHeight = regionSize.getHeight();
            }

            dimensionMap.put(index, regionSize);
            fontMap.put(index, tempFont);
        }

        // 计算总的区域高度
        double totalHeight = dimensionMap.values().stream()
                .mapToDouble(Dimension::getHeight)
                .sum();

        // 如果总高度超过画布高度，按比例缩小字体大小
        if (totalHeight > canvasHeight) {
            double scale = canvasHeight / totalHeight;
            totalHeight = 0;
            for (int index = 0; index < totalAreas; index++) {
                Dimension regionSize = dimensionMap.get(index);
                Font tempFont = fontMap.get(index);
                double newHeight = regionSize.getHeight() * scale;
                // 调整区域大小和字体大小
                regionSize = new Dimension(regionSize.width, (int) newHeight);
                tempFont = tempFont.deriveFont((float) (tempFont.getSize() * scale));
                dimensionMap.put(index, regionSize);
                fontMap.put(index, tempFont);
                totalHeight += newHeight;
            }
        }

        // 计算每个区域的垂直偏移量
        int offsetYInit = (int) ((canvasHeight - totalHeight) / 2) + SINGLE_SIZE;
        int currentY = offsetYInit + MARGIN;

        // 计算每个区域的起始偏移量
        int[] offsetXArray = new int[totalAreas];
        int[] offsetYArray = new int[totalAreas];

        // 遍历每个区域并计算偏移量
        for (int index = 0; index < totalAreas; index++) {
            Dimension regionSize = dimensionMap.get(index);
            // 计算 offsetX
            int regionWidth = (int) regionSize.getWidth();
            int rightX = index - 1 >= 0 ? offsetXArray[index - 1] + regionWidth + fontMap.get(index).getSize() : 0;
            int limit = canvasWidth;
            if (index - 1 >= 0 && rightX<=limit) {
                offsetXArray[index] = offsetXArray[index - 1] + regionWidth + MARGIN;
                // 计算 offsetY
                offsetYArray[index] = currentY;
            } else {
                if (index > 0) {
                    // 更新当前区域的垂直偏移量
                    currentY += regionSize.getHeight() + MARGIN;
                }
                offsetXArray[index] = MARGIN;
                // 计算 offsetY
                offsetYArray[index] = currentY;
            }
        }

        // 遍历每个区域并绘制
        for (int index = 0; index < totalAreas; index++) {
            // 获取当前区域的偏移量
            int offsetX = offsetXArray[index];
            int offsetY = offsetYArray[index];

            // 绘制文本
            g.setFont(fontMap.get(index));
            g.drawString(paragraphs[index], offsetX, offsetY);
        }

        String base64Image = convertImageToBase64(image);
        return base64Image;
    }
    public static Dimension calculateRegionSize(int wordCount, Font font, Graphics graphics) {
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        int fontWidth = fontMetrics.getMaxAdvance();

        int regionWidth = calculateRegionWidth(wordCount, fontWidth);
        int regionHeight = calculateRegionHeight(wordCount, fontHeight, fontMetrics.getLeading());

        return new Dimension(regionWidth, regionHeight);
    }

    private static int calculateRegionWidth(int wordCount, int fontWidth) {
        // 根据每行字数和字体宽度计算区域宽度
        // 这里假设每行最多容纳10个字
        int maxWordsPerLine = 10;
        int lines = (int) Math.ceil((double) wordCount / maxWordsPerLine);
        if (wordCount < maxWordsPerLine) {
            return wordCount * fontWidth;
        }
        return maxWordsPerLine * fontWidth; // 每行字数乘以字体宽度
    }

    private static int calculateRegionHeight(int wordCount, int fontHeight, int leading) {
        // 根据每行字数和字体高度计算区域高度
        // 这里假设每行最多容纳10个字
        int maxWordsPerLine = 10;
        int lines = (int) Math.ceil((double) wordCount / maxWordsPerLine);

        // 区域高度 = 行数 * 字体高度 + (行数 - 1) * 行间距
        return lines * fontHeight + (lines - 1) * leading;
    }

    private static String convertImageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static InputStream bufferedImageToInputStream(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}