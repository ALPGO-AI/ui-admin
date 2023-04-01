package cc.alpgo.common.utils;

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

    private String envName;
    public StableDiffusionEnv(String envName, Long environmentId, String domain, String username, String password, Integer txt2imgFnIndex, Integer img2imgFnIndex, Integer txt2imgControlNetFnIndex, Integer img2imgControlNetFnIndex, Boolean isLoraPluginInstalled) {
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
}
