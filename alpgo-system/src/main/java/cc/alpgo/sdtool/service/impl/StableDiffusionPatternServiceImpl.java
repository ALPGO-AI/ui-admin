package cc.alpgo.sdtool.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import cc.alpgo.common.core.redis.RedisCache;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.enums.EnvTaskExecutionStatus;
import cc.alpgo.common.event.SdToolAddGenerateByPatternIdEvent;
import cc.alpgo.common.event.SdToolExecuteGenerateByPatternIdEvent;
import cc.alpgo.common.event.UpdateEnvExecutionStatusEvent;
import cc.alpgo.common.event.WebSocketSendMessageEvent;
import cc.alpgo.common.utils.*;
import cc.alpgo.sdtool.constant.ProgressInfoConstant;
import cc.alpgo.sdtool.domain.ControlNetRequestBody;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;
import cc.alpgo.sdtool.util.*;
import cc.alpgo.sdtool.util.request.Txt2txtRequestParams;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.service.IEnvironmentService;
import cc.alpgo.system.service.IImageService;
import cc.alpgo.system.utils.ImageBuilder;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import cc.alpgo.sdtool.mapper.StableDiffusionPatternMapper;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;

import static cc.alpgo.sdtool.util.StableDiffusionApiUtil.generateSessionHash;
import static org.springframework.util.CollectionUtils.isEmpty;

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
    private RedisCache redisCache;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private StableDiffusionPatternMapper stableDiffusionPatternMapper;
    @Autowired
    private StableDiffusionApiUtil stableDiffusionApiUtil;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private IStableDiffusionOutputService stableDiffusionOutputService;
    @Autowired
    private IEnvironmentService environmentService;
    @Autowired
    private IImageService imageService;

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
    public StableDiffusionPattern generateByPatternId(Map<String, String> params, Long patternId, List<Map<String, Object>> extraGenerateParamsList) throws Exception {
        StableDiffusionPattern stableDiffusionPattern = selectStableDiffusionPatternByPatternId(patternId);
        List<CosConfig> cosConfigs = environmentService.getActiveConfigs(params);
        List<StableDiffusionEnv> sdEnvs = environmentService.getActiveEnvs(params);
        if (extraGenerateParamsList == null) {
            extraGenerateParamsList = new ArrayList<>();
            extraGenerateParamsList.add(new HashMap<>());
        }
        for (Map<String, Object> extraGenerateParams : extraGenerateParamsList) {
            for (StableDiffusionEnv sdEnv : sdEnvs) {
                applicationContext.publishEvent(
                        new SdToolAddGenerateByPatternIdEvent(
                                new SdToolExecuteGenerateByPatternIdEvent(
                                        UUID.randomUUID().toString(),
                                        patternId,
                                        cosConfigs,
                                        sdEnv,
                                        params.get("wsid"),
                                        extraGenerateParams),
                                sdEnv.getEnvName(),
                                sdEnv.getUsername() + ":" + stableDiffusionPattern.getPresetTemplate() + "生成" + stableDiffusionPattern.getPatternStyle()
                        )
                );
            }
        }

        return selectStableDiffusionPatternByPatternId(patternId);
    }

    @Override
    public StableDiffusionPattern generateByPatternIdAsync(Long patternId, List<CosConfig> cosConfigs, StableDiffusionEnv sdEnv, String wsId, Map<String, Object> extraGenerateParams) throws Exception {
        sendProgressInfo(wsId, sdEnv, ProgressInfoConstant.START);
        StableDiffusionPattern stableDiffusionPattern = selectStableDiffusionPatternByPatternId(patternId);
        if (stableDiffusionPattern == null) {
            return null;
        }
        String negativePrompt = stableDiffusionPattern.getNegativePrompt();
        String positivePrompt = stableDiffusionPattern.getPositivePrompt();

        String sessionHash = generateSessionHash();
        String parametersJson = stableDiffusionPattern.getParametersJson();
        Map<String, Object> parameters = new Gson().fromJson(parametersJson, HashMap.class);
        // 本次生成参数覆盖
        if (extraGenerateParams != null) {
            Set<String> strings = extraGenerateParams.keySet();
            for (String string : strings) {
                parameters.put(string, extraGenerateParams.get(string));
            }
        }
        Txt2txtRequestParams txt2txtRequestParams = null;
        String switchModelRequestContent = new Txt2txtRequestParams(
                positivePrompt,
                negativePrompt,
                parameters
        ).toPreDictSwitchModel(sessionHash, sdEnv);
        if (StringUtils.isNotEmpty(switchModelRequestContent)) {
            stableDiffusionApiUtil.apiPredict(sdEnv, switchModelRequestContent);
        }
        updateStatus(sdEnv.getEnvKey(), EnvTaskExecutionStatus.Processing);
        if (stableDiffusionApiUtil.isEnableControlNet(parameters)) {
            ControlNetRequestBody controlNetRequestBody = stableDiffusionApiUtil.getControlNetRequestBody(parameters);
            txt2txtRequestParams = new Txt2txtRequestParams(
                    positivePrompt,
                    negativePrompt,
                    parameters,
                    stableDiffusionApiUtil.convertToBase64(controlNetRequestBody.getInputImage(), cosConfigs, sdEnv),
                    controlNetRequestBody.getModule(),
                    controlNetRequestBody.getModel()
            );
            sendProgressInfo(wsId, sdEnv, ProgressInfoConstant.SEND_CONTROL_NET);
            StableDiffusionApiResponse resultForSetControlNet = stableDiffusionApiUtil.apiPredict(
                    sdEnv,
                    txt2txtRequestParams.toPreDictForControlNet(sessionHash,
                            sdEnv,
                            stableDiffusionPattern.getPresetTemplate().equals("img2img"),
                            controlNetRequestBody
                    ));
            log.info("predict controlnet response: {}", resultForSetControlNet);
        } else {
            txt2txtRequestParams = new Txt2txtRequestParams(
                    positivePrompt,
                    negativePrompt,
                    parameters
            );
        }

        StableDiffusionApiResponse result = null;
        sendProgressInfo(wsId, sdEnv, ProgressInfoConstant.SEND_GENERATE);
        if (stableDiffusionPattern.getPresetTemplate().equals("img2img")) {
            Object init_images = parameters.get("init_images");
            if (init_images == null) {
                throw new Exception("请选择图生图初始图片");
            }
            result = stableDiffusionApiUtil.apiPredict(sdEnv,
                    txt2txtRequestParams.toPreDictForImg2img(sessionHash,
                            stableDiffusionApiUtil.convertToBase64((String) init_images, cosConfigs, sdEnv), sdEnv));
        } else {
            result = stableDiffusionApiUtil.apiPredict(sdEnv,
                    txt2txtRequestParams.toPreDict(sessionHash, sdEnv));
        }
        sendProgressInfo(wsId, sdEnv, ProgressInfoConstant.RECEIVE_IMAGE);

        updateStatus(sdEnv.getEnvKey(), EnvTaskExecutionStatus.ImageUploading);
        List<Long> imageIds = transToCosReturnImageIds(patternId, sdEnv, result, cosConfigs, wsId);
        if (isEmpty(imageIds)) {
            throw new Exception("图片生成失败，请检查参数是否正确");
        }
        sendProgressInfo(wsId, sdEnv, ProgressInfoConstant.UPLOAD_TO_COS);
        Map<Long, String> longStringMap = imageService.selectUrls(imageIds, cosConfigs);
        stableDiffusionOutputService.generateOutput(stableDiffusionPattern, new Gson().toJson(longStringMap.values()), "GENERATE_IMAGE", result);
        stableDiffusionPattern.setSampleImage(new Gson().toJson(imageIds));
        stableDiffusionPatternMapper.updateStableDiffusionPattern(stableDiffusionPattern);
        return stableDiffusionPattern;
    }

    private void updateStatus(String envKey, EnvTaskExecutionStatus processing) {
        applicationContext.publishEvent(new UpdateEnvExecutionStatusEvent(envKey, processing));
    }

    private void sendProgressInfo(String wsId, StableDiffusionEnv sdEnv, ProgressInfoConstant progressInfo) {
        applicationContext.publishEvent(new WebSocketSendMessageEvent(wsId, sdEnv.getEnvName(), progressInfo.getMsg()));
    }

    private List<Long> transToCosReturnImageIds(Long patternId, StableDiffusionEnv sdEnv, StableDiffusionApiResponse result, List<CosConfig> cosConfigs, String wsId) throws IOException {
        List<String> fileNames = new ArrayList<>();
        List<Object> data = result.getData();
        if (isEmpty(data)) {
            return new ArrayList<>();
        }
        List<Object> objects = (List<Object>) data.get(0);
        if (isEmpty(objects)) {
            return new ArrayList<>();
        }
        for (Object object : objects) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (map == null) {
                continue;
            }
            Object isFileObj = map.get("is_file");
            Boolean isFile = (Boolean) isFileObj;
            if (isFile != null && isFile) {
                Object fileNameObj = map.get("name");
                String fileName = (String) fileNameObj;
                fileNames.add(fileName);
            }
        }
        List<Image> images = ImageBuilder.build(sdEnv, cosConfigs, fileNames);
        for (Image image : images) {
            imageService.insertImage(image);
        }
        stableDiffusionApiUtil.transToCos(sdEnv, result, cosConfigs, wsId);
        return images.stream().map(Image::getImageId).collect(Collectors.toList());
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
