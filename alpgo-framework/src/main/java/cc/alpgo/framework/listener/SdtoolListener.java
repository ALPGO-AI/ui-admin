package cc.alpgo.framework.listener;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.core.redis.RedisCache;
import cc.alpgo.common.enums.EnvTaskExecutionStatus;
import cc.alpgo.common.event.*;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.framework.websocket.WebSocketUsers;
import cc.alpgo.neo4j.service.INeo4jService;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.alpgo.common.utils.StringUtils.isNotEmpty;

@Component
@EnableAsync
public class SdtoolListener implements ApplicationListener<ApplicationEvent> {
    @Autowired
    private ApplicationContext applicationContext;
    private static final Logger log = LoggerFactory.getLogger(SdtoolListener.class);
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private EnvTaskQueueListener envTaskQueueListener;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;
    @Autowired
    private INeo4jService neo4jService;
    @Autowired
    private AlpgoConfig alpgoConfig;
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
            List<CosConfig> cosConfigs = uploadToCosEvent.getCosConfigs();
            String key = uploadToCosEvent.getKey();
            String envKey = uploadToCosEvent.getEnvKey();
            try {
                cosUtil.uploadStream(key, inputStream, cosConfigs);
            } catch (IOException e) {
                log.error("SDToolListener upload {} error", ((UploadToCosInputStreamEvent) event).getKey());
                throw new RuntimeException(e);
            }
            log.info("SDToolListener upload {} finished", ((UploadToCosInputStreamEvent) event).getKey());
            UploadToCosInputStreamEvent uploadToCosInputStreamEvent = (UploadToCosInputStreamEvent) event;
            String cosKey = uploadToCosInputStreamEvent.getKey();
            List<CosConfig> cosConfigsTemp = uploadToCosInputStreamEvent.getCosConfigs();
            Gson gson = new Gson();
            if (isNotEmpty(cosConfigsTemp)) {
                StableDiffusionEnv env = uploadToCosEvent.getEnv();
                if (env != null) {
                    Map<String, Object> webhookDataMap = env.getWebhookDataMap();
                    if (webhookDataMap == null) {
                        webhookDataMap = new HashMap<>();
                    }
                    CosConfig cosConfig = cosConfigsTemp.get(0);
                    String fullUrl = cosUtil.getFullUrl(cosConfig, cosKey);
                    Map map = new HashMap<>();
                    map.put("outputImageUrl", fullUrl);
                    map.put("envId", env.getEnvId());
                    map.put("patternData", webhookDataMap);
                    map.put("outputParams", env.getOutputResponseData());
                    applicationContext.publishEvent(new WebhooksEvent(gson.toJson(map)));
                }
            }
        }
        if (event instanceof SdToolExecuteGenerateByPatternIdEvent) {
            log.info("SDToolListener catch SdToolGenerateByPatternIdEvent {}", ((SdToolExecuteGenerateByPatternIdEvent) event).getPatternId());
            SdToolExecuteGenerateByPatternIdEvent sdEvent = (SdToolExecuteGenerateByPatternIdEvent) event;
            try {

                updateStatus(sdEvent.getSdEnv().getEnvKey(), EnvTaskExecutionStatus.Start);
                StableDiffusionOutput stableDiffusionOutput = stableDiffusionPatternService.generateByPatternIdAsync(
                        sdEvent.getPatternId(), sdEvent.getCosConfigs(), sdEvent.getSdEnv(), sdEvent.getWsId(), sdEvent.getExtraGenerateParams()
                );
                neo4jService.createOutput(stableDiffusionOutput);
                updateStatus(sdEvent.getSdEnv().getEnvKey(), EnvTaskExecutionStatus.Finished);
            } catch (Exception e) {
                log.error("SDToolListener execute {} error", ((SdToolExecuteGenerateByPatternIdEvent) event).toString());
                updateStatus(sdEvent.getSdEnv().getEnvKey(), EnvTaskExecutionStatus.Idle);
                throw new RuntimeException(e);
            }
            log.info("SDToolListener execute {} finished", ((SdToolExecuteGenerateByPatternIdEvent) event).toString());
        }
        if (event instanceof SdToolAddGenerateByPatternIdEvent) {
            log.info("SDToolListener catch SdToolAddGenerateByPatternIdEvent {}", ((SdToolAddGenerateByPatternIdEvent) event).getEnvKey());
            SdToolAddGenerateByPatternIdEvent sdAddEvent = (SdToolAddGenerateByPatternIdEvent) event;
            envTaskQueueListener.addTaskToEnvironmentQueue(sdAddEvent.getEnvKey(), sdAddEvent);
            log.info("SDToolListener execute {} finished", ((SdToolAddGenerateByPatternIdEvent) event).toString());
        }
        if (event instanceof WebhooksEvent) {
            for (String webhook : alpgoConfig.getWebhooks()) {
                OkHttpClient client = new OkHttpClient.Builder().build();
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), ((WebhooksEvent) event).getPayload());
                Request request = new Request.Builder()
                        .url(webhook)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                log.info("call webhook {} success", webhook);
            }
        }
        if (event instanceof CustomWebhooksEvent) {
            CustomWebhooksEvent customWebhooksEvent = (CustomWebhooksEvent) event;
            String webhook = customWebhooksEvent.getUrl();
            OkHttpClient client = new OkHttpClient.Builder().build();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), customWebhooksEvent.getPayload());
            Request request = new Request.Builder()
                    .url(webhook)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("call webhook {} success", webhook);
        }
    }
    private void updateStatus(String envKey, EnvTaskExecutionStatus processing) {
        applicationContext.publishEvent(new UpdateEnvExecutionStatusEvent(envKey, processing));
    }
}