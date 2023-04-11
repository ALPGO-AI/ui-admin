package cc.alpgo.system.controller;

import cc.alpgo.common.annotation.Log;
import cc.alpgo.common.core.controller.BaseController;
import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.common.core.page.TableDataInfo;
import cc.alpgo.common.enums.BusinessType;
import cc.alpgo.common.utils.poi.ExcelUtil;
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.service.IEnvironmentService;
import cc.alpgo.system.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * imageController
 *
 * @author marcus
 * @date 2023-03-31
 */
@RestController
@RequestMapping("/system/image-provider")
public class ImageProviderController extends BaseController
{
    @Autowired
    private IImageService imageService;
    @Autowired
    private IEnvironmentService environmentService;

    /**
     * 随机返回图片地址(指定图床环境id)
     */
    @GetMapping("/random/{envId}")
    public Map<Long, String> selectUrls(@PathVariable("envId") Long envId)
    {
        return imageService.selectUrlsRandom(1, environmentService.getActiveConfigs(envId));
    }

}
