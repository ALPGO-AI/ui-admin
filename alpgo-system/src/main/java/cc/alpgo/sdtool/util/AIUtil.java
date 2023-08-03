package cc.alpgo.sdtool.util;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.core.domain.entity.SysDictData;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.domain.ChatBoxAIModel;
import cc.alpgo.sdtool.domain.OpenAIRequest;
import cc.alpgo.system.service.ISysDictDataService;
import cc.alpgo.system.service.ISysDictTypeService;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AIUtil {
    @Autowired
    private AlpgoConfig alpgoConfig;
    @Autowired
    private ISysDictTypeService sysDictTypeService;
    @Autowired
    private ISysDictDataService sysDictDataService;

    public String getAIPrompt(String content) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .followRedirects(true)
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{" +
                "\"uuid\": \""+ UUID.randomUUID().toString()+"\"," +
                "\"messages\": [" +
                "{" +
                "\"role\": \"system\"," +
                "\"content\": \"给我AI英文提示词，英文逗号分隔，不要返回其他任何内容\"" +
                "}," +
                "{" +
                "    \"role\": \"user\"," +
                "    \"content\": \""+content+"\"" +
                "}\n    ],\n    \"temperature\": 0.7,\n    \"stream\": true\n}");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://chatboxai.app/api/ai/chat")
                .method("POST", body)
                .addHeader("Authorization", alpgoConfig.getChatBoxAIKey())
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        // Handle the response as needed
        String result = response.body().string();
        String prompt = "";
        response.close();
        String[] split = result.split("data: ");
        Gson gson = new Gson();
        for (String str : split) {
            try {
                ChatBoxAIModel res = gson.fromJson(str, ChatBoxAIModel.class);
                String content2 = res.getChoices().get(0).getDelta().getContent();
                if (StringUtils.isNotBlank(content2) && !content2.equals("null")) {
                    prompt += content2;
                }
            } catch (Exception e) {
                // nothing
            }
        }
        return prompt;
    }

    public String cartToolCall(String label, String content) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .followRedirects(true)
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        String systemLogic = sysDictTypeService.selectValueByTypeAndLabel("ai_preset", label);
        if (StringUtils.isBlank(systemLogic)) {
            return label + "配置未找到";
        }
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{" +
                "\"uuid\": \""+ UUID.randomUUID().toString()+"\"," +
                "\"messages\": [" +
                "{" +
                "\"role\": \"system\"," +
                "\"content\": \""+systemLogic+"\"" +
                "}," +
                "{" +
                "    \"role\": \"user\"," +
                "    \"content\": \""+content+"\"" +
                "}\n    ],\n    \"temperature\": 0.7,\n    \"stream\": true\n}");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://chatboxai.app/api/ai/chat")
                .method("POST", body)
                .addHeader("Authorization", alpgoConfig.getChatBoxAIKey())
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        // Handle the response as needed
        String result = response.body().string();
        String prompt = "";
        response.close();
        String[] split = result.split("data: ");
        Gson gson = new Gson();
        for (String str : split) {
            try {
                ChatBoxAIModel res = gson.fromJson(str, ChatBoxAIModel.class);
                String content2 = res.getChoices().get(0).getDelta().getContent();
                if (StringUtils.isNotBlank(content2) && !content2.equals("null")) {
                    prompt += content2;
                }
            } catch (Exception e) {
                // nothing
            }
        }
        return prompt;
    }

    public String cartToolCall(OpenAIRequest req) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .followRedirects(true)
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{" +
                "\"uuid\": \""+ UUID.randomUUID().toString()+"\"," +
                "\"messages\": [" +
                "{" +
                "\"role\": \"system\"," +
                "\"content\": \""+req.getQuestion()+"\"" +
                "}," +
                "{" +
                "    \"role\": \"user\"," +
                "    \"content\": \""+req.getContent()+"\"" +
                "}\n    ],\n    \"temperature\": 0.7,\n    \"stream\": true\n}");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://chatboxai.app/api/ai/chat")
                .method("POST", body)
                .addHeader("Authorization", alpgoConfig.getChatBoxAIKey())
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        // Handle the response as needed
        String result = response.body().string();
        String prompt = "";
        response.close();
        String[] split = result.split("data: ");
        Gson gson = new Gson();
        for (String str : split) {
            try {
                ChatBoxAIModel res = gson.fromJson(str, ChatBoxAIModel.class);
                String content2 = res.getChoices().get(0).getDelta().getContent();
                if (StringUtils.isNotBlank(content2) && !content2.equals("null")) {
                    prompt += content2;
                }
            } catch (Exception e) {
                // nothing
            }
        }
        return prompt;
    }
}
