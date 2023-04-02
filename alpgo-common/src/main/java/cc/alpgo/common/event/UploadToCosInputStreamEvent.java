package cc.alpgo.common.event;

import cc.alpgo.common.enums.CosConfig;
import org.springframework.context.ApplicationEvent;

import java.io.InputStream;
import java.util.List;

public class UploadToCosInputStreamEvent extends ApplicationEvent {
    private final InputStream inputStream;
    private final String key;
    private final List<CosConfig> cosConfigs;
    private final String envKey;

    public UploadToCosInputStreamEvent(String key, InputStream inputStream, List<CosConfig> cosConfigs, String envKey) {
        super(key);
        this.inputStream = inputStream;
        this.key = key;
        this.cosConfigs = cosConfigs;
        this.envKey = envKey;
    }

    public String getEnvKey() {
        return envKey;
    }

    public List<CosConfig> getCosConfigs() {
        return cosConfigs;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getKey() {
        return key;
    }
}
