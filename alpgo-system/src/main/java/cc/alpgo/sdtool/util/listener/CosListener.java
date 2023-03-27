package cc.alpgo.sdtool.util.listener;

import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.event.UploadToCosEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CosListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger log = LoggerFactory.getLogger(CosListener.class);
    @Autowired
    private CosUtil cosUtil;
    public void onApplicationEvent(ApplicationEvent event) {
        // 判断事件类型，执行特定处理逻辑
        if (event instanceof UploadToCosEvent) {
            log.info("CosListener catch UploadToCosEvent {}", ((UploadToCosEvent) event).getKey());
            UploadToCosEvent uploadToCosEvent = (UploadToCosEvent) event;
            String imageBase64 = uploadToCosEvent.getImageBase64();
            String key = uploadToCosEvent.getKey();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put(key, imageBase64);
            cosUtil.uploadFileBase64(dataMap);
            log.info("CosListener upload {} finished", ((UploadToCosEvent) event).getKey());
        }
    }
}