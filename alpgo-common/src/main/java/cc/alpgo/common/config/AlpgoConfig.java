package cc.alpgo.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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

    /** webhooks target */
    private List<String> webhooks;
    private String chatBoxAIKey;

    public List<String> getWebhooks() {
        if (CollectionUtils.isEmpty(webhooks)) {
            return new ArrayList<>();
        }
        return webhooks;
    }

    public void setWebhooks(List<String> webhooks) {

        this.webhooks = webhooks;
    }

    public String getChatBoxAIKey() {
        return chatBoxAIKey;
    }
    public void setChatBoxAIKey(String chatBoxAIKey) {
        this.chatBoxAIKey = chatBoxAIKey;
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
