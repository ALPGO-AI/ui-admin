package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.util.*;
import cc.alpgo.sdtool.util.request.Img2imgRequestParams;
import cc.alpgo.sdtool.util.request.Txt2txtControlNetRequestParams;
import cc.alpgo.sdtool.util.request.Txt2txtRequestParams;
import com.github.pagehelper.Page;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.sdtool.mapper.StableDiffusionOutputMapper;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;

import static cc.alpgo.sdtool.constant.ApiContants.maskImg;

/**
 * stable_diffusion_outputService业务层处理
 *
 * @author marcus
 * @date 2023-03-23
 */
@Service
public class StableDiffusionOutputServiceImpl implements IStableDiffusionOutputService
{
    @Autowired
    private StableDiffusionApiUtil stableDiffusionApiUtil;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private StableDiffusionOutputMapper stableDiffusionOutputMapper;
    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;

    /**
     * 查询stable_diffusion_output
     *
     * @param outputId stable_diffusion_output主键
     * @return stable_diffusion_output
     */
    @Override
    public StableDiffusionOutput selectStableDiffusionOutputByOutputId(Long outputId)
    {
        return stableDiffusionOutputMapper.selectStableDiffusionOutputByOutputId(outputId);
    }

    /**
     * 查询stable_diffusion_output列表
     *
     * @param stableDiffusionOutput stable_diffusion_output
     * @return stable_diffusion_output
     */
    @Override
    public List<StableDiffusionOutput> selectStableDiffusionOutputList(StableDiffusionOutput stableDiffusionOutput)
    {
        List<StableDiffusionOutput> stableDiffusionOutputs = stableDiffusionOutputMapper.selectStableDiffusionOutputList(stableDiffusionOutput);
        Map<String, Object> dataMap = selectDataMap(stableDiffusionOutputs);
        return fillData(stableDiffusionOutputs, dataMap);
    }

    private StableDiffusionOutput fillData(StableDiffusionOutput stableDiffusionOutput, Map<String, Object> dataMap) {
        StableDiffusionPattern pattern = (StableDiffusionPattern) getDataFromDataMap(
                getDataKey(
                        StableDiffusionPattern.class.getName(),
                        stableDiffusionOutput.getPatternId()
                ), dataMap
        );
        if (pattern == null) {
            return stableDiffusionOutput;
        }
        stableDiffusionOutput.setPatternStyle(StringUtils.CVN(pattern.getPatternStyle()));
        return stableDiffusionOutput;
    }

    private Object getDataFromDataMap(String dataKey, Map<String, Object> dataMap) {
        return dataMap.get(dataKey);
    }

    private String getDataKey(String name, Long patternId) {
        return name + "_" + patternId;
    }

    private List<StableDiffusionOutput> fillData(List<StableDiffusionOutput> stableDiffusionOutputs, Map<String, Object> dataMap) {
        if (CollectionUtils.isEmpty(stableDiffusionOutputs)) {
            return new Page<>();
        }
        for (StableDiffusionOutput stableDiffusionOutput : stableDiffusionOutputs) {
            fillData(stableDiffusionOutput, dataMap);
        }

        return stableDiffusionOutputs;
    }

    private Map<String, Object> selectDataMap(List<StableDiffusionOutput> stableDiffusionOutputs) {
        if (CollectionUtils.isEmpty(stableDiffusionOutputs)) {
            return new HashMap<>();
        }
        List<Long> collect = stableDiffusionOutputs.stream().map(StableDiffusionOutput::getPatternId).collect(Collectors.toList());
        List<StableDiffusionPattern> stableDiffusionPatterns = stableDiffusionPatternService.selectStableDiffusionPatternListByPatternIds(collect);
        if (CollectionUtils.isEmpty(stableDiffusionPatterns)) {
            return new HashMap<>();
        }
        return stableDiffusionPatterns.stream().collect(Collectors.toMap(item -> getDataKey(StableDiffusionPattern.class.getName(), item.getPatternId()), item -> item));
    }

    /**
     * 新增stable_diffusion_output
     *
     * @param stableDiffusionOutput stable_diffusion_output
     * @return 结果
     */
    @Override
    public int insertStableDiffusionOutput(StableDiffusionOutput stableDiffusionOutput)
    {
        stableDiffusionOutput.setCreateTime(DateUtils.getNowDate());
        return stableDiffusionOutputMapper.insertStableDiffusionOutput(stableDiffusionOutput);
    }

    /**
     * 修改stable_diffusion_output
     *
     * @param stableDiffusionOutput stable_diffusion_output
     * @return 结果
     */
    @Override
    public int updateStableDiffusionOutput(StableDiffusionOutput stableDiffusionOutput)
    {
        stableDiffusionOutput.setUpdateTime(DateUtils.getNowDate());
        return stableDiffusionOutputMapper.updateStableDiffusionOutput(stableDiffusionOutput);
    }

    /**
     * 批量删除stable_diffusion_output
     *
     * @param outputIds 需要删除的stable_diffusion_output主键
     * @return 结果
     */
    @Override
    public int deleteStableDiffusionOutputByOutputIds(Long[] outputIds)
    {
        return stableDiffusionOutputMapper.deleteStableDiffusionOutputByOutputIds(outputIds);
    }

    /**
     * 删除stable_diffusion_output信息
     *
     * @param outputId stable_diffusion_output主键
     * @return 结果
     */
    @Override
    public int deleteStableDiffusionOutputByOutputId(Long outputId)
    {
        return stableDiffusionOutputMapper.deleteStableDiffusionOutputByOutputId(outputId);
    }

    @Override
    public StableDiffusionOutput generateOutput(StableDiffusionPattern pattern, String imageUrl, String type, Map<String, Object> params) {
        StableDiffusionOutput output = new StableDiffusionOutput();
        output.setOutputImageUrl(imageUrl);
        output.setPatternId(pattern.getPatternId());
        output.setReferenceImageUrl(imageUrl);
        output.setSeed("-1");
        output.setStraightParameter(new Gson().toJson(params));
        output.setType(type);
        output.setReferenceOuputId(-1l);
        insertStableDiffusionOutput(output);
        return output;
    }

    @Override
    public StableDiffusionOutput generateByPatternId(Map<String, String> params, Long outputId) throws IOException {
        StableDiffusionOutput output = selectStableDiffusionOutputByOutputId(outputId);
        if (output == null) {
            return null;
        }
        StableDiffusionPattern stableDiffusionPattern = stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(output.getPatternId());
        if (stableDiffusionPattern == null) {
            return null;
        }
        String negativePrompt = stableDiffusionPattern.getNegativePrompt();
        String positivePrompt = stableDiffusionPattern.getPositivePrompt();
        StableDiffusionApiResponse result = stableDiffusionApiUtil.txt2img(params,
                new Txt2txtRequestParams(positivePrompt, negativePrompt, stableDiffusionPattern.getParametersJson(), output.getSeed()).toRequestBody());
        List<String> imageUrls = cosUtil.uploadAsync(result.getImages());
        result.setImages(imageUrls);
        List<String> imageUrlsFromDb = stableDiffusionPatternService.selectAllRelatedOutputImageUrls(stableDiffusionPattern.getPatternId());
        imageUrlsFromDb.addAll(imageUrls);
        stableDiffusionPattern.setSampleImage(new Gson().toJson(imageUrlsFromDb));
        stableDiffusionPatternService.updateStableDiffusionPattern(stableDiffusionPattern);
        // 添加output数据
        return generateFromOutput(stableDiffusionPattern, output, new Gson().toJson(imageUrls), "GENERATE_IMAGE", result.getParameters(), new ArrayList<>());
    }

    @Override
    public StableDiffusionOutput generateByImgFromOutput(Map<String, String> params, Long outputId) throws IOException {
        StableDiffusionOutput output = selectStableDiffusionOutputByOutputId(outputId);
        if (output == null) {
            return null;
        }
        StableDiffusionPattern stableDiffusionPattern = stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(output.getPatternId());
        if (stableDiffusionPattern == null) {
            return null;
        }
        String negativePrompt = stableDiffusionPattern.getNegativePrompt();
        String positivePrompt = stableDiffusionPattern.getPositivePrompt();
        String straightParameter = output.getStraightParameter();
        Map<String, Object> straightParameterMap = new Gson().fromJson(straightParameter, HashMap.class);
        List<String> initImgUrls = new Gson().fromJson(output.getOutputImageUrl(), List.class);
        fillImg2imgData(straightParameterMap, initImgUrls);
        StableDiffusionApiResponse result = stableDiffusionApiUtil.txt2img(params,
                new Txt2txtControlNetRequestParams(
                        positivePrompt,
                        negativePrompt,
                        stableDiffusionPattern.getParametersJson(),
                        "-1",
                        ((List<String>) straightParameterMap.get("init_images")).get(0)
                ).toRequestBody());
        List<String> imageUrls = cosUtil.uploadAsync(result.getImages());
        result.setImages(imageUrls);
        List<String> imageUrlsFromDb = stableDiffusionPatternService.selectAllRelatedOutputImageUrls(stableDiffusionPattern.getPatternId());
        imageUrlsFromDb.addAll(imageUrls);
        stableDiffusionPattern.setSampleImage(new Gson().toJson(imageUrlsFromDb));
        stableDiffusionPatternService.updateStableDiffusionPattern(stableDiffusionPattern);
        // 添加output数据
        return generateFromOutput(stableDiffusionPattern, output, new Gson().toJson(imageUrls), "GENERATE_IMAGE", result.getParameters(), initImgUrls);
    }

    private void fillImg2imgData(Map<String, Object> straightParameterMap, List<String> imageUrls) throws IOException {
        straightParameterMap.put("image_cfg_scale", straightParameterMap.get("cfg_scale"));
        straightParameterMap.put("resize_mode", 0.0);
        straightParameterMap.put("include_init_images", true);
        straightParameterMap.put("initial_noise_multiplier", 0.0);
        straightParameterMap.put("inpaint_full_res", false);
        straightParameterMap.put("inpaint_full_res_padding", 0.0);
        straightParameterMap.put("inpainting_fill", 0.0);
        straightParameterMap.put("inpainting_mask_invert", 0.0);
        straightParameterMap.put("init_images", convertToBase64(imageUrls));
        straightParameterMap.put("mask", maskImg);
        straightParameterMap.put("mask_blur", 4.0);
    }

    private List<String> convertToBase64(List<String> imageUrls) throws IOException {
        List<String> result = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            String imageBase64String = cosUtil.downloadToBase64(imageUrl);
            result.add(imageBase64String);
        }
        return result;
    }

    private String convertToBase64(String imageUrl) throws IOException {
        if (StringUtils.isEmpty(imageUrl)) {
            return null;
        }
        return cosUtil.downloadToBase64(imageUrl);
    }
    private StableDiffusionOutput generateFromOutput(StableDiffusionPattern stableDiffusionPattern, StableDiffusionOutput output, String imageUrl, String type, Map<String, Object> params, List<String> initImgUrls) {
        StableDiffusionOutput outputToDb = new StableDiffusionOutput();
        outputToDb.setOutputImageUrl(imageUrl);
        outputToDb.setPatternId(stableDiffusionPattern.getPatternId());
        outputToDb.setReferenceImageUrl(imageUrl);
        outputToDb.setSeed(output.getSeed());
        params.put("init_images", null);
        params.put("mask", null);
        outputToDb.setStraightParameter(new Gson().toJson(params));
        outputToDb.setType(type);
        outputToDb.setReferenceOuputId(output.getReferenceOuputId());
        insertStableDiffusionOutput(outputToDb);
        return outputToDb;
    }
}
