package cc.alpgo.system.service;

import java.util.List;
import cc.alpgo.system.domain.EnvironmentParameters;

/**
 * environment_parametersService接口
 * 
 * @author asahiluna
 * @date 2023-03-30
 */
public interface IEnvironmentParametersService 
{
    /**
     * 查询environment_parameters
     * 
     * @param parameterId environment_parameters主键
     * @return environment_parameters
     */
    public EnvironmentParameters selectEnvironmentParametersByParameterId(Long parameterId);

    /**
     * 查询environment_parameters列表
     * 
     * @param environmentParameters environment_parameters
     * @return environment_parameters集合
     */
    public List<EnvironmentParameters> selectEnvironmentParametersList(EnvironmentParameters environmentParameters);

    /**
     * 新增environment_parameters
     * 
     * @param environmentParameters environment_parameters
     * @return 结果
     */
    public int insertEnvironmentParameters(EnvironmentParameters environmentParameters);

    /**
     * 修改environment_parameters
     * 
     * @param environmentParameters environment_parameters
     * @return 结果
     */
    public int updateEnvironmentParameters(EnvironmentParameters environmentParameters);

    /**
     * 批量删除environment_parameters
     * 
     * @param parameterIds 需要删除的environment_parameters主键集合
     * @return 结果
     */
    public int deleteEnvironmentParametersByParameterIds(Long[] parameterIds);

    /**
     * 删除environment_parameters信息
     * 
     * @param parameterId environment_parameters主键
     * @return 结果
     */
    public int deleteEnvironmentParametersByParameterId(Long parameterId);
}
