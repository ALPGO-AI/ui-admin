package cc.alpgo.sdtool.domain;

import cc.alpgo.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cc.alpgo.common.annotation.Excel;

import java.util.ArrayList;
import java.util.List;

/**
 * stableDiffusionPattern对象 stableDiffusionPattern
 *
 * @author marcus
 * @date 2023-03-21
 */
public class StableDiffusionPattern extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** pattern设置id */
    private Long patternId;

    /** 模型 */
    @Excel(name = "模型")
    private String model;

    /** 正向提示 */
    @Excel(name = "正向提示")
    private String positivePrompt;

    /** 负向提示 */
    @Excel(name = "负向提示")
    private String negativePrompt;

    /** 样例图片 */
    @Excel(name = "样例图片")
    private String sampleImage;

    /** 预设模板 */
    @Excel(name = "预设模板")
    private String presetTemplate;

    /** pattern风格 */
    @Excel(name = "pattern风格")
    private String patternStyle;
    /** pattern风格 */
    @Excel(name = "参数JSON")
    private String parametersJson;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public String getParametersJson() {
        return parametersJson;
    }

    public void setParametersJson(String parametersJson) {
        this.parametersJson = parametersJson;
    }

    public void setPatternId(Long patternId)
    {
        this.patternId = patternId;
    }

    public Long getPatternId()
    {
        return patternId;
    }
    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return model;
    }
    public void setPositivePrompt(String positivePrompt)
    {
        this.positivePrompt = positivePrompt;
    }

    public String getPositivePrompt()
    {
        return positivePrompt;
    }
    public void setNegativePrompt(String negativePrompt)
    {
        this.negativePrompt = negativePrompt;
    }

    public String getNegativePrompt()
    {
        return negativePrompt;
    }
    public void setSampleImage(String sampleImage)
    {
        this.sampleImage = sampleImage;
    }

    public String getSampleImage()
    {
        return sampleImage;
    }
    public void setPresetTemplate(String presetTemplate)
    {
        this.presetTemplate = presetTemplate;
    }

    public String getPresetTemplate()
    {
        return presetTemplate;
    }
    public void setPatternStyle(String patternStyle)
    {
        this.patternStyle = patternStyle;
    }

    public String getPatternStyle()
    {
        return patternStyle;
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
            .append("patternId", getPatternId())
            .append("model", getModel())
            .append("positivePrompt", getPositivePrompt())
            .append("negativePrompt", getNegativePrompt())
            .append("sampleImage", getSampleImage())
            .append("presetTemplate", getPresetTemplate())
            .append("patternStyle", getPatternStyle())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
