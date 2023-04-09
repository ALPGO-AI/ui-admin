package cc.alpgo.system.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import cc.alpgo.common.annotation.Log;
import cc.alpgo.common.core.controller.BaseController;
import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.common.core.page.TableDataInfo;
import cc.alpgo.common.enums.BusinessType;
import cc.alpgo.common.utils.poi.ExcelUtil;
import cc.alpgo.system.domain.request.WebUIModelOptionsRequestBody;
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
import cc.alpgo.system.domain.Environment;
import cc.alpgo.system.service.IEnvironmentService;

/**
 * environmentsController
 *
 * @author asahiluna
 * @date 2023-03-30
 */
@RestController
@RequestMapping("/system/environment")
public class EnvironmentController extends BaseController
{
    @Autowired
    private IEnvironmentService environmentService;

    /**
     * 查询webuiModelOptions列表
     */
    @PreAuthorize("@ss.hasPermi('system:environment:list')")
    @PostMapping("/webuiModelOptions")
    public AjaxResult webuiModelOptions(@RequestBody WebUIModelOptionsRequestBody refresh) throws Exception {
        Map<String, List<String>> map = environmentService.webuiModelOptions(getHeaderMap(), refresh.getRefresh());
        return AjaxResult.success(map);
    }
    @PreAuthorize("@ss.hasPermi('system:environment:list')")
    @PostMapping("/webuiControlNetModelOptions")
    public AjaxResult webuiControlNetModelOptions(@RequestBody WebUIModelOptionsRequestBody refresh) throws Exception {
        Map<String, List<String>> map = environmentService.webuiControlNetModelOptions(getHeaderMap(), refresh.getRefresh());
        return AjaxResult.success(map);
    }
    /**
     * 查询environments列表
     */
    @PreAuthorize("@ss.hasPermi('system:environment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Environment environment)
    {
        startPage();
        List<Environment> list = environmentService.selectEnvironmentList(environment);
        return getDataTable(list);
    }

    /**
     * 导出environments列表
     */
    @PreAuthorize("@ss.hasPermi('system:environment:export')")
    @Log(title = "environments", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Environment environment)
    {
        List<Environment> list = environmentService.selectEnvironmentList(environment);
        ExcelUtil<Environment> util = new ExcelUtil<Environment>(Environment.class);
        util.exportExcel(response, list, "environments数据");
    }

    /**
     * 获取environments详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:environment:query')")
    @GetMapping(value = "/{environmentId}")
    public AjaxResult getInfo(@PathVariable("environmentId") Long environmentId)
    {
        return AjaxResult.success(environmentService.selectEnvironmentByEnvironmentId(environmentId));
    }

    /**
     * 新增environments
     */
    @PreAuthorize("@ss.hasPermi('system:environment:add')")
    @Log(title = "environments", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Environment environment)
    {
        return toAjax(environmentService.insertEnvironment(environment));
    }

    /**
     * 修改environments
     */
    @PreAuthorize("@ss.hasPermi('system:environment:edit')")
    @Log(title = "environments", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Environment environment)
    {
        return toAjax(environmentService.updateEnvironment(environment));
    }

    /**
     * 删除environments
     */
    @PreAuthorize("@ss.hasPermi('system:environment:remove')")
    @Log(title = "environments", businessType = BusinessType.DELETE)
	@DeleteMapping("/{environmentIds}")
    public AjaxResult remove(@PathVariable Long[] environmentIds)
    {
        return toAjax(environmentService.deleteEnvironmentByEnvironmentIds(environmentIds));
    }
}
