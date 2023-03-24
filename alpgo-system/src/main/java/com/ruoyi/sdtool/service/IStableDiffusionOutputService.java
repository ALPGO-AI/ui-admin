package com.alpgo.sdtool.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alpgo.sdtool.domain.StableDiffusionOutput;
import com.alpgo.sdtool.domain.StableDiffusionPattern;
import com.alpgo.sdtool.util.StableDiffusionApiResponse;

/**
 * stable_diffusion_outputService接口
 *
 * @author marcus
 * @date 2023-03-23
 */
public interface IStableDiffusionOutputService
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
     * 批量删除stable_diffusion_output
     *
     * @param outputIds 需要删除的stable_diffusion_output主键集合
     * @return 结果
     */
    public int deleteStableDiffusionOutputByOutputIds(Long[] outputIds);

    /**
     * 删除stable_diffusion_output信息
     *
     * @param outputId stable_diffusion_output主键
     * @return 结果
     */
    public int deleteStableDiffusionOutputByOutputId(Long outputId);

    StableDiffusionOutput generateOutput(StableDiffusionPattern stableDiffusionPattern, StableDiffusionApiResponse response, String type);

    StableDiffusionOutput generateByPatternId(Map<String, String> params, Long outputId) throws IOException;

    StableDiffusionOutput generateSketchBySampleImgFromOutput(Map<String, String> params, Long outputId) throws IOException;
}
