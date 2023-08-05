package cc.alpgo.sdtool.controller;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.core.controller.BaseController;
import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.common.core.page.TableDataInfo;
import cc.alpgo.common.domain.FileNameVO;
import cc.alpgo.common.enums.BusinessType;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.event.CustomWebhooksEvent;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.common.utils.file.FileUtils;
import cc.alpgo.common.utils.poi.ExcelUtil;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.neo4j.service.INeo4jService;
import cc.alpgo.sdtool.domain.ChatBoxAIModel;
import cc.alpgo.sdtool.domain.GenerateFontArtRequestBody;
import cc.alpgo.sdtool.util.AIUtil;
import cc.alpgo.sdtool.util.AutoLayoutGenerator;
import cc.alpgo.sdtool.util.BlackBackgroundWithWhiteArtisticTextGenerator;
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.domain.ImageProvider;
import cc.alpgo.system.service.IEnvironmentService;
import cc.alpgo.system.utils.ImageBuilder;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.OkHttpClient;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
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
import cc.alpgo.sdtool.domain.PackageCardRequestBody;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

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
    private INeo4jService neo4jService;
    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;

    @Autowired
    private BlackBackgroundWithWhiteArtisticTextGenerator blackBackgroundWithWhiteArtisticTextGenerator;

    @Autowired
    private IEnvironmentService environmentService;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AlpgoConfig alpgoConfig;
    @Autowired
    private AIUtil aiUtil;
    @Autowired
    private AutoLayoutGenerator autoLayoutGenerator;

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
    public AjaxResult generate(@PathVariable("patternId") Long patternId, @RequestBody List<Map<String, Object>> extraGenerateParams) throws Exception {

        return AjaxResult.success(stableDiffusionPatternService.generateByPatternId(getHeaderMap(), patternId, extraGenerateParams));
    }
    @PreAuthorize("@ss.hasPermi('sdtool:pattern:list')")
    @GetMapping("/graph")
    public AjaxResult graph(){

        return AjaxResult.success(neo4jService.fetchPatternGraph());
    }

    @PreAuthorize("@ss.hasPermi('sdtool:pattern:edit')")
    @PostMapping("/packageCard")
    public AjaxResult packageCard(@RequestBody PackageCardRequestBody packageCardRequestBody){

        return AjaxResult.success(neo4jService.packageCard(packageCardRequestBody));
    }

    @PostMapping("/generateFontArtAndReturnCosUrl")
    public AjaxResult generateFontArtAndReturnCosUrl(@RequestBody GenerateFontArtRequestBody body) throws IOException {
        Map<String, String> headerMap = getHeaderMap();
        InputStream fontArtImage = blackBackgroundWithWhiteArtisticTextGenerator.generateImageByTextReturnInputStream(
                body.getFontArtText(),
                body.getFontArtSize(),
                body.getWidth() == null ? 512 : body.getWidth(),
                body.getHeight() == null ? 768 : body.getHeight()
        );
        List<CosConfig> activeConfigs = environmentService.getActiveConfigs(headerMap);
        if (isNotEmpty(activeConfigs)) {
            FileNameVO fileNameVO = new FileNameVO(UUID.randomUUID().toString()+".png", "01_FONT_ART");
            cosUtil.uploadAsync(fontArtImage, CosUtil.toKey(
                    fileNameVO
            ), activeConfigs, null);
            Image image = ImageBuilder.build(activeConfigs, fileNameVO);
            List<ImageProvider> imageProviderList = image.getImageProviderList();
            if (isNotEmpty(imageProviderList)) {
                ImageProvider imageProvider = imageProviderList.get(0);
                return AjaxResult.success(imageProvider.getUrl());
            }
        }
        return AjaxResult.error();
    }


    @GetMapping("/fontart/{content}/{fontSize}/base64.png")
    public void generateFontArtAndReturnFile(@PathVariable("content") String fontArtText,@PathVariable("fontSize") Integer fontArtSize, HttpServletResponse response, HttpServletRequest request) throws IOException {
        InputStream fontArtImage = blackBackgroundWithWhiteArtisticTextGenerator.generateImageByTextReturnInputStream(
                fontArtText,
                fontArtSize,
                512,
                768
        );
        try
        {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            FileUtils.setAttachmentResponseHeader(response, "base64.png");
            FileUtils.writeBytes(fontArtImage, response.getOutputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @GetMapping("/fontart/{content}/{fontSize}")
    public String getFontArtCardWithSize(@PathVariable("content") String fontArtText,@PathVariable("fontSize") Integer fontArtSize){
        String fontArtImage = blackBackgroundWithWhiteArtisticTextGenerator.generateImageByText(
                fontArtText,
                fontArtSize,
                512,
                768
        );
        return "<img src=\"data:image/png;base64,"+fontArtImage+"\" width=\"512\" height=\"768\">";
    }
    @GetMapping("/fontart/{content}")
    public String getFontArtCard(@PathVariable("content") String fontArtText) throws IOException {
        Integer fontArtSize = 72;
        String fontArtImage = autoLayoutGenerator.generateImageByTextAutoLayout(
                fontArtText,
                fontArtSize,
                512,
                768
        );
        return "<img src=\"data:image/png;base64,"+fontArtImage+"\" width=\"512\" height=\"768\">";
    }
    @GetMapping("/fontart/{content}/base64.png")
    public void generateFontArtAndReturnFile(@PathVariable("content") String fontArtText,HttpServletResponse response, HttpServletRequest request) throws IOException {
        InputStream fontArtImage = autoLayoutGenerator.generateImageByTextAutoLayoutReturnInputStream(
                fontArtText,
                72,
                512,
                768
        );
        try
        {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            FileUtils.setAttachmentResponseHeader(response, "base64.png");
            FileUtils.writeBytes(fontArtImage, response.getOutputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @GetMapping("/fontart/regeneratebyrevid/{revId}")
    public String regenerateByRevId(@PathVariable("revId") String revId){
        String url = "https://hooks.airtable.com/workflows/v1/genericWebhook/apptggWYplng9n6VI/wflKkckMGdtTyuR4j/wtrpm7zd3djJTMPcR";
        Gson gson = new Gson();
        Map map = new HashMap<>();
        map.put("revId", revId);
        applicationContext.publishEvent(new CustomWebhooksEvent(url, gson.toJson(map)));
        return "请求已接受，正在处理中，请稍等";
    }

    @GetMapping("/fontart/transtoprompt/{content}")
    public AjaxResult transtoprompt(@PathVariable("content") String content) throws IOException {

        return AjaxResult.success(aiUtil.getAIPrompt(content));
    }

    @PostMapping("/fontart/generateWithAuthCode/{authcode}")
    public AjaxResult generateFontArtAndReturnCosUrl(@PathVariable("authcode") String authCode, @RequestBody List<Map<String, Object>> extraGenerateParams) throws Exception {
        // check auth code status
        StableDiffusionPattern pattern = stableDiffusionPatternService.selectStableDiffusionPatternByAuthCode(authCode);
        if (pattern != null) {
            Map<String, Object> map = new Gson().fromJson(pattern.getParametersJson(), HashMap.class);
            if (map.get("enableAuthCode") != null && (Boolean) map.get("enableAuthCode") && map.get("authCode") != null && ((String) map.get("authCode")).equals(authCode)) {
                Object enableAuthCodeCanUseHeaderParams = map.get("enableAuthCodeCanUseHeaderParams");
                if (enableAuthCodeCanUseHeaderParams == null) {
                    return AjaxResult.error("No available envs");
                }
                LinkedTreeMap enableAuthCodeCanUseHeaderParamsMap = (LinkedTreeMap) enableAuthCodeCanUseHeaderParams;
                Set<String> set = enableAuthCodeCanUseHeaderParamsMap.keySet();
                LinkedHashMap headerParams = new LinkedHashMap<>();
                for (String key: set) {
                    headerParams.put(key, enableAuthCodeCanUseHeaderParamsMap.get(key).toString());
                }
                if(extraGenerateParams.get(0).get("autoPrompt") != null && (Boolean) extraGenerateParams.get(0).get("autoPrompt")) {
                    extraGenerateParams.get(0).put("AIPrompt", aiUtil.getAIPrompt((String)extraGenerateParams.get(0).get("autoPromptContent")));
                }
                    // valid auth code, start generate with extraParams
                stableDiffusionPatternService.generateByPatternId(
                        headerParams,
                        pattern.getPatternId(),
                        extraGenerateParams
                );
                return AjaxResult.success("OK");
            }
        }
        return AjaxResult.error("Invalid auth code");
    }
}
