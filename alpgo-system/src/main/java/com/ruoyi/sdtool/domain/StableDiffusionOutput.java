package com.alpgo.sdtool.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alpgo.common.annotation.Excel;
import com.alpgo.common.core.domain.TreeEntity;

/**
 * stable_diffusion_output对象 stable_diffusion_output
 *
 * @author marcus
 * @date 2023-03-23
 */
public class StableDiffusionOutput extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** output id */
    private Long outputId;

    /** 输出图片地址 */
    @Excel(name = "输出图片地址")
    private String outputImageUrl;

    /** 参考原图地址 */
    @Excel(name = "参考原图地址")
    private String referenceImageUrl;

    /** 使用的种子 */
    @Excel(name = "使用的种子")
    private String seed;

    /** 类型（绘图/线稿/三视图） */
    @Excel(name = "类型", readConverterExp = "绘=图/线稿/三视图")
    private String type;

    /** 直出参数 */
    private String straightParameter;

    /** 参考输出图片id */
    @Excel(name = "参考输出图片id")
    private Long referenceOuputId;

    /** patternId */
    @Excel(name = "patternId")
    private Long patternId;

    /** patternId */
    @Excel(name = "模板风格")
    private String patternStyle;

    public String getPatternStyle() {
        return patternStyle;
    }

    public void setPatternStyle(String patternStyle) {
        this.patternStyle = patternStyle;
    }

    public void setOutputId(Long outputId)
    {
        this.outputId = outputId;
    }

    public Long getOutputId()
    {
        return outputId;
    }
    public void setOutputImageUrl(String outputImageUrl)
    {
        this.outputImageUrl = outputImageUrl;
    }

    public String getOutputImageUrl()
    {
        return outputImageUrl;
    }
    public void setReferenceImageUrl(String referenceImageUrl)
    {
        this.referenceImageUrl = referenceImageUrl;
    }

    public String getReferenceImageUrl()
    {
        return referenceImageUrl;
    }
    public void setSeed(String seed)
    {
        this.seed = seed;
    }

    public String getSeed()
    {
        return seed;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setStraightParameter(String straightParameter)
    {
        this.straightParameter = straightParameter;
    }

    public String getStraightParameter()
    {
        return straightParameter;
    }
    public void setReferenceOuputId(Long referenceOuputId)
    {
        this.referenceOuputId = referenceOuputId;
    }

    public Long getReferenceOuputId()
    {
        return referenceOuputId;
    }
    public void setPatternId(Long patternId)
    {
        this.patternId = patternId;
    }

    public Long getPatternId()
    {
        return patternId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("outputId", getOutputId())
            .append("outputImageUrl", getOutputImageUrl())
            .append("referenceImageUrl", getReferenceImageUrl())
            .append("seed", getSeed())
            .append("type", getType())
            .append("straightParameter", getStraightParameter())
            .append("referenceOuputId", getReferenceOuputId())
            .append("patternId", getPatternId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
