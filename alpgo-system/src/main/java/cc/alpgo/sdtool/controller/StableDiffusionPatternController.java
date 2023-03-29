package cc.alpgo.sdtool.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import cc.alpgo.common.annotation.Log;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;

/**
 * stable_diffusion_patternController
 *
 * @author marcus
 * @date 2023-03-21
 */
@RestController
@RequestMapping("/sdtool/pattern")
public class StableDiffusionPatternController extends BaseController
{
    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;

    /**
     * 查询stable_diffusion_pattern列表
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:list')")
    @GetMapping("/list")
    public TableDataInfo list(StableDiffusionPattern stableDiffusionPattern)
    {
        startPage();
        List<StableDiffusionPattern> list = stableDiffusionPatternService.selectStableDiffusionPatternList(stableDiffusionPattern);
        return getDataTable(list);
    }

    /**
     * 导出stable_diffusion_pattern列表
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:export')")
    @Log(title = "stable_diffusion_pattern", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StableDiffusionPattern stableDiffusionPattern)
    {
        List<StableDiffusionPattern> list = stableDiffusionPatternService.selectStableDiffusionPatternList(stableDiffusionPattern);
        ExcelUtil<StableDiffusionPattern> util = new ExcelUtil<StableDiffusionPattern>(StableDiffusionPattern.class);
        util.exportExcel(response, list, "stable_diffusion_pattern数据");
    }

    /**
     * 获取stable_diffusion_pattern详细信息
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:query')")
    @GetMapping(value = "/{patternId}")
    public AjaxResult getInfo(@PathVariable("patternId") Long patternId)
    {
        return AjaxResult.success(stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(patternId));
    }

    /**
     * 新增stable_diffusion_pattern
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:add')")
    @Log(title = "stable_diffusion_pattern", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StableDiffusionPattern stableDiffusionPattern)
    {
        return toAjax(stableDiffusionPatternService.insertStableDiffusionPattern(stableDiffusionPattern));
    }

    /**
     * 修改stable_diffusion_pattern
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:edit')")
    @Log(title = "stable_diffusion_pattern", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StableDiffusionPattern stableDiffusionPattern)
    {
        return toAjax(stableDiffusionPatternService.updateStableDiffusionPattern(stableDiffusionPattern));
    }

    /**
     * 删除stable_diffusion_pattern
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:remove')")
    @Log(title = "stable_diffusion_pattern", businessType = BusinessType.DELETE)
	@DeleteMapping("/{patternIds}")
    public AjaxResult remove(@PathVariable Long[] patternIds)
    {
        return toAjax(stableDiffusionPatternService.deleteStableDiffusionPatternByPatternIds(patternIds));
    }
    /**
     * 出图stable_diffusion_pattern
     */
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:edit')")
    @Log(title = "stable_diffusion_pattern", businessType = BusinessType.OTHER)
    @PostMapping("/{patternId}/generate")
    public AjaxResult generate(@PathVariable("patternId") Long patternId) throws Exception {

        return AjaxResult.success(stableDiffusionPatternService.generateByPatternId(getHeaderMap(), patternId));
    }
}
