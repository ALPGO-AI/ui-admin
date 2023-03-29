package cc.alpgo.common.event;

import org.springframework.context.ApplicationEvent;

public class UploadToCosEvent extends ApplicationEvent {
    private final String imageBase64;
    private final String key;

    public UploadToCosEvent(String key, String image) {
        super(key);
        this.imageBase64 = image;
        this.key = key;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public String getKey() {
        return key;
    }
}
