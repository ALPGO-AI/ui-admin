package cc.alpgo.common.event;

import cc.alpgo.common.enums.CosConfig;
import org.springframework.context.ApplicationEvent;

import java.io.InputStream;

public class UploadToCosInputStreamEvent extends ApplicationEvent {
    private final InputStream inputStream;
    private final String key;
    private final CosConfig cosConfig;
    private final String wsId;

    public UploadToCosInputStreamEvent(String key, InputStream inputStream, CosConfig cosConfig, String wsId) {
        super(key);
        this.inputStream = inputStream;
        this.key = key;
        this.cosConfig = cosConfig;
        this.wsId = wsId;
    }

    public String getWsId() {
        return wsId;
    }

    public CosConfig getCosConfig() {
        return cosConfig;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getKey() {
        return key;
    }
}
