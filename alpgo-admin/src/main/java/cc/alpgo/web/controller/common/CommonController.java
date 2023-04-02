package cc.alpgo.web.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.common.domain.EnvTaskVO;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.event.StartEnvApplicationEvent;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.common.utils.file.FileUploadUtils;
import cc.alpgo.common.utils.file.FileUtils;
import cc.alpgo.framework.listener.EnvTaskQueueListener;
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.domain.ImageProvider;
import cc.alpgo.system.service.IEnvironmentService;
import cc.alpgo.system.service.IImageService;
import cc.alpgo.system.utils.ImageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.constant.Constants;
import cc.alpgo.framework.config.ServerConfig;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * 通用请求处理
 *
 * @author alpgo
 */
@RestController
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private EnvTaskQueueListener envTaskQueueListener;

    protected Map<String, String> getHeaderMap() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }
        return headerMap;
    }
    @Autowired
    private AlpgoConfig alpgoConfig;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private IImageService imageService;
    @Autowired
    private IEnvironmentService environmentService;

    /**
     * 环境任务列表
     */
    @GetMapping("common/envTaskList")
    public Map<String, List<EnvTaskVO>> listEnvTasks()
    {
        Map<String, String> headerMap = getHeaderMap();
        List<StableDiffusionEnv> activeEnvs = environmentService.getActiveEnvs(headerMap);
        Map<String, ConcurrentLinkedQueue<StartEnvApplicationEvent>> environmentTaskQueues = envTaskQueueListener.getEnvironmentTaskQueues();
        Map<String, List<EnvTaskVO>> result = new HashMap<>();
        for (StableDiffusionEnv activeEnv : activeEnvs) {
            String envKey = activeEnv.getEnvKey();
            ConcurrentLinkedQueue<StartEnvApplicationEvent> events = environmentTaskQueues.get(envKey);
            result.put(envKey, convertToVo(events));
        }
        return result;
    }

    private List<EnvTaskVO> convertToVo(ConcurrentLinkedQueue<StartEnvApplicationEvent> events) {
        List<EnvTaskVO> vo = new ArrayList<>();
        for (StartEnvApplicationEvent event : events) {
            vo.add(new EnvTaskVO(event));
        }
        return vo;
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = AlpgoConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = AlpgoConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            Map<String, String> headerMap = getHeaderMap();
            List<CosConfig> activeConfigs = environmentService.getActiveConfigs(headerMap);
            if (isNotEmpty(activeConfigs)) {
                File localFile = new File(AlpgoConfig.getProfile() + fileName.replace("/profile", "/"));
                cosUtil.uploadAsync(new FileInputStream(localFile), CosUtil.toKey(fileName), activeConfigs, null);
                Image image = ImageBuilder.build(activeConfigs, fileName);
                imageService.insertImage(image);
                List<ImageProvider> imageProviderList = image.getImageProviderList();
                if (isNotEmpty(imageProviderList)) {
                    ImageProvider imageProvider = imageProviderList.get(0);
                    ajax.put("url", imageProvider.getUrl());
                }
            }
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = AlpgoConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
