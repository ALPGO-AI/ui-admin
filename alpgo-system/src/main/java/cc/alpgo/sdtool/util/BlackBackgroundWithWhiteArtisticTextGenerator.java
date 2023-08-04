package cc.alpgo.sdtool.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import javax.imageio.ImageIO;

@Component
public class BlackBackgroundWithWhiteArtisticTextGenerator {
    @Autowired
    private FontLoader fontLoader;
    public String generateImageByText(String text, int fontSize, int canvasWidth, int canvasHeight) {
        String[] lines = text.split(",");

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, canvasWidth, canvasHeight);

        graphics.setColor(Color.WHITE);
        drawTextOnImage(graphics, lines, canvasWidth, canvasHeight, fontSize);

        graphics.dispose();
        String base64Image = convertImageToBase64(image);
        return base64Image;
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
    private void drawTextOnImage(Graphics graphics, String[] lines, int width, int height, int fontSize) {
        Font font = new Font(fontLoader.loadFont(), Font.BOLD, fontSize);
        graphics.setFont(font);

        FontMetrics fontMetrics = graphics.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        int startTextY = (height - (lines.length * lineHeight)) / 2;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int textWidth = fontMetrics.stringWidth(line);
            int textX = (width - textWidth) / 2;
            int textY = startTextY + (i * lineHeight) + fontMetrics.getAscent();
            graphics.drawString(line, textX, textY);
        }
    }

    public InputStream generateImageByTextReturnInputStream(String fontArtText, Integer fontArtSize, int canvasWidth, int canvasHeight) throws IOException {
        String[] lines = fontArtText.split(",");

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, canvasWidth, canvasHeight);

        graphics.setColor(Color.WHITE);
        drawTextOnImage(graphics, lines, canvasWidth, canvasHeight, fontArtSize);

        graphics.dispose();
        // Convert BufferedImage to InputStream
        InputStream inputStream = bufferedImageToInputStream(image);
        return inputStream;
    }

    private static InputStream bufferedImageToInputStream(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}