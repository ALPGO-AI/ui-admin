package cc.alpgo.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author alpgo
 */
@Component
@ConfigurationProperties(prefix = "alpgo")
public class AlpgoConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 上传路径 */
    private static String profile;

    /** 获取地址开关 */
    private static boolean addressEnabled;

    /** 验证码类型 */
    private static String captchaType;
    private String cosApiSecretId;
    private String cosApiSecretKey;
    private String cosApiBucketName;
    private String cosApiRegion;

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

    public String getCosApiBucketName() {
        return cosApiBucketName;
    }

    public void setCosApiBucketName(String cosApiBucketName) {
        this.cosApiBucketName = cosApiBucketName;
    }

    public String getCosApiRegion() {
        return cosApiRegion;
    }

    public void setCosApiRegion(String cosApiRegion) {
        this.cosApiRegion = cosApiRegion;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        AlpgoConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        AlpgoConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        AlpgoConfig.captchaType = captchaType;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取分析文件上传路径
     */
    public static String getAnalyzePath()
    {
        return getProfile() + "/analyze";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}