package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.sdtool.domain.ControlNetRequestBody;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.util.*;
import cc.alpgo.sdtool.util.request.Txt2txtRequestParams;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import com.github.pagehelper.Page;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.sdtool.mapper.StableDiffusionOutputMapper;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;

import static cc.alpgo.sdtool.constant.ApiContants.maskImg;
import static cc.alpgo.sdtool.util.StableDiffusionApiUtil.generateSessionHash;

/**
 * stable_diffusion_outputService业务层处理
 *
 * @author marcus
 * @date 2023-03-23
 */
@Service
public class StableDiffusionOutputServiceImpl implements IStableDiffusionOutputService
{
    private static final Logger log = LoggerFactory.getLogger(StableDiffusionOutputServiceImpl.class);
    @Autowired
    private StableDiffusionApiUtil stableDiffusionApiUtil;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private StableDiffusionOutputMapper stableDiffusionOutputMapper;
    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;
    @Autowired
    private AlpgoConfig alpgoConfig;

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
    public StableDiffusionOutput generateOutput(StableDiffusionPattern pattern, String imageUrl, String type, StableDiffusionApiResponse params) {
        StableDiffusionOutput output = new StableDiffusionOutput();
        output.setOutputImageUrl(imageUrl);
        output.setPatternId(pattern.getPatternId());
        output.setReferenceImageUrl(imageUrl);
        Map<String, Object> map = new Gson().fromJson(pattern.getParametersJson(), HashMap.class);
        output.setSeed((String) map.get("seed"));
        output.setStraightParameter(new Gson().toJson(params));
        output.setType(type);
        output.setReferenceOuputId(-1l);
        insertStableDiffusionOutput(output);
        return output;
    }

    @Override
    public StableDiffusionPattern generateByPatternId(Map<String, String> params, Long outputId) throws Exception {
        StableDiffusionOutput output = selectStableDiffusionOutputByOutputId(outputId);
        if (output == null) {
            return null;
        }
        StableDiffusionPattern stableDiffusionPattern = stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(output.getPatternId());
        if (stableDiffusionPattern == null) {
            return null;
        }
        return stableDiffusionPatternService.generateByPatternId(params, stableDiffusionPattern.getPatternId(), null);
    }

    @Override
    public StableDiffusionPattern generateByImgFromOutput(Map<String, String> params, Long outputId, ControlNetRequestBody controlNetRequestBody) throws Exception {
        StableDiffusionOutput output = selectStableDiffusionOutputByOutputId(outputId);
        if (output == null) {
            return null;
        }
        StableDiffusionPattern stableDiffusionPattern = stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(output.getPatternId());
        if (stableDiffusionPattern == null) {
            return null;
        }
        return stableDiffusionPatternService.generateByPatternId(params, stableDiffusionPattern.getPatternId(), null);
    }


    private StableDiffusionOutput generateFromOutput(
            StableDiffusionPattern stableDiffusionPattern,
            StableDiffusionOutput output,
            String imageUrl,
            String type,
            StableDiffusionApiResponse params) {
        StableDiffusionOutput outputToDb = new StableDiffusionOutput();
        outputToDb.setOutputImageUrl(imageUrl);
        outputToDb.setPatternId(stableDiffusionPattern.getPatternId());
        outputToDb.setReferenceImageUrl(imageUrl);
        outputToDb.setSeed(output.getSeed());
        outputToDb.setStraightParameter(new Gson().toJson(params));
        outputToDb.setType(type);
        outputToDb.setReferenceOuputId(output.getReferenceOuputId());
        insertStableDiffusionOutput(outputToDb);
        return outputToDb;
    }
}
