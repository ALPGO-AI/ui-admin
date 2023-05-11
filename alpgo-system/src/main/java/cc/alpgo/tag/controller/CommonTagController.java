package cc.alpgo.tag.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import cc.alpgo.tag.domain.CommonTag;
import cc.alpgo.tag.service.ICommonTagService;
import cc.alpgo.common.annotation.Log;
import cc.alpgo.common.core.controller.BaseController;
import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.common.core.page.TableDataInfo;
import cc.alpgo.common.enums.BusinessType;
import cc.alpgo.common.utils.poi.ExcelUtil;


/**
 * 标签Controller
 *
 * @author marcus
 * @date 2023-05-12
 */
@RestController
@RequestMapping("/tag/tag")
public class CommonTagController extends BaseController
{
    @Autowired
    private ICommonTagService commonTagService;

    /**
     * 查询标签列表
     */
    @PreAuthorize("@ss.hasPermi('tag:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommonTag commonTag)
    {
        startPage();
        List<CommonTag> list = commonTagService.selectCommonTagList(commonTag);
        return getDataTable(list);
    }

    /**
     * 导出标签列表
     */
    @PreAuthorize("@ss.hasPermi('tag:tag:export')")
    @Log(title = "标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommonTag commonTag)
    {
        List<CommonTag> list = commonTagService.selectCommonTagList(commonTag);
        ExcelUtil<CommonTag> util = new ExcelUtil<CommonTag>(CommonTag.class);
        util.exportExcel(response, list, "标签数据");
    }

    /**
     * 获取标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('tag:tag:query')")
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") Long tagId)
    {
        return AjaxResult.success(commonTagService.selectCommonTagByTagId(tagId));
    }

    /**
     * 新增标签
     */
    @PreAuthorize("@ss.hasPermi('tag:tag:add')")
    @Log(title = "标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommonTag commonTag)
    {
        return toAjax(commonTagService.insertCommonTag(commonTag));
    }

    /**
     * 修改标签
     */
    @PreAuthorize("@ss.hasPermi('tag:tag:edit')")
    @Log(title = "标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CommonTag commonTag)
    {
        return toAjax(commonTagService.updateCommonTag(commonTag));
    }

    /**
     * 删除标签
     */
    @PreAuthorize("@ss.hasPermi('tag:tag:remove')")
    @Log(title = "标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tagIds}")
    public AjaxResult remove(@PathVariable Long[] tagIds)
    {
        return toAjax(commonTagService.deleteCommonTagByTagIds(tagIds));
    }
}
