package cc.alpgo.sdtool.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class FontLoader {

    private final ResourceLoader resourceLoader;

    public FontLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String loadFont() {
        try {
            Resource resource = resourceLoader.getResource("classpath:fonts/SmileySans-Oblique-2.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, resource.getInputStream());

            // Register the font with the GraphicsEnvironment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.getFontName();
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
