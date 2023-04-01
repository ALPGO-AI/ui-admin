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
import cc.alpgo.system.service.IEnvironmentService;
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
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.service.IImageService;


/**
 * imageController
 *
 * @author marcus
 * @date 2023-03-31
 */
@RestController
@RequestMapping("/system/image")
public class ImageController extends BaseController
{
    @Autowired
    private IImageService imageService;
    @Autowired
    private IEnvironmentService environmentService;

    /**
     * 查询image列表
     */
    @PostMapping("/map")
    public Map<Long, String> selectUrls(@RequestBody List<Long> ids)
    {
        return imageService.selectUrls(ids, environmentService.getActiveConfigs(getHeaderMap()));
    }

    /**
     * 查询image列表
     */
    @PreAuthorize("@ss.hasPermi('system:image:list')")
    @GetMapping("/list")
    public TableDataInfo list(Image image)
    {
        startPage();
        List<Image> list = imageService.selectImageList(image);
        return getDataTable(list);
    }

    /**
     * 导出image列表
     */
    @PreAuthorize("@ss.hasPermi('system:image:export')")
    @Log(title = "image", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Image image)
    {
        List<Image> list = imageService.selectImageList(image);
        ExcelUtil<Image> util = new ExcelUtil<Image>(Image.class);
        util.exportExcel(response, list, "image数据");
    }

    /**
     * 获取image详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:image:query')")
    @GetMapping(value = "/{imageId}")
    public AjaxResult getInfo(@PathVariable("imageId") Long imageId)
    {
        return AjaxResult.success(imageService.selectImageByImageId(imageId));
    }

    /**
     * 新增image
     */
    @PreAuthorize("@ss.hasPermi('system:image:add')")
    @Log(title = "image", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Image image)
    {
        return toAjax(imageService.insertImage(image));
    }

    /**
     * 修改image
     */
    @PreAuthorize("@ss.hasPermi('system:image:edit')")
    @Log(title = "image", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Image image)
    {
        return toAjax(imageService.updateImage(image));
    }

    /**
     * 删除image
     */
    @PreAuthorize("@ss.hasPermi('system:image:remove')")
    @Log(title = "image", businessType = BusinessType.DELETE)
	@DeleteMapping("/{imageIds}")
    public AjaxResult remove(@PathVariable Long[] imageIds)
    {
        return toAjax(imageService.deleteImageByImageIds(imageIds));
    }
}
