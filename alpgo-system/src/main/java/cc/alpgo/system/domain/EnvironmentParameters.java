package cc.alpgo.system.domain;

import cc.alpgo.common.annotation.Excel;
import cc.alpgo.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * environment_parameters对象 environment_parameters
 *
 * @author asahiluna
 * @date 2023-03-30
 */
public class EnvironmentParameters extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long parameterId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long environmentId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String paramName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String paramValue;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String paramType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String description;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String accessLevel;

    public void setParameterId(Long parameterId)
    {
        this.parameterId = parameterId;
    }

    public Long getParameterId()
    {
        return parameterId;
    }
    public void setEnvironmentId(Long environmentId)
    {
        this.environmentId = environmentId;
    }

    public Long getEnvironmentId()
    {
        return environmentId;
    }
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public String getParamName()
    {
        return paramName;
    }
    public void setParamValue(String paramValue)
    {
        this.paramValue = paramValue;
    }

    public String getParamValue()
    {
        return paramValue;
    }
    public void setParamType(String paramType)
    {
        this.paramType = paramType;
    }

    public String getParamType()
    {
        return paramType;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setAccessLevel(String accessLevel)
    {
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel()
    {
        return accessLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("parameterId", getParameterId())
            .append("environmentId", getEnvironmentId())
            .append("paramName", getParamName())
            .append("paramValue", getParamValue())
            .append("paramType", getParamType())
            .append("description", getDescription())
            .append("accessLevel", getAccessLevel())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
