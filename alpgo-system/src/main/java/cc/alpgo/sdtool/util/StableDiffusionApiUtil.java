package cc.alpgo.sdtool.util;

import cc.alpgo.sdtool.constant.ApiContants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class StableDiffusionApiUtil {

    private static final Logger log = LoggerFactory.getLogger(CosUtil.class);
    private String token = null;

    public StableDiffusionApiResponse txt2img(Map<String, String> params, String content) throws IOException {
        OkHttpClient client = getClient(params);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String url = domain + "/sdapi/v1/txt2img";
        url = url.replace("//sdapi", "/sdapi");
        log.info("request stable diffusion domain {} ,api: {}", domain, url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Gson gson = new Gson();
        StableDiffusionApiResponse result = gson.fromJson(string, StableDiffusionApiResponse.class);
        return result;
    }
    public StableDiffusionApiResponse img2img(Map<String, String> params, String content) throws IOException {
        OkHttpClient client = getClient(params);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String url = domain + "/sdapi/v1/img2img";
        url = url.replace("//sdapi", "/sdapi");
        log.info("request stable diffusion domain {} ,api: {}", domain, url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Gson gson = new Gson();
        StableDiffusionApiResponse result = gson.fromJson(string, StableDiffusionApiResponse.class);
        return result;
    }
    private OkHttpClient getClient(Map<String, String> params) {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
    }
}
