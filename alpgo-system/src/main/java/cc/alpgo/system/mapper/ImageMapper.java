package cc.alpgo.system.mapper;

import java.util.List;
import java.util.Map;

import cc.alpgo.system.domain.Image;
import cc.alpgo.system.domain.ImageProvider;
import cc.alpgo.system.domain.vo.ImageIdUrlVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

/**
 * imageMapper接口
 * 
 * @author marcus
 * @date 2023-03-31
 */
public interface ImageMapper 
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
     * 删除image
     * 
     * @param imageId image主键
     * @return 结果
     */
    public int deleteImageByImageId(Long imageId);

    /**
     * 批量删除image
     * 
     * @param imageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteImageByImageIds(Long[] imageIds);

    /**
     * 批量删除image_provider
     * 
     * @param imageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteImageProviderByImageIds(Long[] imageIds);
    
    /**
     * 批量新增image_provider
     * 
     * @param imageProviderList image_provider列表
     * @return 结果
     */
    public int batchImageProvider(List<ImageProvider> imageProviderList);
    

    /**
     * 通过image主键删除image_provider信息
     * 
     * @param imageId imageID
     * @return 结果
     */
    public int deleteImageProviderByImageId(Long imageId);
    List<ImageIdUrlVO> selectImageUrlsByImageIdsAndEnvId(@Param("imageIds") List<Long> imageIds,@Param("envId") Long envId);

    List<ImageIdUrlVO> selectImageUrlsRandomCountAndEnvId(@Param("imageCount") int count, @Param("envId") Long envId);
}
