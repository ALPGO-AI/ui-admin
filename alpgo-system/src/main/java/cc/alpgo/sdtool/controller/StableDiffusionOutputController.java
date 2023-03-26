package cc.alpgo.sdtool.controller;

import java.io.IOException;
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
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionOutputService;

/**
 * stable_diffusion_outputController
 *
 * @author marcus
 * @date 2023-03-23
 */
@RestController
@RequestMapping("/sdtool/output")
public class StableDiffusionOutputController extends BaseController
{
    @Autowired
    private IStableDiffusionOutputService stableDiffusionOutputService;

    /**
     * 查询stable_diffusion_output列表
     */
    @PreAuthorize("@ss.hasPermi('sdtool:output:list')")
    @GetMapping("/list")
    public TableDataInfo list(StableDiffusionOutput stableDiffusionOutput)
    {
        startPage();
        List<StableDiffusionOutput> list = stableDiffusionOutputService.selectStableDiffusionOutputList(stableDiffusionOutput);
        return getDataTable(list);
    }

    /**
     * 导出stable_diffusion_output列表
     */
    @PreAuthorize("@ss.hasPermi('sdtool:output:export')")
    @Log(title = "stable_diffusion_output", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StableDiffusionOutput stableDiffusionOutput)
    {
        List<StableDiffusionOutput> list = stableDiffusionOutputService.selectStableDiffusionOutputList(stableDiffusionOutput);
        ExcelUtil<StableDiffusionOutput> util = new ExcelUtil<StableDiffusionOutput>(StableDiffusionOutput.class);
        util.exportExcel(response, list, "stable_diffusion_output数据");
    }

    /**
     * 获取stable_diffusion_output详细信息
     */
    @PreAuthorize("@ss.hasPermi('sdtool:output:query')")
    @GetMapping(value = "/{outputId}")
    public AjaxResult getInfo(@PathVariable("outputId") Long outputId)
    {
        return AjaxResult.success(stableDiffusionOutputService.selectStableDiffusionOutputByOutputId(outputId));
    }

    /**
     * 新增stable_diffusion_output
     */
    @PreAuthorize("@ss.hasPermi('sdtool:output:add')")
    @Log(title = "stable_diffusion_output", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StableDiffusionOutput stableDiffusionOutput)
    {
        return toAjax(stableDiffusionOutputService.insertStableDiffusionOutput(stableDiffusionOutput));
    }

    /**
     * 修改stable_diffusion_output
     */
    @PreAuthorize("@ss.hasPermi('sdtool:output:edit')")
    @Log(title = "stable_diffusion_output", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StableDiffusionOutput stableDiffusionOutput)
    {
        return toAjax(stableDiffusionOutputService.updateStableDiffusionOutput(stableDiffusionOutput));
    }

    /**
     * 删除stable_diffusion_output
     */
    @PreAuthorize("@ss.hasPermi('sdtool:output:remove')")
    @Log(title = "stable_diffusion_output", businessType = BusinessType.DELETE)
	@DeleteMapping("/{outputIds}")
    public AjaxResult remove(@PathVariable Long[] outputIds)
    {
        return toAjax(stableDiffusionOutputService.deleteStableDiffusionOutputByOutputIds(outputIds));
    }
    @PreAuthorize("@ss.hasPermi('sdtool:output:edit')")
    @Log(title = "stable_diffusion_output", businessType = BusinessType.OTHER)
    @PostMapping("/{outputId}/generate")
    public AjaxResult generateByOutputId(@PathVariable("outputId") Long outputId) throws IOException {

        return AjaxResult.success(stableDiffusionOutputService.generateByPatternId(getHeaderMap(), outputId));
    }

    @PreAuthorize("@ss.hasPermi('sdtool:output:edit')")
    @Log(title = "stable_diffusion_output", businessType = BusinessType.OTHER)
    @PostMapping("/{outputId}/generateByImg")
    public AjaxResult generateByImgFromOutput(@PathVariable("outputId") Long outputId) throws IOException {
        return AjaxResult.success(stableDiffusionOutputService.generateByImgFromOutput(getHeaderMap(), outputId));
    }
}
