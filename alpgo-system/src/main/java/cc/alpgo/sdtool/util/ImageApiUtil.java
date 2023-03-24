package cc.alpgo.sdtool.util;

import com.google.gson.Gson;
import okhttp3.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class ImageApiUtil {

    private String token = null;

    public String getImageBase64String(String filePath) throws IOException {
        URL url = new URL(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        byte[] byteChunk = new byte[4096];
        int n;

        while ((n = is.read(byteChunk)) > 0) {
            baos.write(byteChunk, 0, n);
        }

        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
        return base64Image;
    }
}
