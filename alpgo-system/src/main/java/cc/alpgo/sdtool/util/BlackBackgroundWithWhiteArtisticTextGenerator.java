package cc.alpgo.sdtool.util;
import cc.alpgo.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;

@Component
public class BlackBackgroundWithWhiteArtisticTextGenerator {

    String text = "以（い）为,伊（イ）人,来了";  // 要生成的文本
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

    private String convertImageToBase64(BufferedImage image) {
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
}