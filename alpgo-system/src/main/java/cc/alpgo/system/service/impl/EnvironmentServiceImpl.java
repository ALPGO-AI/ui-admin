package cc.alpgo.system.service.impl;

import java.util.HashMap;
import java.util.List;

import cc.alpgo.common.core.redis.RedisCache;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.sdtool.util.StableDiffusionApiUtil;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import cc.alpgo.system.domain.request.ModelListRequestParams;
import cc.alpgo.system.service.IEnvironmentService;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import cc.alpgo.system.domain.EnvironmentParameters;
import cc.alpgo.system.mapper.EnvironmentMapper;
import cc.alpgo.system.domain.Environment;

import static cc.alpgo.sdtool.constant.ApiContants.ACTIVE_TENCENT_COS;
import static cc.alpgo.sdtool.constant.ApiContants.ACTIVE_WEBUI_ENVS;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * environmentsService业务层处理
 *
 * @author asahiluna
 * @date 2023-03-30
 */
@Service
public class EnvironmentServiceImpl implements IEnvironmentService
{
    @Autowired
    private StableDiffusionApiUtil stableDiffusionApiUtil;
    @Autowired
    private EnvironmentMapper environmentMapper;
    @Autowired
    private RedisCache redisCache;

    private List<Long> convertToIds(String listString) {
        if (StringUtils.isEmpty(listString)) {
            return new ArrayList<>();
        }

        List<Long> result = new ArrayList<>();
        for (String str : listString.split(",")) {
            result.add(Long.parseLong(str));
        }
        return result;
    }
    @Override
    public List<CosConfig> getActiveConfigs(Map<String, String> params) {
        List<Long> envIds = convertToIds(params.get(ACTIVE_TENCENT_COS));
        if (isEmpty(envIds)) {
            return new ArrayList<>();
        }
        List<Environment> environments = selectEnvironmentListByEnvironmentIds(envIds);
        return convertToCosConfigs(environments);
    }

    private List<CosConfig> convertToCosConfigs(List<Environment> environments) {
        if (isEmpty(environments)) {
            return new ArrayList<>();
        }
        return environments.stream().map(this::convertEnvToCosConfig).collect(Collectors.toList());
    }

    private CosConfig convertEnvToCosConfig(Environment environment) {
        Map<String, String> map = new HashMap<>();
        for (EnvironmentParameters param : environment.getEnvironmentParametersList()) {
            map.put(param.getParamName(), param.getParamValue());
        }
        return new CosConfig(
                environment.getName(),
                environment.getEnvironmentId(),
                map.get("cosApiSecretId"),
                map.get("cosApiSecretKey"),
                map.get("cosApiRegion"),
                map.get("cosApiBucketName"));
    }


    @Override
    public List<StableDiffusionEnv> getActiveEnvs(Map<String, String> params) {
        List<Long> envIds = convertToIds(params.get(ACTIVE_WEBUI_ENVS));
        List<Environment> environments = selectEnvironmentListByEnvironmentIds(envIds);
        return convertToStableDiffusionEnvs(environments);
    }
    private List<StableDiffusionEnv> convertToStableDiffusionEnvs(List<Environment> environments) {
        if (isEmpty(environments)) {
            return new ArrayList<>();
        }
        return environments.stream().map(this::convertEnvToStableDiffusionEnv).collect(Collectors.toList());
    }

    private StableDiffusionEnv convertEnvToStableDiffusionEnv(Environment environment) {
        Map<String, String> map = new HashMap<>();
        for (EnvironmentParameters param : environment.getEnvironmentParametersList()) {
            map.put(param.getParamName(), param.getParamValue());
        }
        return new StableDiffusionEnv(
                environment.getName(),
                environment.getEnvironmentId(),
                map.get("domain"),
                map.get("username"),
                map.get("password"),
                Integer.parseInt(map.get("txt2imgFnIndex")),
                Integer.parseInt(map.get("img2imgFnIndex")),
                Integer.parseInt(map.get("txt2imgControlNetFnIndex")),
                Integer.parseInt(map.get("img2imgControlNetFnIndex")),
                Boolean.valueOf(map.get("isLoraPluginInstalled")),
                Integer.parseInt(map.get("switchModelFnIndex")),
                Boolean.valueOf(map.get("isUltimateUpscalePluginInstalled"))
        );
    }
    /**
     * 查询environments
     *
     * @param environmentId environments主键
     * @return environments
     */
    @Override
    public Environment selectEnvironmentByEnvironmentId(Long environmentId)
    {
        return environmentMapper.selectEnvironmentByEnvironmentId(environmentId);
    }

    /**
     * 查询environments列表
     *
     * @param environment environments
     * @return environments
     */
    @Override
    public List<Environment> selectEnvironmentList(Environment environment)
    {
        return environmentMapper.selectEnvironmentList(environment);
    }

    @Override
    public List<Environment> selectEnvironmentListByEnvironmentIds(List<Long> envIds) {
        return environmentMapper.selectEnvironmentListByEnvironmentIds(envIds);
    }

    /**
     * 新增environments
     *
     * @param environment environments
     * @return 结果
     */
    @Transactional
    @Override
    public int insertEnvironment(Environment environment)
    {
        environment.setCreateTime(DateUtils.getNowDate());
        int rows = environmentMapper.insertEnvironment(environment);
        insertEnvironmentParameters(environment);
        return rows;
    }

    /**
     * 修改environments
     *
     * @param environment environments
     * @return 结果
     */
    @Transactional
    @Override
    public int updateEnvironment(Environment environment)
    {
        environment.setUpdateTime(DateUtils.getNowDate());
        environmentMapper.deleteEnvironmentParametersByEnvironmentId(environment.getEnvironmentId());
        insertEnvironmentParameters(environment);
        return environmentMapper.updateEnvironment(environment);
    }

    /**
     * 批量删除environments
     *
     * @param environmentIds 需要删除的environments主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEnvironmentByEnvironmentIds(Long[] environmentIds)
    {
        environmentMapper.deleteEnvironmentParametersByEnvironmentIds(environmentIds);
        return environmentMapper.deleteEnvironmentByEnvironmentIds(environmentIds);
    }

    /**
     * 删除environments信息
     *
     * @param environmentId environments主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEnvironmentByEnvironmentId(Long environmentId)
    {
        environmentMapper.deleteEnvironmentParametersByEnvironmentId(environmentId);
        return environmentMapper.deleteEnvironmentByEnvironmentId(environmentId);
    }

    @Override
    public Map<String, List<String>> webuiModelOptions(Map<String, String> headerMap, Boolean refresh) throws Exception {
        List<StableDiffusionEnv> activeEnvs = getActiveEnvs(headerMap);
        if (!refresh) {
            Map<String, List<String>> webuiModelOptions = new HashMap<>();
            for (StableDiffusionEnv activeEnv : activeEnvs) {
                webuiModelOptions.put(activeEnv.getEnvId().toString(), redisCache.getCacheList("webuiModelOptions"+activeEnv.getEnvId().toString()));
            }
            if (webuiModelOptions != null) {
                return webuiModelOptions;
            }
        }
        Map<String, List<String>> result = new HashMap<>();
        for (StableDiffusionEnv activeEnv : activeEnvs) {
            StableDiffusionApiResponse res = stableDiffusionApiUtil.apiPredict(activeEnv, new ModelListRequestParams(activeEnv).toPreDict());
            for (Object datum : res.getData()) {
                if (datum instanceof LinkedTreeMap) {
                    Map datumMap = (LinkedTreeMap) datum;
                    Object choices = datumMap.get("choices");
                    if (choices instanceof List) {
                        ArrayList choicesList = (ArrayList) choices;
                        result.put(activeEnv.getEnvId().toString(), choicesList);
                        redisCache.setCacheList("webuiModelOptions"+activeEnv.getEnvId().toString(), choicesList);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 新增environment_parameters信息
     *
     * @param environment environments对象
     */
    public void insertEnvironmentParameters(Environment environment)
    {
        List<EnvironmentParameters> environmentParametersList = environment.getEnvironmentParametersList();
        Long environmentId = environment.getEnvironmentId();
        if (StringUtils.isNotNull(environmentParametersList))
        {
            List<EnvironmentParameters> list = new ArrayList<EnvironmentParameters>();
            for (EnvironmentParameters environmentParameters : environmentParametersList)
            {
                environmentParameters.setEnvironmentId(environmentId);
                list.add(environmentParameters);
            }
            if (list.size() > 0)
            {
                environmentMapper.batchEnvironmentParameters(list);
            }
        }
    }
}
