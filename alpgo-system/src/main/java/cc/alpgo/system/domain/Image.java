package cc.alpgo.system.domain;

import java.util.List;

import cc.alpgo.common.annotation.Excel;
import cc.alpgo.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * image对象 image
 *
 * @author marcus
 * @date 2023-03-31
 */
public class Image extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** image id */
    private Long imageId;

    /** image uri */
    @Excel(name = "image uri")
    private String uri;

    /** image_provider信息 */
    private List<ImageProvider> imageProviderList;

    public void setImageId(Long imageId)
    {
        this.imageId = imageId;
    }

    public Long getImageId()
    {
        return imageId;
    }
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return uri;
    }

    public List<ImageProvider> getImageProviderList()
    {
        return imageProviderList;
    }

    public void setImageProviderList(List<ImageProvider> imageProviderList)
    {
        this.imageProviderList = imageProviderList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("imageId", getImageId())
            .append("uri", getUri())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("imageProviderList", getImageProviderList())
            .toString();
    }
}
