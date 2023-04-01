package cc.alpgo.common.event;

import cc.alpgo.common.enums.CosConfig;
import org.springframework.context.ApplicationEvent;

public class UploadToCosEvent extends ApplicationEvent {
    private final String imageBase64;
    private final String key;
    private final CosConfig cosConfig;

    public UploadToCosEvent(String key, String image, CosConfig cosConfig) {
        super(key);
        this.imageBase64 = image;
        this.key = key;
        this.cosConfig = cosConfig;
    }

    public CosConfig getCosConfig() {
        return cosConfig;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public String getKey() {
        return key;
    }
}
