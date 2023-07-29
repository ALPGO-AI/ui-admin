package cc.alpgo.common.event;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.common.utils.uuid.UUID;
import org.springframework.context.ApplicationEvent;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class UploadToCosInputStreamEvent extends ApplicationEvent {
    private final InputStream inputStream;
    private final String key;
    private final List<CosConfig> cosConfigs;
    private final String envKey;
    private final StableDiffusionEnv env;

    public UploadToCosInputStreamEvent(String key, InputStream inputStream, List<CosConfig> cosConfigs, StableDiffusionEnv env) {
        super(key);
        this.inputStream = inputStream;
        this.key = key;
        this.cosConfigs = cosConfigs;
        this.envKey = env != null ? env.getEnvKey() : UUID.randomUUID().toString();
        this.env = env;
    }

    public StableDiffusionEnv getEnv() {
        return env;
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
