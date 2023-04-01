package cc.alpgo.system.service.impl;

import java.util.List;

import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.system.service.IEnvironmentParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.system.mapper.EnvironmentParametersMapper;
import cc.alpgo.system.domain.EnvironmentParameters;

/**
 * environment_parametersService业务层处理
 *
 * @author asahiluna
 * @date 2023-03-30
 */
@Service
public class EnvironmentParametersServiceImpl implements IEnvironmentParametersService
{
    @Autowired
    private EnvironmentParametersMapper environmentParametersMapper;

    /**
     * 查询environment_parameters
     *
     * @param parameterId environment_parameters主键
     * @return environment_parameters
     */
    @Override
    public EnvironmentParameters selectEnvironmentParametersByParameterId(Long parameterId)
    {
        return environmentParametersMapper.selectEnvironmentParametersByParameterId(parameterId);
    }

    /**
     * 查询environment_parameters列表
     *
     * @param environmentParameters environment_parameters
     * @return environment_parameters
     */
    @Override
    public List<EnvironmentParameters> selectEnvironmentParametersList(EnvironmentParameters environmentParameters)
    {
        return environmentParametersMapper.selectEnvironmentParametersList(environmentParameters);
    }

    /**
     * 新增environment_parameters
     *
     * @param environmentParameters environment_parameters
     * @return 结果
     */
    @Override
    public int insertEnvironmentParameters(EnvironmentParameters environmentParameters)
    {
        environmentParameters.setCreateTime(DateUtils.getNowDate());
        return environmentParametersMapper.insertEnvironmentParameters(environmentParameters);
    }

    /**
     * 修改environment_parameters
     *
     * @param environmentParameters environment_parameters
     * @return 结果
     */
    @Override
    public int updateEnvironmentParameters(EnvironmentParameters environmentParameters)
    {
        environmentParameters.setUpdateTime(DateUtils.getNowDate());
        return environmentParametersMapper.updateEnvironmentParameters(environmentParameters);
    }

    /**
     * 批量删除environment_parameters
     *
     * @param parameterIds 需要删除的environment_parameters主键
     * @return 结果
     */
    @Override
    public int deleteEnvironmentParametersByParameterIds(Long[] parameterIds)
    {
        return environmentParametersMapper.deleteEnvironmentParametersByParameterIds(parameterIds);
    }

    /**
     * 删除environment_parameters信息
     *
     * @param parameterId environment_parameters主键
     * @return 结果
     */
    @Override
    public int deleteEnvironmentParametersByParameterId(Long parameterId)
    {
        return environmentParametersMapper.deleteEnvironmentParametersByParameterId(parameterId);
    }
}
