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
            Resource resource1 = resourceLoader.getResource("classpath:fonts/SmileySans-Oblique-2.ttf");
            Font font1 = Font.createFont(Font.TRUETYPE_FONT, resource1.getInputStream());

            Resource resource2 = resourceLoader.getResource("classpath:fonts/douyuFont-2.otf");
            Font font2 = Font.createFont(Font.TRUETYPE_FONT, resource2.getInputStream());
            // Register the font with the GraphicsEnvironment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font1);
            ge.registerFont(font2);
            return font2.getFontName();
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
