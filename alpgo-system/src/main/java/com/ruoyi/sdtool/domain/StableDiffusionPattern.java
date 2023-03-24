package com.alpgo.sdtool.domain;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alpgo.common.annotation.Excel;
import com.alpgo.common.core.domain.BaseEntity;

import java.util.Map;

/**
 * stable_diffusion_pattern对象 stable_diffusion_pattern
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
    private String positiveprompt;

    /** 负向提示 */
    @Excel(name = "负向提示")
    private String negativeprompt;

    /** 样例图片 */
    @Excel(name = "样例图片")
    private String sampleimage;

    /** 预设模板 */
    @Excel(name = "预设模板")
    private String presettemplate;

    /** pattern风格 */
    @Excel(name = "pattern风格")
    private String patternstyle;
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
    public void setPositiveprompt(String positiveprompt)
    {
        this.positiveprompt = positiveprompt;
    }

    public String getPositiveprompt()
    {
        return positiveprompt;
    }
    public void setNegativeprompt(String negativeprompt)
    {
        this.negativeprompt = negativeprompt;
    }

    public String getNegativeprompt()
    {
        return negativeprompt;
    }
    public void setSampleimage(String sampleimage)
    {
        this.sampleimage = sampleimage;
    }

    public String getSampleimage()
    {
        return sampleimage;
    }
    public void setPresettemplate(String presettemplate)
    {
        this.presettemplate = presettemplate;
    }

    public String getPresettemplate()
    {
        return presettemplate;
    }
    public void setPatternstyle(String patternstyle)
    {
        this.patternstyle = patternstyle;
    }

    public String getPatternstyle()
    {
        return patternstyle;
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
            .append("positiveprompt", getPositiveprompt())
            .append("negativeprompt", getNegativeprompt())
            .append("sampleimage", getSampleimage())
            .append("presettemplate", getPresettemplate())
            .append("patternstyle", getPatternstyle())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
