package cc.alpgo.common.event;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.StableDiffusionEnv;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class SdToolExecuteGenerateByPatternIdEvent extends ApplicationEvent implements EnvTaskEvent {
    public SdToolExecuteGenerateByPatternIdEvent(String uuid, Long patternId, List<CosConfig> cosConfigs, StableDiffusionEnv sdEnv, String wsId) {
        super(uuid);
        this.uuid = uuid;
        this.patternId = patternId;
        this.cosConfigs = cosConfigs;
        this.sdEnv = sdEnv;
        this.wsId = wsId;
    }

    public String getWsId() {
        return wsId;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getPatternId() {
        return patternId;
    }

    public List<CosConfig> getCosConfigs() {
        return cosConfigs;
    }

    public StableDiffusionEnv getSdEnv() {
        return sdEnv;
    }

    private String uuid;
    private Long patternId;
    private List<CosConfig> cosConfigs;
    private StableDiffusionEnv sdEnv;
    private String wsId;

    @Override
    public String toString() {
        return "SdToolGenerateByPatternIdEvent{" +
                "uuid='" + uuid + '\'' +
                ", patternId=" + patternId +
                ", cosConfigs=" + cosConfigs +
                ", sdEnv=" + sdEnv +
                '}';
    }

    @Override
    public String getEnvKey() {
        return sdEnv.getEnvKey();
    }
}
