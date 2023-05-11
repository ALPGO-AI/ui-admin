package cc.alpgo.tag.domain;

import cc.alpgo.common.annotation.Excel;
import cc.alpgo.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 标签对象 common_tag
 *
 * @author marcus
 * @date 2023-05-12
 */
 public class CommonTag extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 标签id */
    private Long tagId;

    /** 父类id */
    @Excel(name = "父类id")
    private Long parentid;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String name;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer ordernum;

    /** 标签描述 */
    @Excel(name = "标签描述")
    private String desctag;

    /** 标签类别（0正向 1反向） */
    @Excel(name = "标签类别", readConverterExp = "0=正向,1=反向")
    private String type;

    /** 标签状态（0正常 1停用） */
    @Excel(name = "标签状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标签（0代表存在 2代表删除） */
    private String delFlag;

    public void setTagId(Long tagId)
    {
        this.tagId = tagId;
    }

    public Long getTagId()
    {
        return tagId;
    }
    public void setParentid(Long parentid)
    {
        this.parentid = parentid;
    }

    public Long getParentid()
    {
        return parentid;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setOrdernum(Integer ordernum)
    {
        this.ordernum = ordernum;
    }

    public Integer getOrdernum()
    {
        return ordernum;
    }
    public void setDesctag(String desctag)
    {
        this.desctag = desctag;
    }

    public String getDesctag()
    {
        return desctag;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("tagId", getTagId())
                .append("parentid", getParentid())
                .append("name", getName())
                .append("ordernum", getOrdernum())
                .append("desctag", getDesctag())
                .append("type", getType())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
