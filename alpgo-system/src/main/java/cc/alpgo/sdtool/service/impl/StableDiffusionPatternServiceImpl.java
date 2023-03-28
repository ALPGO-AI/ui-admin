package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.sdtool.domain.ControlNetRequestBody;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;
import cc.alpgo.sdtool.util.*;
import cc.alpgo.sdtool.util.request.Txt2txtRequestParams;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.sdtool.mapper.StableDiffusionPatternMapper;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import org.springframework.util.CollectionUtils;

import static cc.alpgo.sdtool.util.StableDiffusionApiUtil.generateSessionHash;

/**
 * stable_diffusion_patternService业务层处理
 *
 * @author marcus
 * @date 2023-03-21
 */
@Service
public class StableDiffusionPatternServiceImpl implements IStableDiffusionPatternService
{
    private static final Logger log = LoggerFactory.getLogger(StableDiffusionPatternServiceImpl.class);
    @Autowired
    private StableDiffusionPatternMapper stableDiffusionPatternMapper;
    @Autowired
    private StableDiffusionApiUtil stableDiffusionApiUtil;
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
    public StableDiffusionPattern generateByPatternId(Map<String, String> params, Long patternId) throws Exception {
        StableDiffusionPattern stableDiffusionPattern = selectStableDiffusionPatternByPatternId(patternId);
        if (stableDiffusionPattern == null) {
            return null;
        }
        String negativePrompt = stableDiffusionPattern.getNegativePrompt();
        String positivePrompt = stableDiffusionPattern.getPositivePrompt();

        String sessionHash = generateSessionHash();
        String parametersJson = stableDiffusionPattern.getParametersJson();
        Map<String, Object> parameters = new Gson().fromJson(parametersJson, HashMap.class);
        Txt2txtRequestParams txt2txtRequestParams = null;
        if (stableDiffusionApiUtil.isEnableControlNet(parameters)) {
            ControlNetRequestBody controlNetRequestBody = stableDiffusionApiUtil.getControlNetRequestBody(parameters);
            txt2txtRequestParams = new Txt2txtRequestParams(
                    positivePrompt,
                    negativePrompt,
                    stableDiffusionPattern.getParametersJson(),
                    "-1",
                    stableDiffusionApiUtil.convertToBase64(controlNetRequestBody.getInputImage()),
                    controlNetRequestBody.getModule(),
                    controlNetRequestBody.getModel()
            );
            StableDiffusionApiResponse resultForSetControlNet = stableDiffusionApiUtil.apiPredict(
                    params,
                    txt2txtRequestParams.toPreDictForControlNet(sessionHash,
                            params,
                            stableDiffusionPattern.getPresetTemplate().equals("img2img")));
            log.info("predict controlnet response: {}", resultForSetControlNet);
        } else {
            txt2txtRequestParams = new Txt2txtRequestParams(
                    positivePrompt,
                    negativePrompt,
                    stableDiffusionPattern.getParametersJson(),
                    "-1"
            );
        }

        StableDiffusionApiResponse result = null;
        if (stableDiffusionPattern.getPresetTemplate().equals("img2img")) {
            Object init_images = parameters.get("init_images");
            if (init_images == null) {
                throw new Exception("请选择图生图初始图片");
            }
            result = stableDiffusionApiUtil.apiPredict(params,
                    txt2txtRequestParams.toPreDictForImg2img(sessionHash,
                            stableDiffusionApiUtil.convertToBase64((String) init_images),
                            params));
        } else {
            result = stableDiffusionApiUtil.apiPredict(params,
                    txt2txtRequestParams.toPreDict(sessionHash, params));
        }
        List<String> imageUrls = stableDiffusionApiUtil.transToCos(params, result);
        if (CollectionUtils.isEmpty(imageUrls)) {
            throw new Exception("图片生成失败，请检查参数是否正确");
        }
        stableDiffusionOutputService.generateOutput(stableDiffusionPattern, new Gson().toJson(imageUrls), "GENERATE_IMAGE", result);
        List<String> imageUrlsFromDb = selectAllRelatedOutputImageUrls(patternId);
        stableDiffusionPattern.setSampleImage(new Gson().toJson(imageUrlsFromDb));
        stableDiffusionPatternMapper.updateStableDiffusionPattern(stableDiffusionPattern);
        return stableDiffusionPattern;
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
    public StableDiffusionApiResponse generateByImg(Map<String, String> params, Long patternId) throws IOException {

        return null;
    }

    @Override
    public List<StableDiffusionPattern> selectStableDiffusionPatternListByPatternIds(List<Long> ids) {
        return stableDiffusionPatternMapper.selectStableDiffusionPatternListByPatternIds(ids);
    }
}
