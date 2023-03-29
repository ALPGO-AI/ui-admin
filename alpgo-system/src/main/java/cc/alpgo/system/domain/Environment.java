package cc.alpgo.system.domain;

import java.util.List;

import cc.alpgo.common.annotation.Excel;
import cc.alpgo.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * environments对象 environment
 *
 * @author asahiluna
 * @date 2023-03-30
 */
public class Environment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long environmentId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String description;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String accessLevel;

    /** environment_parameters信息 */
    private List<EnvironmentParameters> environmentParametersList;

    public void setEnvironmentId(Long environmentId)
    {
        this.environmentId = environmentId;
    }

    public Long getEnvironmentId()
    {
        return environmentId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
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

    public List<EnvironmentParameters> getEnvironmentParametersList()
    {
        return environmentParametersList;
    }

    public void setEnvironmentParametersList(List<EnvironmentParameters> environmentParametersList)
    {
        this.environmentParametersList = environmentParametersList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("environmentId", getEnvironmentId())
            .append("name", getName())
            .append("type", getType())
            .append("description", getDescription())
            .append("accessLevel", getAccessLevel())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("environmentParametersList", getEnvironmentParametersList())
            .toString();
    }
}
