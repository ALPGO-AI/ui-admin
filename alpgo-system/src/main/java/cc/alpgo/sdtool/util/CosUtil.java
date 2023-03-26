package cc.alpgo.sdtool.util;

import cc.alpgo.common.config.AlpgoConfig;
import cc.alpgo.common.utils.uuid.UUID;
import cc.alpgo.sdtool.util.event.UploadToCosEvent;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public String uploadAsync(String image) {
        String key = generateFileName();
        applicationContext.publishEvent(new UploadToCosEvent(key, image));
        return key;
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
    }// 创建 TransferManager 实例，这个实例用来后续调用高级接口

    TransferManager createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        COSClient cosClient = getClient();
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
    public String downloadToBase64(String key) {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();

        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = alpgoConfig.getCosApiBucketName();
        // 本地文件路径
        String localFilePath = alpgoConfig.getProfile() + key;
        File downloadFile = new File(localFilePath);

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
}