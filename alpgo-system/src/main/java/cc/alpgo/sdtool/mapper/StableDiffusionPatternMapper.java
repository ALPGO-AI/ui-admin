package cc.alpgo.sdtool.mapper;

import java.util.List;

import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import org.apache.ibatis.annotations.Param;

/**
 * stable_diffusion_patternMapper接口
 *
 * @author marcus
 * @date 2023-03-21
 */
public interface StableDiffusionPatternMapper
{
    /**
     * 查询stable_diffusion_pattern
     *
     * @param patternId stable_diffusion_pattern主键
     * @return stable_diffusion_pattern
     */
    public StableDiffusionPattern selectStableDiffusionPatternByPatternId(Long patternId);

    /**
     * 查询stable_diffusion_pattern列表
     *
     * @param stableDiffusionPattern stable_diffusion_pattern
     * @return stable_diffusion_pattern集合
     */
    public List<StableDiffusionPattern> selectStableDiffusionPatternList(StableDiffusionPattern stableDiffusionPattern);

    /**
     * 新增stable_diffusion_pattern
     *
     * @param stableDiffusionPattern stable_diffusion_pattern
     * @return 结果
     */
    public int insertStableDiffusionPattern(StableDiffusionPattern stableDiffusionPattern);

    /**
     * 修改stable_diffusion_pattern
     *
     * @param stableDiffusionPattern stable_diffusion_pattern
     * @return 结果
     */
    public int updateStableDiffusionPattern(StableDiffusionPattern stableDiffusionPattern);

    /**
     * 删除stable_diffusion_pattern
     *
     * @param patternId stable_diffusion_pattern主键
     * @return 结果
     */
    public int deleteStableDiffusionPatternByPatternId(Long patternId);

    /**
     * 批量删除stable_diffusion_pattern
     *
     * @param patternIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStableDiffusionPatternByPatternIds(Long[] patternIds);

    List<StableDiffusionPattern> selectStableDiffusionPatternListByPatternIds(@Param("patternIds") List<Long> ids);

    void updateStableDiffusionPatternSampleImage(StableDiffusionPattern stableDiffusionPattern);
}
