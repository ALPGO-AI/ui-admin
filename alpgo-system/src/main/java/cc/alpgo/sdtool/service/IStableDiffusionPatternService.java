package cc.alpgo.sdtool.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cc.alpgo.common.enums.CosConfig;
import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;

/**
 * stable_diffusion_patternService接口
 *
 * @author marcus
 * @date 2023-03-21
 */
public interface IStableDiffusionPatternService
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
     * 批量删除stable_diffusion_pattern
     *
     * @param patternIds 需要删除的stable_diffusion_pattern主键集合
     * @return 结果
     */
    public int deleteStableDiffusionPatternByPatternIds(Long[] patternIds);

    /**
     * 删除stable_diffusion_pattern信息
     *
     * @param patternId stable_diffusion_pattern主键
     * @return 结果
     */
    public int deleteStableDiffusionPatternByPatternId(Long patternId);

    public StableDiffusionPattern generateByPatternId(Map<String, String> params, Long patternId, List<Map<String, Object>> extraGenerateParams) throws Exception;

    StableDiffusionOutput generateByPatternIdAsync(Long patternId, List<CosConfig> cosConfigs, StableDiffusionEnv sdEnv, String wsId, Map<String, Object> extraGenerateParams) throws Exception;

    List<String> selectAllRelatedOutputImageUrls(Long patternId);

    public StableDiffusionApiResponse generateByImg(Map<String, String> params, Long patternId) throws IOException;

    List<StableDiffusionPattern> selectStableDiffusionPatternListByPatternIds(List<Long> toArray);

    StableDiffusionPattern selectStableDiffusionPatternByAuthCode(String authCode);
}
