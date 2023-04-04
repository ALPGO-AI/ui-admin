package cc.alpgo.common.utils;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.event.UploadToCosEvent;
import cc.alpgo.common.event.UploadToCosInputStreamEvent;
import cc.alpgo.common.utils.uuid.UUID;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.idrsolutions.image.png.PngCompressor;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cc.alpgo.common.utils.sign.Md5Utils.hash;

@Component
public class CosUtil {

    @Autowired
    private AlpgoConfig alpgoConfig;
    @Autowired
    private TimeStringUtil timeStringUtil;
    @Autowired
    private ApplicationContext applicationContext;
    private static final Logger log = LoggerFactory.getLogger(CosUtil.class);
    private Map<String, COSClient> cosClientMap = new HashMap<>();

    public static String getFullUrl(CosConfig cosConfig, String fileName) {
        return "https://{bucketName}.cos.{bucketRegion}.myqcloud.com/"
                .replace("{bucketName}", cosConfig.getCosApiBucketName())
                .replace("{bucketRegion}", cosConfig.getCosApiRegion()) + fileName;
    }

    public static String toKey(String fileName) {
        String[] split = fileName.split("\\.");
        String ext = split[split.length - 1];
        return hash(fileName) + "." + ext;
    }

    public List<String> upload(List<String> images, CosConfig cosConfig) {
        Map<String, String> dataMap = new HashMap<>();
        for (String image : images) {
            dataMap.put(generateFileName(), image);
        }
        return uploadFileBase64(dataMap, cosConfig);
    }

    public String upload(String image, CosConfig cosConfig) {
        Map<String, String> dataMap = new HashMap<>();
        String fileName = generateFileName();
        dataMap.put(fileName, image);
        uploadFileBase64(dataMap, cosConfig);
        return fileName;
    }
    public String upload(MultipartFile file, CosConfig cosConfig) {
        Map<String, MultipartFile> dataMap = new HashMap<>();
        String fileName = generateFileName();
        dataMap.put(fileName, file);
        uploadFile(dataMap, cosConfig);
        return fileName;
    }
    public String generateFileName() {
        return timeStringUtil.getTimeString() + "_" + UUID.randomUUID().toString() + ".png";
    }

    public void uploadAsync(InputStream is, String cosKey, List<CosConfig> cosConfigs, String envKey) throws IOException {
        applicationContext.publishEvent(new UploadToCosInputStreamEvent(cosKey, is, cosConfigs, envKey));
    }
    public List<String> uploadAsync(List<String> images, CosConfig cosConfig) {
        Map<String, String> dataMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (String image : images) {
            String key = generateFileName();
            dataMap.put(key, image);
            list.add(key);
            applicationContext.publishEvent(new UploadToCosEvent(key, image, cosConfig));
        }
        return list;
    }

    public String uploadAsync(String image, CosConfig cosConfig) {
        String key = generateFileName();
        applicationContext.publishEvent(new UploadToCosEvent(key, image, cosConfig));
        return key;
    }
    public COSClient getClient(CosConfig cosConfig) {
        if (cosClientMap.containsKey(cosConfig.getHash())) {
            COSClient cosClient = cosClientMap.get(cosConfig.getHash());
            if (cosClient != null) {
                return cosClient;
            }
        }
        Map<String, Object> map = new HashMap<>();
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(cosConfig.getCosApiSecretId(), cosConfig.getCosApiSecretKey());
        // 2 设置bucket的区域, COS地域在上面有提到
        ClientConfig clientConfig = new ClientConfig(new Region(cosConfig.getCosApiRegion()));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        cosClientMap.put(cosConfig.getHash(), cosclient);
        return cosclient;
    }
    public List<String> uploadFileBase64(Map<String, String> dataMap, CosConfig cosConfig) {
        List<String> keys = new ArrayList<>();
        COSClient cosClient = getClient(cosConfig);
        String result = "";
        if(cosClient == null) {
            return keys;
        }
        try {
            for (Map.Entry<String, String> stringStringEntry : dataMap.entrySet()) {
                result = uploadFileBase64Single(stringStringEntry.getValue(), stringStringEntry.getKey(), cosConfig);
                keys.add(stringStringEntry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
        }finally {
            //关闭客户端(关闭后台线程)
//            cosClient.shutdown();
        }
        return keys;
    }

    public List<String> uploadFile(Map<String, MultipartFile> dataMap, CosConfig cosConfig) {
        List<String> keys = new ArrayList<>();
        COSClient cosClient = getClient(cosConfig);
        String result = "";
        if(cosClient == null) {
            return keys;
        }
        try {
            for (Map.Entry<String, MultipartFile> stringStringEntry : dataMap.entrySet()) {
                result = uploadFileSingle(stringStringEntry.getValue(), stringStringEntry.getKey(), cosConfig);
                keys.add(stringStringEntry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
        }finally {
            //关闭客户端(关闭后台线程)
//            cosClient.shutdown();
        }
        return keys;
    }

    private String uploadInputStreamSingle(InputStream inputStream, String key, CosConfig cosConfig) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置 Content type, 默认是 application/octet-stream
        COSClient cosClient = getClient(cosConfig);
        objectMetadata.setContentType("image/png");
        //对象键（Key）是对象在存储桶中的唯一标识。
        //例如，在对象的访问域名https://ba-189629.cos.ap-beijing.myqcloud.com/app/img/bb/profile_big.jpg
        //则把key设为app/img/bb/profile_big.jpg  //云控制台目录//ba-189629-->app-->img-->bb
        PutObjectResult putObjectResult = null;
        try {
            cosClient.putObject(cosConfig.getCosApiBucketName(), key, inputStream, objectMetadata);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "";
    }
    private String uploadFileSingle(MultipartFile value, String key, CosConfig cosConfig) throws IOException {
        //转化为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(value.getBytes());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        COSClient cosClient = getClient(cosConfig);
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("image/png");
        //对象键（Key）是对象在存储桶中的唯一标识。
        //例如，在对象的访问域名https://ba-189629.cos.ap-beijing.myqcloud.com/app/img/bb/profile_big.jpg
        //则把key设为app/img/bb/profile_big.jpg  //云控制台目录//ba-189629-->app-->img-->bb
        PutObjectResult putObjectResult = cosClient.putObject(cosConfig.getCosApiBucketName(), key, inputStream, objectMetadata);
        String result = putObjectResult.getETag();  // 获取文件的 etag
        log.info("uploadFile==result=======result:{}",result);
        return result;
    }

    private String uploadFileBase64Single(String base64Str, String fileKey, CosConfig cosConfig) throws Exception {
        String base64compress = compressImageByFile(base64Str);
        byte[] bytes = Base64.getDecoder().decode(base64compress.trim());
        COSClient cosClient = getClient(cosConfig);
        //转化为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("image/png");
        //对象键（Key）是对象在存储桶中的唯一标识。
        //例如，在对象的访问域名https://ba-189629.cos.ap-beijing.myqcloud.com/app/img/bb/profile_big.jpg
        //则把key设为app/img/bb/profile_big.jpg  //云控制台目录//ba-189629-->app-->img-->bb
        PutObjectResult putObjectResult = cosClient.putObject(cosConfig.getCosApiBucketName(), fileKey, inputStream, objectMetadata);
        String result = putObjectResult.getETag();  // 获取文件的 etag
        log.info("uploadFileBase64==result=======result:{}",result);
        return result;
    }

    /**
     * 压缩jpg图片,返回base64字符串
     * sourceFile: 源文件;
     */
    public static String compressImageByFile(String inString) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(inString.trim());
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
    }// 创建 TransferManager 实例，这个实例用来后续调用高级接口

    TransferManager createTransferManager(CosConfig config) {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        COSClient cosClient = getClient(config);
        if(cosClient == null) {
            return null;
        }

        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        return transferManager;
    }
    void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }
    public String downloadToBase64(String key, List<CosConfig> cosConfigs) throws Exception {
        if (key.startsWith("http")) {
            URL url = new URL(key);
            InputStream is = url.openStream();
            byte[] data = null;
            try {
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = is.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                data = swapStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new Exception("输入流关闭异常");
                    }
                }
            }

            return Base64.getEncoder().encodeToString(data);
        }
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        if (key.startsWith("/profile")) {
            String localFilePath = alpgoConfig.getProfile() + key.replace("/profile", "");
            File downloadFile = new File(localFilePath);
            try {
                String result = fileToBase64(downloadFile);
                return result;
            } catch (Exception e) {
                log.info("本地未找到: {}", localFilePath);
            }
        }
        File downloadFile = new File(alpgoConfig.getProfile() + key);
        for (CosConfig cosConfig : cosConfigs) {
            TransferManager transferManager = createTransferManager(cosConfig);
            // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
            String bucketName = cosConfig.getCosApiBucketName();

            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            try {
                // 返回一个异步结果 Download, 可同步的调用 waitForCompletion 等待下载结束, 成功返回 void, 失败抛出异常
                Download download = transferManager.download(getObjectRequest, downloadFile);
                download.waitForCompletion();
            } catch (CosServiceException e) {
                e.printStackTrace();
            } catch (CosClientException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = fileToBase64(downloadFile);
            // 确定本进程不再使用 transferManager 实例之后，关闭之
            // 详细代码参见本页：高级接口 -> 关闭 TransferManager
            shutdownTransferManager(transferManager);
            return result;
        }
        throw new Exception("找不到对应文件，请检查文件路径: " + key);
    }

    private String getFileName(String key) {
        String[] split = key.split("/");
        if (split.length > 1) {
            return split[split.length - 1];
        }
        return key;
    }

    /**将 file 转化为 Base64*/
    public static String fileToBase64(File file) {
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.getEncoder().encodeToString(buffer);
        } catch (Exception e) {
            throw new RuntimeException("文件路径无效\n" + e.getMessage());
        }
    }

    public void uploadStream(String key, InputStream inputStream, List<CosConfig> cosConfigs) throws IOException {
        for (CosConfig cosConfig : cosConfigs) {
            uploadInputStreamSingle(inputStream, key, cosConfig);
        }
    }
}
