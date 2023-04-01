package cc.alpgo.system.service;

import java.util.List;
import java.util.Map;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.system.domain.Image;

/**
 * imageService接口
 * 
 * @author marcus
 * @date 2023-03-31
 */
public interface IImageService 
{
    /**
     * 查询image
     * 
     * @param imageId image主键
     * @return image
     */
    public Image selectImageByImageId(Long imageId);

    /**
     * 查询image列表
     * 
     * @param image image
     * @return image集合
     */
    public List<Image> selectImageList(Image image);

    /**
     * 新增image
     * 
     * @param image image
     * @return 结果
     */
    public int insertImage(Image image);

    /**
     * 修改image
     * 
     * @param image image
     * @return 结果
     */
    public int updateImage(Image image);

    /**
     * 批量删除image
     * 
     * @param imageIds 需要删除的image主键集合
     * @return 结果
     */
    public int deleteImageByImageIds(Long[] imageIds);

    /**
     * 删除image信息
     * 
     * @param imageId image主键
     * @return 结果
     */
    public int deleteImageByImageId(Long imageId);

    Map<Long, String> selectUrls(List<Long> imageIds, List<CosConfig> cosConfigs);
    Map<Long, String> selectUrls(List<Long> imageIds, CosConfig cosConfigs);
}
