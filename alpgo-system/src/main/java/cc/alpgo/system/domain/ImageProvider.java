package cc.alpgo.system.domain;

import cc.alpgo.common.annotation.Excel;
import cc.alpgo.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * image_provider对象 image_provider
 *
 * @author marcus
 * @date 2023-03-31
 */
public class ImageProvider extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** provider id */
    private Long providerId;

    /** 环境id */
    @Excel(name = "环境id")
    private Long envId;

    /** image id */
    @Excel(name = "image id")
    private Long imageId;

    /** 完整地址 */
    @Excel(name = "完整地址")
    private String url;

    public void setProviderId(Long providerId)
    {
        this.providerId = providerId;
    }

    public Long getProviderId()
    {
        return providerId;
    }
    public void setEnvId(Long envId)
    {
        this.envId = envId;
    }

    public Long getEnvId()
    {
        return envId;
    }
    public void setImageId(Long imageId)
    {
        this.imageId = imageId;
    }

    public Long getImageId()
    {
        return imageId;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("providerId", getProviderId())
            .append("envId", getEnvId())
            .append("imageId", getImageId())
            .append("url", getUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
