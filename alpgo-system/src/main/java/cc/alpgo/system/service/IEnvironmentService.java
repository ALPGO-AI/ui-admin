package cc.alpgo.system.service;

import java.util.List;
import java.util.Map;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.system.domain.Environment;

/**
 * environmentsService接口
 * 
 * @author asahiluna
 * @date 2023-03-30
 */
public interface IEnvironmentService 
{
    List<CosConfig> getActiveConfigs(Map<String, String> params);

    List<StableDiffusionEnv> getActiveEnvs(Map<String, String> params);

    /**
     * 查询environments
     * 
     * @param environmentId environments主键
     * @return environments
     */
    public Environment selectEnvironmentByEnvironmentId(Long environmentId);

    /**
     * 查询environments列表
     * 
     * @param environment environments
     * @return environments集合
     */
    public List<Environment> selectEnvironmentList(Environment environment);
    public List<Environment> selectEnvironmentListByEnvironmentIds(List<Long> envIds);

    /**
     * 新增environments
     * 
     * @param environment environments
     * @return 结果
     */
    public int insertEnvironment(Environment environment);

    /**
     * 修改environments
     * 
     * @param environment environments
     * @return 结果
     */
    public int updateEnvironment(Environment environment);

    /**
     * 批量删除environments
     * 
     * @param environmentIds 需要删除的environments主键集合
     * @return 结果
     */
    public int deleteEnvironmentByEnvironmentIds(Long[] environmentIds);

    /**
     * 删除environments信息
     * 
     * @param environmentId environments主键
     * @return 结果
     */
    public int deleteEnvironmentByEnvironmentId(Long environmentId);

    Map<String, List<String>> webuiModelOptions(Map<String, String> headerMap, Boolean refresh) throws Exception;
}
