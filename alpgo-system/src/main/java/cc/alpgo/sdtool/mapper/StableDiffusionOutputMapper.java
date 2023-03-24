package cc.alpgo.sdtool.mapper;

import java.util.List;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;

/**
 * stable_diffusion_outputMapper接口
 *
 * @author marcus
 * @date 2023-03-23
 */
public interface StableDiffusionOutputMapper
{
    /**
     * 查询stable_diffusion_output
     *
     * @param outputId stable_diffusion_output主键
     * @return stable_diffusion_output
     */
    public StableDiffusionOutput selectStableDiffusionOutputByOutputId(Long outputId);

    /**
     * 查询stable_diffusion_output列表
     *
     * @param stableDiffusionOutput stable_diffusion_output
     * @return stable_diffusion_output集合
     */
    public List<StableDiffusionOutput> selectStableDiffusionOutputList(StableDiffusionOutput stableDiffusionOutput);

    /**
     * 新增stable_diffusion_output
     *
     * @param stableDiffusionOutput stable_diffusion_output
     * @return 结果
     */
    public int insertStableDiffusionOutput(StableDiffusionOutput stableDiffusionOutput);

    /**
     * 修改stable_diffusion_output
     *
     * @param stableDiffusionOutput stable_diffusion_output
     * @return 结果
     */
    public int updateStableDiffusionOutput(StableDiffusionOutput stableDiffusionOutput);

    /**
     * 删除stable_diffusion_output
     *
     * @param outputId stable_diffusion_output主键
     * @return 结果
     */
    public int deleteStableDiffusionOutputByOutputId(Long outputId);

    /**
     * 批量删除stable_diffusion_output
     *
     * @param outputIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStableDiffusionOutputByOutputIds(Long[] outputIds);
}
