package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;
import cc.alpgo.sdtool.util.ImageApiUtil;
import cc.alpgo.sdtool.util.StableDiffusionApiParams;
import cc.alpgo.sdtool.util.StableDiffusionApiResponse;
import cc.alpgo.sdtool.util.StableDiffusionApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.sdtool.mapper.StableDiffusionPatternMapper;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;

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
        String negativeprompt = stableDiffusionPattern.getNegativePrompt();
        String positiveprompt = stableDiffusionPattern.getPositivePrompt();
        String sessionHash = UUID.randomUUID().toString();
        StableDiffusionApiResponse result = stableDiffusionApiUtil.sendApiToImg(params, new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), "-1").toPreDict(sessionHash));
        String fileName = result.getFileName();
        stableDiffusionPattern.setSampleImage(fileName);
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
        if (stableDiffusionPattern.getSampleImage() == null) {
            return null;
        }
        String negativeprompt = stableDiffusionPattern.getNegativePrompt();
        String positiveprompt = stableDiffusionPattern.getPositivePrompt();
        StableDiffusionApiParams stableDiffusionApiParams = new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), "-1");
        String imageBase64String = imageApiUtil.getImageBase64String(stableDiffusionPattern.getSampleImage());
        String sessionHash = UUID.randomUUID().toString();
        StableDiffusionApiResponse resultForSetControlNet = stableDiffusionApiUtil.sendApiToImg(params, stableDiffusionApiParams.toPreDictForControlNet(imageBase64String, sessionHash));
        StableDiffusionApiResponse result = stableDiffusionApiUtil.sendApiToImg(params, stableDiffusionApiParams.toPreDictForSketch(stableDiffusionPattern.getSampleImage(), sessionHash));
        // 添加output数据
        stableDiffusionOutputService.generateOutput(stableDiffusionPattern, result, "SKETCH_IMAGE");
        return result;
    }

    @Override
    public List<StableDiffusionPattern> selectStableDiffusionPatternListByPatternIds(List<Long> ids) {
        return stableDiffusionPatternMapper.selectStableDiffusionPatternListByPatternIds(ids);
    }
}
