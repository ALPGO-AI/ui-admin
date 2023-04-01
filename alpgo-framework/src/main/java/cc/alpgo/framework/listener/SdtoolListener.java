package cc.alpgo.framework.listener;

import cc.alpgo.common.event.SdToolGenerateByPatternIdEvent;
import cc.alpgo.common.event.UploadToCosInputStreamEvent;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.event.UploadToCosEvent;
import cc.alpgo.framework.websocket.WebSocketUsers;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableAsync
public class SdtoolListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger log = LoggerFactory.getLogger(SdtoolListener.class);
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;
    public void onApplicationEvent(ApplicationEvent event) {
        // 判断事件类型，执行特定处理逻辑
        if (event instanceof UploadToCosEvent) {
            log.info("SDToolListener catch UploadToCosEvent {}", ((UploadToCosEvent) event).getKey());
            UploadToCosEvent uploadToCosEvent = (UploadToCosEvent) event;
            String imageBase64 = uploadToCosEvent.getImageBase64();
            String key = uploadToCosEvent.getKey();
            CosConfig cosConfig = uploadToCosEvent.getCosConfig();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put(key, imageBase64);
            cosUtil.uploadFileBase64(dataMap, cosConfig);
            log.info("SDToolListener upload {} finished", ((UploadToCosEvent) event).getKey());
        }
        if (event instanceof UploadToCosInputStreamEvent) {
            log.info("SDToolListener catch UploadToCosInputStreamEvent {}", ((UploadToCosInputStreamEvent) event).getKey());
            UploadToCosInputStreamEvent uploadToCosEvent = (UploadToCosInputStreamEvent) event;
            InputStream inputStream = uploadToCosEvent.getInputStream();
            CosConfig cosConfig = uploadToCosEvent.getCosConfig();
            String key = uploadToCosEvent.getKey();
            String wsId = uploadToCosEvent.getWsId();
//            WebSocketUsers.sendMessageToUserByWsId(wsId, cosConfig.getEnvName() + " 开始上传图片 " + key + " 到图床");
            try {
                cosUtil.uploadStream(key, inputStream, cosConfig);
            } catch (IOException e) {
                log.error("SDToolListener upload {} error", ((UploadToCosInputStreamEvent) event).getKey());
//                WebSocketUsers.sendMessageToUserByWsId(wsId, cosConfig.getEnvName() + " 上传图片 " + key + " 到图床出现错误");
                throw new RuntimeException(e);
            }
            WebSocketUsers.sendMessageToUserByWsId(wsId, cosConfig.getEnvName() + " 成功上传图片 " + key + " 到图床");
            log.info("SDToolListener upload {} finished", ((UploadToCosInputStreamEvent) event).getKey());
        }
        if (event instanceof SdToolGenerateByPatternIdEvent) {
            log.info("SDToolListener catch SdToolGenerateByPatternIdEvent {}", ((SdToolGenerateByPatternIdEvent) event).getPatternId());
            SdToolGenerateByPatternIdEvent sdEvent = (SdToolGenerateByPatternIdEvent) event;
            try {
                stableDiffusionPatternService.generateByPatternIdAsync(
                        sdEvent.getPatternId(), sdEvent.getCosConfigs(), sdEvent.getSdEnv(), sdEvent.getWsId()
                );
            } catch (Exception e) {
                log.error("SDToolListener execute {} error", ((SdToolGenerateByPatternIdEvent) event).toString());
                throw new RuntimeException(e);
            }
            log.info("SDToolListener execute {} finished", ((SdToolGenerateByPatternIdEvent) event).toString());
        }
    }
}