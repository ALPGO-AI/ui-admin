package cc.alpgo.sdtool.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class AutoLayoutGenerator {
    @Autowired
    private FontLoader fontLoader;
    private static final int MARGIN = 20; // 安全边距的大小
    private static final int SINGLE_SIZE = 96;
    public String generateImageByTextAutoLayout(String text, int fontSize, int canvasWidth, int canvasHeight) throws IOException {
        Font font = new Font(fontLoader.loadFont(), Font.BOLD, fontSize);
        Color backgroundColor = Color.BLACK;
        Color foregroundColor = Color.WHITE;
        // 使用正则表达式将多个空格替换为两个空格
        String result = text.replaceAll("\\s{2,}", "  ");
        return generateTextLayout(result, font, backgroundColor, foregroundColor, canvasWidth, canvasHeight);
    }

    public static String generateTextLayout(String text, Font font, Color backgroundColor, Color foregroundColor, int canvasWidth, int canvasHeight) throws IOException {
        // 创建黑底白字的图像
        int fontSize = font.getSize();
        String[] paragraphs = text.split(" {2}");
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
            int minSize = 50;
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
        int offsetYInit = (int) ((canvasHeight - totalHeight) / 2);
        int currentY = offsetYInit + MARGIN;

        // 计算每个区域的起始偏移量
        int[] offsetXArray = new int[totalAreas];
        int[] offsetYArray = new int[totalAreas];

        // 遍历每个区域并计算偏移量
        for (int index = 0; index < totalAreas; index++) {
            Dimension regionSize = dimensionMap.get(index);
            // 计算 offsetX
            int regionWidth = (int) regionSize.getWidth();
            int rightX = index - 1 >= 0 ? offsetXArray[index - 1] + regionWidth + fontMap.get(index).getSize() : canvasWidth;
            int limit = canvasWidth - (MARGIN * 2);
            if (index - 1 >= 0 && rightX<limit) {
                offsetXArray[index] = offsetXArray[index - 1] + regionWidth + MARGIN;
                // 计算 offsetY
                offsetYArray[index] = currentY;
            } else {
                if (index > 0) {
                    // 更新当前区域的垂直偏移量
                    currentY += dimensionMap.get(index - 1).getHeight() + MARGIN;
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
//            g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
//            g.drawRect(offsetX, offsetY, (int) dimensionMap.get(index).getWidth(), (int) dimensionMap.get(index).getHeight());
            // 绘制文本
            g.setFont(fontMap.get(index));
//            g.drawString(paragraphs[index], offsetX, offsetY);
            drawTexts(g, paragraphs[index], canvasWidth - (MARGIN * 2), offsetX, offsetY);
        }

        String base64Image = convertImageToBase64(image);
        return base64Image;
    }

    private static void drawTexts(Graphics2D g2d, String text, int maxWidth, int offsetX, int offsetY) {
        // Calculate the bounding rectangle for text
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D bounds = fontMetrics.getStringBounds(text, g2d);
        int baseY = 144;

        // Wrap the text if it exceeds the maximum width
        if (bounds.getWidth() > maxWidth) {
            String[] words = text.split(" ");
            int currentLineY = (int) bounds.getY();
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                Rectangle2D wordBounds = fontMetrics.getStringBounds(currentLine + word, g2d);

                if (wordBounds.getWidth() > maxWidth) {
                    g2d.drawString(currentLine.toString(), offsetX,baseY + offsetY + currentLineY);
                    currentLine = new StringBuilder(word + " ");
                    currentLineY += fontMetrics.getHeight();
                } else {
                    currentLine.append(word).append(" ");
                }
            }

            g2d.drawString(currentLine.toString(), offsetX, baseY + offsetY + currentLineY);
        } else {
            // Draw the entire text without wrapping
            g2d.drawString(text, offsetX, baseY + offsetY + (int) bounds.getY());
        }

    }

    public static Dimension calculateRegionSize(int wordCount, Font font, Graphics graphics) {
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        int fontWidth = font.getSize();

        int regionWidth = calculateRegionWidth(wordCount, fontWidth);
        int regionHeight = calculateRegionHeight(wordCount, fontHeight, fontMetrics.getLeading());

        return new Dimension(regionWidth, regionHeight);
    }

    private static int calculateRegionWidth(int wordCount, int fontWidth) {
        // 根据每行字数和字体宽度计算区域宽度
        int maxWordsPerLine = 9;
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