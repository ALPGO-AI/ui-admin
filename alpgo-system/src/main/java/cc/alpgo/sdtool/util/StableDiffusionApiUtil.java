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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class StableDiffusionApiUtil {

    private String token = null;

    public StableDiffusionApiResponse sendApiToImg(Map<String, String> params, String content) throws IOException {
        OkHttpClient client = getClient(params);
        String domain = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_DOMAIN);
        String token = params.get(ApiContants.STABLE_DIFFUSION_WEBUI_TOKEN);
        String url = domain + "/run/predict";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", token)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Gson gson = new Gson();
        StableDiffusionApiResponse result = gson.fromJson(string, StableDiffusionApiResponse.class);
        List<Object> data = result.getData();
        if (!CollectionUtils.isEmpty(data)) {
            Object listObj = data.get(0);
            if (listObj instanceof ArrayList) {
                ArrayList arr = (ArrayList) listObj;
                if (!CollectionUtils.isEmpty(arr)) {
                    Object linkedTreeMapObj = arr.get(0);
                    LinkedTreeMap map = (LinkedTreeMap) linkedTreeMapObj;
                    Object nameObj = map.get("name");
                    String fileName = getFileName(nameObj);
                    result.setFileName(fileName);
                }
            }
        }
        return result;
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
