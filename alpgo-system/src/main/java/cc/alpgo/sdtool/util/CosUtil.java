package cc.alpgo.sdtool.util;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.util.event.UploadToCosEvent;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import com.idrsolutions.image.png.PngCompressor;

import java.io.*;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CosUtil {

    @Autowired
    private TimeStringUtil timeStringUtil;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AlpgoConfig alpgoConfig;
    private static final Logger log = LoggerFactory.getLogger(CosUtil.class);
    private COSClient cosClient = null;

    public List<String> upload(List<String> images) {
        Map<String, String> dataMap = new HashMap<>();
        for (String image : images) {
            dataMap.put(generateFileName(), image);
        }
        return uploadFileBase64(dataMap);
    }

    private String generateFileName() {
        return timeStringUtil.getTimeString() + "_" + UUID.randomUUID().toString() + ".png";
    }

    public List<String> uploadAsync(List<String> images) {
        Map<String, String> dataMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (String image : images) {
            String key = generateFileName();
            dataMap.put(key, image);
            list.add(key);
            applicationContext.publishEvent(new UploadToCosEvent(key, image));
        }
        return list;
    }

    public COSClient getClient() {
        Map<String, Object> map = new HashMap<>();
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(alpgoConfig.getCosApiSecretId(), alpgoConfig.getCosApiSecretKey());
        // 2 设置bucket的区域, COS地域在上面有提到
        ClientConfig clientConfig = new ClientConfig(new Region(alpgoConfig.getCosApiRegion()));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        return cosclient;
    }
    public List<String> uploadFileBase64(Map<String, String> dataMap) {
        List<String> keys = new ArrayList<>();
        cosClient = getClient();
        String result = "";
        if(cosClient == null) {
            return keys;
        }
        try {
            for (Map.Entry<String, String> stringStringEntry : dataMap.entrySet()) {
                result = uploadFileBase64Single(stringStringEntry.getValue(), stringStringEntry.getKey());
                keys.add(stringStringEntry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
        }finally {
            //关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return keys;
    }

    private String uploadFileBase64Single(String base64Str, String fileKey) throws Exception {
        String base64compress = compressImageByFile(base64Str);
        byte[] bytes = new BASE64Decoder().decodeBuffer(base64compress.trim());
        //转化为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("image/png");
        //对象键（Key）是对象在存储桶中的唯一标识。
        //例如，在对象的访问域名https://ba-189629.cos.ap-beijing.myqcloud.com/app/img/bb/profile_big.jpg
        //则把key设为app/img/bb/profile_big.jpg  //云控制台目录//ba-189629-->app-->img-->bb
        PutObjectResult putObjectResult = cosClient.putObject(alpgoConfig.getCosApiBucketName(), fileKey, inputStream, objectMetadata);
        String result = putObjectResult.getETag();  // 获取文件的 etag
        log.info("uploadFileBase64==result=======result:{}",result);
        return result;
    }

    /**
     * 压缩jpg图片,返回base64字符串
     * sourceFile: 源文件;
     */
    public static String compressImageByFile(String inString) throws Exception {
        byte[] bytes = new BASE64Decoder().decodeBuffer(inString.trim());
        //转化为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            PngCompressor.compress(inputStream, out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null){
                out.close();
            }
        }
    }
}