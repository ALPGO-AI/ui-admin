package cc.alpgo.system.service.impl;

import java.util.HashMap;
import java.util.List;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.DateUtils;
import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.system.domain.vo.ImageIdUrlVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import cc.alpgo.system.domain.ImageProvider;
import cc.alpgo.system.mapper.ImageMapper;
import cc.alpgo.system.domain.Image;
import cc.alpgo.system.service.IImageService;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * imageService业务层处理
 *
 * @author marcus
 * @date 2023-03-31
 */
@Service
public class ImageServiceImpl implements IImageService
{
    @Autowired
    private ImageMapper imageMapper;

    /**
     * 查询image
     *
     * @param imageId image主键
     * @return image
     */
    @Override
    public Image selectImageByImageId(Long imageId)
    {
        return imageMapper.selectImageByImageId(imageId);
    }

    /**
     * 查询image列表
     *
     * @param image image
     * @return image
     */
    @Override
    public List<Image> selectImageList(Image image)
    {
        return imageMapper.selectImageList(image);
    }

    /**
     * 新增image
     *
     * @param image image
     * @return 结果
     */
    @Transactional
    @Override
    public int insertImage(Image image)
    {
        image.setCreateTime(DateUtils.getNowDate());
        int rows = imageMapper.insertImage(image);
        insertImageProvider(image);
        return rows;
    }

    /**
     * 修改image
     *
     * @param image image
     * @return 结果
     */
    @Transactional
    @Override
    public int updateImage(Image image)
    {
        image.setUpdateTime(DateUtils.getNowDate());
        imageMapper.deleteImageProviderByImageId(image.getImageId());
        insertImageProvider(image);
        return imageMapper.updateImage(image);
    }

    /**
     * 批量删除image
     *
     * @param imageIds 需要删除的image主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteImageByImageIds(Long[] imageIds)
    {
        imageMapper.deleteImageProviderByImageIds(imageIds);
        return imageMapper.deleteImageByImageIds(imageIds);
    }

    /**
     * 删除image信息
     *
     * @param imageId image主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteImageByImageId(Long imageId)
    {
        imageMapper.deleteImageProviderByImageId(imageId);
        return imageMapper.deleteImageByImageId(imageId);
    }

    @Override
    public Map<Long, String> selectUrls(List<Long> imageIds, List<CosConfig> cosConfigs) {
        if (isEmpty(cosConfigs)) {
            return new HashMap<>();
        }
        return selectUrls(imageIds, cosConfigs.get(0));
    }

    @Override
    public Map<Long, String> selectUrls(List<Long> imageIds, CosConfig cosConfig) {
        List<ImageIdUrlVO> list = imageMapper.selectImageUrlsByImageIdsAndEnvId(imageIds, cosConfig.getEnvId());
        return list.stream().collect(Collectors.toMap(item -> item.getImageId(), item -> item.getUrl()));
    }

    @Override
    public Map<Long, String> selectUrlsRandom(int count, List<CosConfig> activeConfigs) {
        if (CollectionUtils.isEmpty(activeConfigs)) {
            return new HashMap<>();
        }
        CosConfig cosConfig = activeConfigs.get(0);
        List<ImageIdUrlVO> list = imageMapper.selectImageUrlsRandomCountAndEnvId(1, cosConfig.getEnvId());
        return list.stream().collect(Collectors.toMap(item -> item.getImageId(), item -> item.getUrl()));
    }

    /**
     * 新增image_provider信息
     *
     * @param image image对象
     */
    public void insertImageProvider(Image image)
    {
        List<ImageProvider> imageProviderList = image.getImageProviderList();
        Long imageId = image.getImageId();
        if (StringUtils.isNotNull(imageProviderList))
        {
            List<ImageProvider> list = new ArrayList<ImageProvider>();
            for (ImageProvider imageProvider : imageProviderList)
            {
                imageProvider.setImageId(imageId);
                list.add(imageProvider);
            }
            if (list.size() > 0)
            {
                imageMapper.batchImageProvider(list);
            }
        }
    }
}
