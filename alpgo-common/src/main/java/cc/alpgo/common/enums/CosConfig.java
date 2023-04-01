package cc.alpgo.common.enums;

import static cc.alpgo.common.utils.sign.Md5Utils.hash;

public class CosConfig {
    public CosConfig() {
    }

    public CosConfig(String envName, Long envId, String cosApiSecretId, String cosApiSecretKey, String cosApiRegion, String cosApiBucketName) {
        this.envName = envName;
        this.envId = envId;
        this.cosApiSecretId = cosApiSecretId;
        this.cosApiSecretKey = cosApiSecretKey;
        this.cosApiRegion = cosApiRegion;
        this.cosApiBucketName = cosApiBucketName;
    }

    private String envName;
    private Long envId;
    private String cosApiSecretId;
    private String cosApiSecretKey;
    private String cosApiRegion;
    private String cosApiBucketName;

    public String getEnvName() {
        return envName;
    }

    public Long getEnvId() {
        return envId;
    }

    public String getCosApiSecretId() {
        return cosApiSecretId;
    }

    public void setCosApiSecretId(String cosApiSecretId) {
        this.cosApiSecretId = cosApiSecretId;
    }

    public String getCosApiSecretKey() {
        return cosApiSecretKey;
    }

    public void setCosApiSecretKey(String cosApiSecretKey) {
        this.cosApiSecretKey = cosApiSecretKey;
    }

    public String getCosApiRegion() {
        return cosApiRegion;
    }

    public void setCosApiRegion(String cosApiRegion) {
        this.cosApiRegion = cosApiRegion;
    }

    public String getCosApiBucketName() {
        return cosApiBucketName;
    }

    public void setCosApiBucketName(String cosApiBucketName) {
        this.cosApiBucketName = cosApiBucketName;
    }

    public String getHash() {
        return hash(cosApiSecretId + cosApiSecretKey + cosApiRegion + cosApiBucketName);
    }
}
