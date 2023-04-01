package cc.alpgo.system.mapper;

import java.util.List;
import cc.alpgo.system.domain.Environment;
import cc.alpgo.system.domain.EnvironmentParameters;

/**
 * environmentsMapper接口
 * 
 * @author asahiluna
 * @date 2023-03-30
 */
public interface EnvironmentMapper 
{
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
     * 删除environments
     * 
     * @param environmentId environments主键
     * @return 结果
     */
    public int deleteEnvironmentByEnvironmentId(Long environmentId);

    /**
     * 批量删除environments
     * 
     * @param environmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnvironmentByEnvironmentIds(Long[] environmentIds);

    /**
     * 批量删除environment_parameters
     * 
     * @param environmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnvironmentParametersByEnvironmentIds(Long[] environmentIds);
    
    /**
     * 批量新增environment_parameters
     * 
     * @param environmentParametersList environment_parameters列表
     * @return 结果
     */
    public int batchEnvironmentParameters(List<EnvironmentParameters> environmentParametersList);
    

    /**
     * 通过environments主键删除environment_parameters信息
     * 
     * @param environmentId environmentsID
     * @return 结果
     */
    public int deleteEnvironmentParametersByEnvironmentId(Long environmentId);

    List<Environment> selectEnvironmentListByEnvironmentIds(List<Long> envIds);
}
