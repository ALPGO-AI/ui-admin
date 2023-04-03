package cc.alpgo.common.event;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.StableDiffusionEnv;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

public class SdToolExecuteGenerateByPatternIdEvent extends ApplicationEvent implements EnvTaskEvent {
    public SdToolExecuteGenerateByPatternIdEvent(String uuid, Long patternId, List<CosConfig> cosConfigs, StableDiffusionEnv sdEnv, String wsId, Map<String, Object> extraGenerateParams) {
        super(uuid);
        this.uuid = uuid;
        this.patternId = patternId;
        this.cosConfigs = cosConfigs;
        this.sdEnv = sdEnv;
        this.wsId = wsId;
        this.extraGenerateParams = extraGenerateParams;
    }

    public Map<String, Object> getExtraGenerateParams() {
        return extraGenerateParams;
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

    private Map<String, Object> extraGenerateParams;
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
