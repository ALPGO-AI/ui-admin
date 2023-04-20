package cc.alpgo.system.utils;

import cc.alpgo.common.domain.FileNameVO;
import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.sdtool.util.StableDiffusionApiUtil;
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.domain.ImageProvider;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ImageBuilder {
    public static List<Image> build(StableDiffusionEnv sdEnv, List<CosConfig> cosConfigs, List<FileNameVO> fileNames) throws UnsupportedEncodingException {
        List<Image> images = new ArrayList<>();
        for (FileNameVO fileName : fileNames) {
            Image image = new Image();
            image.setUri(fileName.getFileName());
            image.setImageProviderList(generateImageProviderList(sdEnv, cosConfigs, fileName));
            images.add(image);
        }
        return images;
    }

    public static Image build(List<CosConfig> cosConfigs, FileNameVO fileName) throws UnsupportedEncodingException {
        Image image = new Image();
        image.setUri(fileName.getFileName());
        image.setImageProviderList(generateImageProviderList(null, cosConfigs, fileName));
        return image;
    }

    private static List<ImageProvider> generateImageProviderList(StableDiffusionEnv sdEnv, List<CosConfig> cosConfigs, FileNameVO fileName) throws UnsupportedEncodingException {
        List<ImageProvider> result = new ArrayList<>();
        if (sdEnv != null) {
            ImageProvider imageProvider = new ImageProvider();
            imageProvider.setEnvId(sdEnv.getEnvId());
            imageProvider.setUrl(StableDiffusionApiUtil.getWebUIDownloadUrl(sdEnv, fileName.getFileName()));
            result.add(imageProvider);
        }
        for (CosConfig cosConfig : cosConfigs) {
            ImageProvider imageProviderCos = new ImageProvider();
            imageProviderCos.setEnvId(cosConfig.getEnvId());
            imageProviderCos.setUrl(CosUtil.getFullUrl(cosConfig, CosUtil.toKey(fileName)));
            result.add(imageProviderCos);
        }
        return result;
    }
}
