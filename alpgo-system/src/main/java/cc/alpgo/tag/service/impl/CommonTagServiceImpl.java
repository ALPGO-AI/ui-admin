package cc.alpgo.tag.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.alpgo.tag.mapper.CommonTagMapper;
import cc.alpgo.tag.domain.CommonTag;
import cc.alpgo.tag.service.ICommonTagService;

/**
 * 标签Service业务层处理
 *
 * @author marcus
 * @date 2023-05-12
 */
@Service
public class CommonTagServiceImpl implements ICommonTagService
{
    @Autowired
    private CommonTagMapper commonTagMapper;

    /**
     * 查询标签
     *
     * @param tagId 标签主键
     * @return 标签
     */
    @Override
    public CommonTag selectCommonTagByTagId(Long tagId)
    {
        return commonTagMapper.selectCommonTagByTagId(tagId);
    }

    /**
     * 查询标签列表
     *
     * @param commonTag 标签
     * @return 标签
     */
    @Override
    public List<CommonTag> selectCommonTagList(CommonTag commonTag)
    {
        return commonTagMapper.selectCommonTagList(commonTag);
    }

    /**
     * 新增标签
     *
     * @param commonTag 标签
     * @return 结果
     */
    @Override
    public int insertCommonTag(CommonTag commonTag)
    {
        return commonTagMapper.insertCommonTag(commonTag);
    }

    /**
     * 修改标签
     *
     * @param commonTag 标签
     * @return 结果
     */
    @Override
    public int updateCommonTag(CommonTag commonTag)
    {
        return commonTagMapper.updateCommonTag(commonTag);
    }

    /**
     * 批量删除标签
     *
     * @param tagIds 需要删除的标签主键
     * @return 结果
     */
    @Override
    public int deleteCommonTagByTagIds(Long[] tagIds)
    {
        return commonTagMapper.deleteCommonTagByTagIds(tagIds);
    }

    /**
     * 删除标签信息
     *
     * @param tagId 标签主键
     * @return 结果
     */
    @Override
    public int deleteCommonTagByTagId(Long tagId)
    {
        return commonTagMapper.deleteCommonTagByTagId(tagId);
    }
}
