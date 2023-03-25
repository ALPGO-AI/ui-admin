package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;
import cc.alpgo.sdtool.util.*;
import com.google.gson.Gson;
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
    private CosUtil cosUtil;
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
        StableDiffusionApiResponse result = stableDiffusionApiUtil.txt2img(params, new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), "-1").toRequestBody());
        List<String> images = result.getImages();
        List<String> imageUrls = cosUtil.uploadAsync(images);
        result.setImages(imageUrls);
        stableDiffusionOutputService.generateOutput(stableDiffusionPattern, new Gson().toJson(imageUrls), "GENERATE_IMAGE", result.getParameters());
        List<String> imageUrlsFromDb = selectAllRelatedOutputImageUrls(patternId);
        stableDiffusionPattern.setSampleImage(new Gson().toJson(imageUrlsFromDb));
        result.setPatternImages(imageUrlsFromDb);
        stableDiffusionPatternMapper.updateStableDiffusionPattern(stableDiffusionPattern);
        return result;
    }

    @Override
    public List<String> selectAllRelatedOutputImageUrls(Long patternId) {
        List<String> result = new ArrayList<>();
        StableDiffusionOutput searchByPatternId = new StableDiffusionOutput();
        searchByPatternId.setPatternId(patternId);
        List<StableDiffusionOutput> stableDiffusionOutputs = stableDiffusionOutputService.selectStableDiffusionOutputList(searchByPatternId);
        stableDiffusionOutputs = stableDiffusionOutputs.stream().sorted(Comparator.comparing(StableDiffusionOutput::getCreateTime).reversed()).collect(Collectors.toList());
        for (StableDiffusionOutput stableDiffusionOutput : stableDiffusionOutputs) {
            String outputImageUrl = stableDiffusionOutput.getOutputImageUrl();
            List list = new Gson().fromJson(outputImageUrl, List.class);
            result.addAll(list);
        }
        return result;
    }

    @Override
    public StableDiffusionApiResponse generateSketchBySampleImg(Map<String, String> params, Long patternId) throws IOException {

        return null;
    }

    @Override
    public List<StableDiffusionPattern> selectStableDiffusionPatternListByPatternIds(List<Long> ids) {
        return stableDiffusionPatternMapper.selectStableDiffusionPatternListByPatternIds(ids);
    }
}
