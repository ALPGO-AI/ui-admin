package cc.alpgo.sdtool.util;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.domain.ControlNetRequestBody;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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


    public StableDiffusionApiResponse apiPredict(StableDiffusionEnv params, String content) throws Exception {
        OkHttpClient client = getClient(params);
        String domain = params.getDomain();
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
        try {
            result = gson.fromJson(string, StableDiffusionApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("服务器出错，请确认服务正常运行中，并已开启API访问");
        }
        return result;
    }
    private OkHttpClient getClient(StableDiffusionEnv env) throws IOException {
        String username = env.getUsername();
        String password = env.getPassword();
        String domain = env.getDomain();
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

    public List<String> transToCos(StableDiffusionEnv env, StableDiffusionApiResponse result, List<CosConfig> cosConfigs, String wsId) throws IOException {
        List<String> results = new ArrayList<>();
        List<Object> data = result.getData();
        Map<String, String> keyUrlMap = new HashMap<>();
        String domain = env.getDomain();
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
                String url = getWebUIDownloadUrl(env, fileName);
                results.add(url);
                keyUrlMap.put(CosUtil.toKey(fileName), url);
            }
        }
        if (isNotEmpty(cosConfigs)) {
            downloadImageAsync(env, keyUrlMap, cosConfigs);
        }
        return results;
    }

    private String downloadImage(StableDiffusionEnv env, String imageUrl) throws IOException {
        OkHttpClient client = getClient(env); // 创建一个okhttp客户端对象
        // 创建一个GET方式的请求结构
        Request request = new Request.Builder().url(getWebUIDownloadUrl(env, imageUrl)).build();
        Call call = client.newCall(request); // 根据请求结构创建调用对象
        Response response = call.execute();
        byte[] bytes = response.body().bytes();
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(bytes);
        return data;
    }

    public static String getWebUIDownloadUrl(StableDiffusionEnv env, String imageUrl) throws UnsupportedEncodingException {
        String url = env.getDomain() + "/file=";
        url = url.replace("//file", "/file");
        url = url + imageUrl;
        return url;
    }

    // 下载网络图片并上传到cos
    private void downloadImageAsync(StableDiffusionEnv env, Map<String, String> keyUrlMap, List<CosConfig> configs) throws IOException {
        if (isEmpty(configs)) {
            return;
        }
        for (String key : keyUrlMap.keySet()) {
            String imageUrl = keyUrlMap.get(key);
            OkHttpClient client = getClient(env); // 创建一个okhttp客户端对象
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
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;

                    //储存下载文件的目录
                    File dir = new File(AlpgoConfig.getProfile());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, key);

                    try {

                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            //下载中更新进度条
                        }
                        fos.flush();
                        //下载完成
                    } catch (Exception e) {
                    }finally {

                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {

                        }
                    }
                    cosUtil.uploadAsync(new FileInputStream(file), key, configs, env.getEnvKey());
                }
            });
        }
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
        Object controlnetObj = parameters.get("controlnet");
        if (controlnetObj instanceof LinkedTreeMap) {
            return new ControlNetRequestBody((LinkedTreeMap) controlnetObj);
        }
        if (controlnetObj instanceof LinkedHashMap) {
            return new ControlNetRequestBody((LinkedHashMap) controlnetObj);
        }
        return new ControlNetRequestBody();
    }
    public String convertToBase64(String imageUrl, List<CosConfig> cosConfigs, StableDiffusionEnv env) throws IOException {
        if (StringUtils.isEmpty(imageUrl)) {
            return null;
        }
        try {
            return cosUtil.downloadToBase64(imageUrl, cosConfigs);
        } catch (Exception e) {
            log.info("本地和cos中找不到文件，尝试在webui环境中寻找该文件");
        }
        return downloadImage(env, imageUrl);
    }

}
