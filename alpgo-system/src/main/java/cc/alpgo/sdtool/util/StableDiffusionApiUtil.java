package cc.alpgo.sdtool.util;

import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.constant.ApiContants;
import cc.alpgo.sdtool.domain.ControlNetRequestBody;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class StableDiffusionApiUtil {

    @Autowired
    private CosUtil cosUtil;

    private static final Logger log = LoggerFactory.getLogger(CosUtil.class);
    private String token = null;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public static String generateSessionHash() {
        return UUID.randomUUID().toString();
    }
    public StableDiffusionApiResponse apiPredict(Map<String, String> params, String content) throws Exception {
        OkHttpClient client = getClient(params);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String url = domain + "/api/predict";
        url = url.replace("//api", "/api");
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
        StableDiffusionApiResponse result = null;
        log.info("response: {}", string);
        try {
            result = gson.fromJson(string, StableDiffusionApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("服务器出错，请确认服务正常运行中，并已开启API访问");
        }
        return result;
    }
    private OkHttpClient getClient(Map<String, String> params) throws IOException {
        String username = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_USERNAME);
        String password = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_PASSWORD);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String url = domain + "/login";
        url = url.replace("//login", "/login");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        RequestBody formdata = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formdata)
                .addHeader("Content-Type", "application/json")
                .build();
        Response execute = client.newCall(request).execute();
        String string = execute.body().string();
        return client;
    }

    public List<String> transToCos(Map<String, String> params, StableDiffusionApiResponse result) throws IOException {
        List<String> results = new ArrayList<>();
        List<Object> data = result.getData();
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        if (isEmpty(data)) {
            return results;
        }
        List<Object> objects = (List<Object>) data.get(0);
        if (isEmpty(objects)) {
            return results;
        }
        for (Object object : objects) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (map == null) {
                continue;
            }
            Object isFileObj = map.get("is_file");
            Boolean isFile = (Boolean) isFileObj;
            if (isFile != null && isFile) {
                Object fileNameObj = map.get("name");
                String fileName = (String) fileNameObj;
                String url = domain + "/file=";
                url = url.replace("//file", "/file");
                url = url + fileName;
                String cosKey = cosUtil.generateFileName();
                downloadImage(params, url, cosKey);
                results.add(cosKey);
            }
        }
        return results;
    }
    // 下载网络图片
    private void downloadImage(Map<String, String> params, String imageUrl, String cosKey) throws IOException {
        OkHttpClient client = getClient(params); // 创建一个okhttp客户端对象
        // 创建一个GET方式的请求结构
        Request request = new Request.Builder().url(imageUrl).build();
        Call call = client.newCall(request); // 根据请求结构创建调用对象
        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { // 请求失败
                log.error("下载失败 {}", e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                InputStream is = response.body().byteStream();
                String mediaType = response.body().contentType().toString();
                long length = response.body().contentLength();
                cosUtil.upload(is, cosKey);
            }
        });
    }

    public boolean isEnableControlNet(Map<String, Object> parameters) {
        if (parameters == null) {
            return false;
        }
        ControlNetRequestBody controlnet = getControlNetRequestBody(parameters);
        if (controlnet == null) {
            return false;
        }
        Boolean enable = controlnet.getEnable();
        if (enable == null) {
            return false;
        }
        return enable;
    }

    public ControlNetRequestBody getControlNetRequestBody(Map<String, Object> parameters) {
        LinkedTreeMap controlnet = (LinkedTreeMap) parameters.get("controlnet");
        return new ControlNetRequestBody(controlnet);
    }
    public String convertToBase64(String imageUrl) throws IOException {
        if (StringUtils.isEmpty(imageUrl)) {
            return null;
        }
        return cosUtil.downloadToBase64(imageUrl);
    }
}
