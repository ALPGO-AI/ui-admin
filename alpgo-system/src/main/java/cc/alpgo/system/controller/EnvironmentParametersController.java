package cc.alpgo.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cc.alpgo.common.annotation.Log;
import cc.alpgo.common.core.controller.BaseController;
import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.common.core.page.TableDataInfo;
import cc.alpgo.common.enums.BusinessType;
import cc.alpgo.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cc.alpgo.system.domain.EnvironmentParameters;
import cc.alpgo.system.service.IEnvironmentParametersService;

/**
 * environment_parametersController
 *
 * @author asahiluna
 * @date 2023-03-30
 */
@RestController
@RequestMapping("/system/parameters")
public class EnvironmentParametersController extends BaseController
{
    @Autowired
    private IEnvironmentParametersService environmentParametersService;

    /**
     * 查询environment_parameters列表
     */
    @PreAuthorize("@ss.hasPermi('system:parameters:list')")
    @GetMapping("/list")
    public TableDataInfo list(EnvironmentParameters environmentParameters)
    {
        startPage();
        List<EnvironmentParameters> list = environmentParametersService.selectEnvironmentParametersList(environmentParameters);
        return getDataTable(list);
    }

    /**
     * 导出environment_parameters列表
     */
    @PreAuthorize("@ss.hasPermi('system:parameters:export')")
    @Log(title = "environment_parameters", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EnvironmentParameters environmentParameters)
    {
        List<EnvironmentParameters> list = environmentParametersService.selectEnvironmentParametersList(environmentParameters);
        ExcelUtil<EnvironmentParameters> util = new ExcelUtil<EnvironmentParameters>(EnvironmentParameters.class);
        util.exportExcel(response, list, "environment_parameters数据");
    }

    /**
     * 获取environment_parameters详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:parameters:query')")
    @GetMapping(value = "/{parameterId}")
    public AjaxResult getInfo(@PathVariable("parameterId") Long parameterId)
    {
        return AjaxResult.success(environmentParametersService.selectEnvironmentParametersByParameterId(parameterId));
    }

    /**
     * 新增environment_parameters
     */
    @PreAuthorize("@ss.hasPermi('system:parameters:add')")
    @Log(title = "environment_parameters", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EnvironmentParameters environmentParameters)
    {
        return toAjax(environmentParametersService.insertEnvironmentParameters(environmentParameters));
    }

    /**
     * 修改environment_parameters
     */
    @PreAuthorize("@ss.hasPermi('system:parameters:edit')")
    @Log(title = "environment_parameters", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EnvironmentParameters environmentParameters)
    {
        return toAjax(environmentParametersService.updateEnvironmentParameters(environmentParameters));
    }

    /**
     * 删除environment_parameters
     */
    @PreAuthorize("@ss.hasPermi('system:parameters:remove')")
    @Log(title = "environment_parameters", businessType = BusinessType.DELETE)
	@DeleteMapping("/{parameterIds}")
    public AjaxResult remove(@PathVariable Long[] parameterIds)
    {
        return toAjax(environmentParametersService.deleteEnvironmentParametersByParameterIds(parameterIds));
    }
}
