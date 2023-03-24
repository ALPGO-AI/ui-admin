package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.util.ImageApiUtil;
import cc.alpgo.sdtool.util.StableDiffusionApiParams;
import cc.alpgo.sdtool.util.StableDiffusionApiResponse;
import cc.alpgo.sdtool.util.StableDiffusionApiUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.sdtool.mapper.StableDiffusionOutputMapper;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;

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
    private ImageApiUtil imageApiUtil;
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
        stableDiffusionOutput.setPatternStyle(StringUtils.CVN(pattern.getPatternstyle()));
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
            return new ArrayList<>();
        }
        List<StableDiffusionOutput> collect = stableDiffusionOutputs.stream().map(item -> fillData(item, dataMap)).collect(Collectors.toList());
        return collect;
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
    public StableDiffusionOutput generateOutput(StableDiffusionPattern pattern, StableDiffusionApiResponse result, String type) {
        if (result == null || !result.isIs_generating()) {
            return null;
        }
        StableDiffusionOutput output = new StableDiffusionOutput();
        output.setOutputImageUrl(result.getFileName());
        output.setPatternId(pattern.getPatternId());
        output.setReferenceImageUrl(result.getFileName());
        output.setSeed("-1");
        output.setStraightParameter(result.getData().toString());
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
        String negativeprompt = stableDiffusionPattern.getNegativeprompt();
        String positiveprompt = stableDiffusionPattern.getPositiveprompt();
        String sessionHash = UUID.randomUUID().toString();
        StableDiffusionApiResponse result = stableDiffusionApiUtil.sendApiToImg(params,
                new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), output.getSeed()).toPreDict(sessionHash));
        String fileName = result.getFileName();
        stableDiffusionPattern.setSampleimage(fileName);
        // 添加output数据
        return generateFromOutput(stableDiffusionPattern, output, result, "GENERATE_IMAGE");
    }

    @Override
    public StableDiffusionOutput generateSketchBySampleImgFromOutput(Map<String, String> params, Long outputId) throws IOException {
        StableDiffusionOutput output = selectStableDiffusionOutputByOutputId(outputId);
        if (output == null) {
            return null;
        }
        StableDiffusionPattern stableDiffusionPattern = stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(output.getPatternId());
        if (stableDiffusionPattern == null) {
            return null;
        }
        String negativeprompt = stableDiffusionPattern.getNegativeprompt();
        String positiveprompt = stableDiffusionPattern.getPositiveprompt();
        StableDiffusionApiParams stableDiffusionApiParams = new StableDiffusionApiParams(positiveprompt, negativeprompt, stableDiffusionPattern.getParametersJson(), output.getSeed());
        String imageBase64String = imageApiUtil.getImageBase64String(output.getOutputImageUrl());
        String sessionHash = UUID.randomUUID().toString();
        StableDiffusionApiResponse resultForSetControlNet = stableDiffusionApiUtil.sendApiToImg(params, stableDiffusionApiParams.toPreDictForControlNet(imageBase64String, sessionHash));
        StableDiffusionApiResponse result = stableDiffusionApiUtil.sendApiToImg(params, stableDiffusionApiParams.toPreDictForSketch(stableDiffusionPattern.getSampleimage(), sessionHash));
        // 添加output数据
        return generateFromOutput(stableDiffusionPattern, output, result, "SKETCH_IMAGE");
    }

    private StableDiffusionOutput generateFromOutput(StableDiffusionPattern stableDiffusionPattern, StableDiffusionOutput output, StableDiffusionApiResponse result, String type) {
        if (result == null || !result.isIs_generating()) {
            return null;
        }
        StableDiffusionOutput outputToDb = new StableDiffusionOutput();
        outputToDb.setOutputImageUrl(result.getFileName());
        outputToDb.setPatternId(stableDiffusionPattern.getPatternId());
        outputToDb.setReferenceImageUrl(result.getFileName());
        outputToDb.setSeed(output.getSeed());
        outputToDb.setStraightParameter(result.getData().toString());
        outputToDb.setType(type);
        outputToDb.setReferenceOuputId(output.getReferenceOuputId());
        insertStableDiffusionOutput(outputToDb);
        return outputToDb;
    }
}
