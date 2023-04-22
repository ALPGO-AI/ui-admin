package cc.alpgo.common.utils;

import static java.util.Objects.hash;

public class StableDiffusionEnv {
    private Long envId;
    private String domain;
    private String username;
    private String password;
    private Integer txt2imgFnIndex;
    private Integer img2imgFnIndex;
    private Integer txt2imgControlNetFnIndex;
    private Integer img2imgControlNetFnIndex;
    private Boolean isLoraPluginInstalled;

    private Integer switchModelFnIndex;

    private String envName;

    private Boolean isUltimateUpscalePluginInstalled;
    private Integer fetchControlNetModelFnIndex;
    private Boolean isDisableGradioAuth = false;

    public StableDiffusionEnv(String envName, Long environmentId, String domain, String username, String password, Integer txt2imgFnIndex, Integer img2imgFnIndex, Integer txt2imgControlNetFnIndex, Integer img2imgControlNetFnIndex, Boolean isLoraPluginInstalled, Integer switchModelFnIndex, Boolean isUltimateUpscalePluginInstalled, Integer fetchControlNetModelFnIndex) {
        this.envName = envName;
        this.envId = environmentId;
        this.domain = domain;
        this.username = username;
        this.password = password;
        this.txt2imgFnIndex = txt2imgFnIndex;
        this.img2imgFnIndex = img2imgFnIndex;
        this.txt2imgControlNetFnIndex = txt2imgControlNetFnIndex;
        this.img2imgControlNetFnIndex = img2imgControlNetFnIndex;
        this.isLoraPluginInstalled = isLoraPluginInstalled;
        this.switchModelFnIndex = switchModelFnIndex;
        this.isUltimateUpscalePluginInstalled = isUltimateUpscalePluginInstalled;
        this.fetchControlNetModelFnIndex = fetchControlNetModelFnIndex;
    }

    public Boolean getDisableGradioAuth() {
        return isDisableGradioAuth;
    }

    public void setDisableGradioAuth(Boolean disableGradioAuth) {
        isDisableGradioAuth = disableGradioAuth;
    }

    public Boolean getUltimateUpscalePluginInstalled() {
        return isUltimateUpscalePluginInstalled;
    }

    public Integer getSwitchModelFnIndex() {
        return switchModelFnIndex;
    }

    public String getEnvName() {
        return envName;
    }

    public Long getEnvId() {
        return envId;
    }

    public Boolean getLoraPluginInstalled() {
        return isLoraPluginInstalled;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getTxt2imgFnIndex() {
        return txt2imgFnIndex;
    }

    public Integer getImg2imgFnIndex() {
        return img2imgFnIndex;
    }

    public Integer getTxt2imgControlNetFnIndex() {
        return txt2imgControlNetFnIndex;
    }

    public Integer getImg2imgControlNetFnIndex() {
        return img2imgControlNetFnIndex;
    }

    public String getEnvKey() {
        return this.getEnvId() + ":WebUI:" + this.getEnvName();
    }

    public Integer getFetchControlNetModelFnIndex() {
        return fetchControlNetModelFnIndex;
    }

    public void setFetchControlNetModelFnIndex(Integer fetchControlNetModelFnIndex) {
        this.fetchControlNetModelFnIndex = fetchControlNetModelFnIndex;
    }
}
