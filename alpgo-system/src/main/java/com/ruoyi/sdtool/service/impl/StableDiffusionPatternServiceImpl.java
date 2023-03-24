package com.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;
import com.alpgo.common.utils.DateUtils;
import com.alpgo.common.utils.uuid.UUID;
import com.alpgo.sdtool.service.IStableDiffusionOutputService;
import com.alpgo.sdtool.util.ImageApiUtil;
import com.alpgo.sdtool.util.StableDiffusionApiParams;
import com.alpgo.sdtool.util.StableDiffusionApiResponse;
import com.alpgo.sdtool.util.StableDiffusionApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alpgo.sdtool.mapper.StableDiffusionPatternMapper;
import com.alpgo.sdtool.domain.StableDiffusionPattern;
import com.alpgo.sdtool.service.IStableDiffusionPatternService;
import org.springframework.util.CollectionUtils;

/**
 * stable_diffusion_patternService业务层处理
 *
 * @author marcus
 * @date 2023-03-21
 */
@Service
public class StableDiffusionPatternServiceImpl implements IStableDiffusionPatternService
{
    @Autowired
    private StableDiffusionPatternMapper stableDiffusionPatternMapper;
    @Autowired
    private StableDiffusionApiUtil stableDiffusionApiUtil;
    @Autowired
    private ImageApiUtil imageApiUtil;
    @Autowired
    private IStableDiffusionOutputService stableDiffusionOutputService;

    /**
     * 查询stable_diffusion_pattern
     *
     * @param patternId stable_diffusion_pattern主键
     * @return stable_diffusion_pattern
     */
    @Override
    public StableDiffusionPattern selectStableDiffusionPatternByPatternId(Long patternId)
    {
        return stableDiffusionPatternMapper.selectStableDiffusionPatternByPatternId(patternId);
    }

    /**
     * 查询stable_diffusion_pattern列表
     *
     * @param stableDiffusionPattern stable_diffusion_pattern
     * @return stable_diffusion_pattern
     */
    @Override
    public List<StableDiffusionPattern> selectStableDiffusionPatternList(StableDiffusionPattern stableDiffusionPattern)
    {
        return stableDiffusionPatternMapper.selectStableDiffusionPatternList(stableDiffusionPattern);
    }

    /**
     * 新增stable_diffusion_pattern
     *
     * @param stableDiffusionPattern stable_diffusion_pattern
     * @return 结果
     */
    @Override
    public int insertStableDiffusionPattern(StableDiffusionPattern stableDiffusionPattern)
    {
        stableDiffusionPattern.setCreateTime(DateUtils.getNowDate());
        return stableDiffusionPatternMapper.insertStableDiffusionPattern(stableDiffusionPattern);
    }

    /**
     * 修改stable_diffusion_pattern
     *
     * @param stableDiffusionPattern stable_diffusion_pattern
     * @return 结果
     */
    @Override
    public int updateStableDiffusionPattern(StableDiffusionPattern stableDiffusionPattern)
    {
        stableDiffusionPattern.setUpdateTime(DateUtils.getNowDate());
        return stableDiffusionPatternMapper.updateStableDiffusionPattern(stableDiffusionPattern);
    }

    /**
     * 批量删除stable_diffusion_pattern
     *
     * @param patternIds 需要删除的stable_diffusion_pattern主键
     * @return 结果
     */
    @Override
    public int deleteStableDiffusionPatternByPatternIds(Long[] patternIds)
    {
        return stableDiffusionPatternMapper.deleteStableDiffusionPatternByPatternIds(patternIds);
    }

    /**
     * 删除stable_diffusion_pattern信息
     *
     * @param patternId stable_diffusion_pattern主键
     * @return 结果
     */
    @Override
    public int deleteStableDiffusionPatternByPatternId(Long patternId)
    {
        return stableDiffusionPatternMapper.deleteStableDiffusionPatternByPatternId(patternId);
    }

    @Override
    public StableDiffusionApiResponse generateByPatternId(Map<String, String> params, Long patternId) throws IOException {
        StableDiffusionPattern stableDiffusionPattern = selectStableDiffusionPatternByPatternId(patternId);
        if (stableDiffusionPattern == null) {
            return null;
        }
        String negativeprompt = stableDiffusionPattern.getNegativeprompt();
        String positiveprompt = stableDiffusionPattern.getPositiveprompt();
        String sessionHash = UUID.randomUUID().toString();
        StableDiffusionApiResponse result = stableDiffusionApiUtil.sendApiToImg(params, new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), "-1").toPreDict(sessionHash));
        String fileName = result.getFileName();
        stableDiffusionPattern.setSampleimage(fileName);
        stableDiffusionPatternMapper.updateStableDiffusionPattern(stableDiffusionPattern);
        // 添加output数据
        stableDiffusionOutputService.generateOutput(stableDiffusionPattern, result, "GENERATE_IMAGE");
        return result;
    }

    @Override
    public StableDiffusionApiResponse generateSketchBySampleImg(Map<String, String> params, Long patternId) throws IOException {
        StableDiffusionPattern stableDiffusionPattern = selectStableDiffusionPatternByPatternId(patternId);
        if (stableDiffusionPattern == null) {
            return null;
        }
        if (stableDiffusionPattern.getSampleimage() == null) {
            return null;
        }
        String negativeprompt = stableDiffusionPattern.getNegativeprompt();
        String positiveprompt = stableDiffusionPattern.getPositiveprompt();
        StableDiffusionApiParams stableDiffusionApiParams = new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), "-1");
        String imageBase64String = imageApiUtil.getImageBase64String(stableDiffusionPattern.getSampleimage());
        String sessionHash = UUID.randomUUID().toString();
        StableDiffusionApiResponse resultForSetControlNet = stableDiffusionApiUtil.sendApiToImg(params, stableDiffusionApiParams.toPreDictForControlNet(imageBase64String, sessionHash));
        StableDiffusionApiResponse result = stableDiffusionApiUtil.sendApiToImg(params, stableDiffusionApiParams.toPreDictForSketch(stableDiffusionPattern.getSampleimage(), sessionHash));
        // 添加output数据
        stableDiffusionOutputService.generateOutput(stableDiffusionPattern, result, "SKETCH_IMAGE");
        return result;
    }

    @Override
    public List<StableDiffusionPattern> selectStableDiffusionPatternListByPatternIds(List<Long> ids) {
        return stableDiffusionPatternMapper.selectStableDiffusionPatternListByPatternIds(ids);
    }
}
