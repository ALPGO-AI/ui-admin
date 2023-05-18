package cc.alpgo.tag.mapper;

import java.util.List;
import cc.alpgo.tag.domain.CommonTag;

/**
 * 标签Mapper接口
 *
 * @author marcus
 * @date 2023-05-12
 */
public interface CommonTagMapper
{
    /**
     * 查询标签
     *
     * @param tagId 标签主键
     * @return 标签
     */
    public CommonTag selectCommonTagByTagId(Long tagId);

    /**
     * 查询标签列表
     *
     * @param commonTag 标签
     * @return 标签集合
     */
    public List<CommonTag> selectCommonTagList(CommonTag commonTag);

    /**
     * 新增标签
     *
     * @param commonTag 标签
     * @return 结果
     */
    public int insertCommonTag(CommonTag commonTag);

    /**
     * 修改标签
     *
     * @param commonTag 标签
     * @return 结果
     */
    public int updateCommonTag(CommonTag commonTag);

    /**
     * 删除标签
     *
     * @param tagId 标签主键
     * @return 结果
     */
    public int deleteCommonTagByTagId(Long tagId);

    /**
     * 批量删除标签
     *
     * @param tagIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCommonTagByTagIds(Long[] tagIds);
}
