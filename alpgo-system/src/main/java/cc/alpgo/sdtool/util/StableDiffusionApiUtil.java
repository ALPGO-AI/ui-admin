package cc.alpgo.sdtool.util;

import cc.alpgo.sdtool.constant.ApiContants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
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

    private String token = null;

    public StableDiffusionApiResponse txt2img(Map<String, String> params, String content) throws IOException {
        OkHttpClient client = getClient(params);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String token = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_TOKEN);
        String url = domain + "/sdapi/v1/txt2img";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
//                .addHeader("Cookie", token)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Gson gson = new Gson();
        StableDiffusionApiResponse result = gson.fromJson(string, StableDiffusionApiResponse.class);
        List<String> data = result.getImages();

        return result;
    }

    public String getToken(Map<String, String> params) throws IOException {
        params.put(ApiContants.STABLE_DIFFUSION_WEBUI_USERNAME, "username");
        params.put(ApiContants.STABLE_DIFFUSION_WEBUI_PASSWORD, "password");
        OkHttpClient client = getClient(params);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String url = domain + "/login";
        RequestBody body = new FormBody.Builder()
                .add("username", params.get(ApiContants.STABLE_DIFFUSION_WEBUI_USERNAME))
                .add("password", params.get(ApiContants.STABLE_DIFFUSION_WEBUI_PASSWORD))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Gson gson = new Gson();
        Map<String, Object> result = gson.fromJson(string, Map.class);
        String token = (String) result.get("token");
        return token;
    }
    private OkHttpClient getClient(Map<String, String> params) {
        String username = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_USERNAME);
        String password = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_PASSWORD);
        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(username, password);
                        return response.request().newBuilder().header("Authorization", credential).build();
                    }
                })
                .build();
    }

    private String getFileName(Object nameObj) throws UnsupportedEncodingException {
        if (nameObj == null) {
            return "";
        }
        String pathOrigin = (String) nameObj;
        pathOrigin = pathOrigin.replaceAll("\\\\\\\\", "\\");
        String encodeUrl = java.net.URLEncoder.encode(pathOrigin, "UTF-8");
        return "https://outputs-1251764741.cos.ap-shanghai.myqcloud.com/" + encodeUrl.replaceAll("\\+", "%20");
    }
}
