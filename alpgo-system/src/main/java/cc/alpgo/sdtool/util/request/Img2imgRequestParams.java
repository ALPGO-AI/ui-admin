package cc.alpgo.sdtool.util.request;

import cc.alpgo.common.utils.StringUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class Img2imgRequestParams {
    private List<String> init_images;
    private double resize_mode;
    private double denoising_strength;
    private double image_cfg_scale;
    private String mask;
    private double mask_blur;
    private double inpainting_fill;
    private boolean inpaint_full_res;
    private double inpaint_full_res_padding;
    private double inpainting_mask_invert;
    private double initial_noise_multiplier;
    private String prompt;
    private List<String> styles;
    private double seed;
    private double subseed;
    private double subseed_strength;
    private double seed_resize_from_h;
    private double seed_resize_from_w;
    private String sampler_name;
    private double batch_size;
    private double n_iter;
    private double steps;
    private double cfg_scale;
    private double width;
    private double height;
    private boolean restore_faces;
    private boolean tiling;
    private String negative_prompt;
    private double eta;
    private double s_churn;
    private double s_tmax;
    private double s_tmin;
    private double s_noise;
    private Map<String, Object> override_settings;
    private boolean override_settings_restore_afterwards;
    private List<String> script_args;
    private String sampler_index;
    private boolean include_init_images;
    private String script_name;
    public Img2imgRequestParams(Map<String, Object> map) {
        this.init_images = (List<String>) map.get("init_images");
        this.resize_mode = (double) map.get("resize_mode");
        this.denoising_strength = (double) map.get("denoising_strength");
        this.image_cfg_scale = (double) map.get("image_cfg_scale");
        this.mask = (String) map.get("mask");
        this.mask_blur = (double) map.get("mask_blur");
        this.inpainting_fill = (double) map.get("inpainting_fill");
        this.inpaint_full_res = (boolean) map.get("inpaint_full_res");
        this.inpaint_full_res_padding = (double) map.get("inpaint_full_res_padding");
        this.inpainting_mask_invert = (double) map.get("inpainting_mask_invert");
        this.initial_noise_multiplier = (double) map.get("initial_noise_multiplier");
        this.prompt = (String) map.get("prompt");
        this.styles = (List<String>) map.get("styles");
        this.seed = (double) map.get("seed");
        this.subseed = (double) map.get("subseed");
        this.subseed_strength = (double) map.get("subseed_strength");
        this.seed_resize_from_h = (double) map.get("seed_resize_from_h");
        this.seed_resize_from_w = (double) map.get("seed_resize_from_w");
        this.sampler_name = (String) map.get("sampler_name");
        this.batch_size = (double) map.get("batch_size");
        this.n_iter = (double) map.get("n_iter");
        this.steps = (double) map.get("steps");
        this.cfg_scale = (double) map.get("cfg_scale");
        this.width = (double) map.get("width");
        this.height = (double) map.get("height");
        this.restore_faces = (boolean) map.get("restore_faces");
        this.tiling = (boolean) map.get("tiling");
        this.negative_prompt = (String) map.get("negative_prompt");
        this.eta = (double) map.get("eta");
        this.s_churn = (double) map.get("s_churn");
        this.s_tmax = (double) map.get("s_tmax");
        this.s_tmin = (double) map.get("s_tmin");
        this.s_noise = (double) map.get("s_noise");
        this.override_settings = (Map<String, Object>) map.get("override_settings");
        this.override_settings_restore_afterwards = (boolean) map.get("override_settings_restore_afterwards");
        this.script_args = (List<String>) map.get("script_args");
        this.sampler_index = (String) map.get("sampler_index");
        this.include_init_images = (boolean) map.get("include_init_images");
        this.script_name = (String) map.get("script_name");
    }

    public List<String> getInit_images() {
        return init_images;
    }

    public void setInit_images(List<String> init_images) {
        this.init_images = init_images;
    }

    public double getResize_mode() {
        return resize_mode;
    }

    public void setResize_mode(double resize_mode) {
        this.resize_mode = resize_mode;
    }

    public double getDenoising_strength() {
        return denoising_strength;
    }

    public void setDenoising_strength(double denoising_strength) {
        this.denoising_strength = denoising_strength;
    }

    public double getImage_cfg_scale() {
        return image_cfg_scale;
    }

    public void setImage_cfg_scale(double image_cfg_scale) {
        this.image_cfg_scale = image_cfg_scale;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public double getMask_blur() {
        return mask_blur;
    }

    public void setMask_blur(double mask_blur) {
        this.mask_blur = mask_blur;
    }

    public double getInpainting_fill() {
        return inpainting_fill;
    }

    public void setInpainting_fill(double inpainting_fill) {
        this.inpainting_fill = inpainting_fill;
    }

    public boolean isInpaint_full_res() {
        return inpaint_full_res;
    }

    public void setInpaint_full_res(boolean inpaint_full_res) {
        this.inpaint_full_res = inpaint_full_res;
    }

    public double getInpaint_full_res_padding() {
        return inpaint_full_res_padding;
    }

    public void setInpaint_full_res_padding(double inpaint_full_res_padding) {
        this.inpaint_full_res_padding = inpaint_full_res_padding;
    }

    public double getInpainting_mask_invert() {
        return inpainting_mask_invert;
    }

    public void setInpainting_mask_invert(double inpainting_mask_invert) {
        this.inpainting_mask_invert = inpainting_mask_invert;
    }

    public double getInitial_noise_multiplier() {
        return initial_noise_multiplier;
    }

    public void setInitial_noise_multiplier(double initial_noise_multiplier) {
        this.initial_noise_multiplier = initial_noise_multiplier;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }

    public double getSeed() {
        return seed;
    }

    public void setSeed(double seed) {
        this.seed = seed;
    }

    public double getSubseed() {
        return subseed;
    }

    public void setSubseed(double subseed) {
        this.subseed = subseed;
    }

    public double getSubseed_strength() {
        return subseed_strength;
    }

    public void setSubseed_strength(double subseed_strength) {
        this.subseed_strength = subseed_strength;
    }

    public double getSeed_resize_from_h() {
        return seed_resize_from_h;
    }

    public void setSeed_resize_from_h(double seed_resize_from_h) {
        this.seed_resize_from_h = seed_resize_from_h;
    }

    public double getSeed_resize_from_w() {
        return seed_resize_from_w;
    }

    public void setSeed_resize_from_w(double seed_resize_from_w) {
        this.seed_resize_from_w = seed_resize_from_w;
    }

    public String getSampler_name() {
        return sampler_name;
    }

    public void setSampler_name(String sampler_name) {
        this.sampler_name = sampler_name;
    }

    public double getBatch_size() {
        return batch_size;
    }

    public void setBatch_size(double batch_size) {
        this.batch_size = batch_size;
    }

    public double getN_iter() {
        return n_iter;
    }

    public void setN_iter(double n_iter) {
        this.n_iter = n_iter;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }

    public double getCfg_scale() {
        return cfg_scale;
    }

    public void setCfg_scale(double cfg_scale) {
        this.cfg_scale = cfg_scale;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isRestore_faces() {
        return restore_faces;
    }

    public void setRestore_faces(boolean restore_faces) {
        this.restore_faces = restore_faces;
    }

    public boolean isTiling() {
        return tiling;
    }

    public void setTiling(boolean tiling) {
        this.tiling = tiling;
    }

    public String getNegative_prompt() {
        return negative_prompt;
    }

    public void setNegative_prompt(String negative_prompt) {
        this.negative_prompt = negative_prompt;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public double getS_churn() {
        return s_churn;
    }

    public void setS_churn(double s_churn) {
        this.s_churn = s_churn;
    }

    public double getS_tmax() {
        return s_tmax;
    }

    public void setS_tmax(double s_tmax) {
        this.s_tmax = s_tmax;
    }

    public double getS_tmin() {
        return s_tmin;
    }

    public void setS_tmin(double s_tmin) {
        this.s_tmin = s_tmin;
    }

    public double getS_noise() {
        return s_noise;
    }

    public void setS_noise(double s_noise) {
        this.s_noise = s_noise;
    }

    public Map<String, Object> getOverride_settings() {
        return override_settings;
    }

    public void setOverride_settings(Map<String, Object> override_settings) {
        this.override_settings = override_settings;
    }

    public boolean isOverride_settings_restore_afterwards() {
        return override_settings_restore_afterwards;
    }

    public void setOverride_settings_restore_afterwards(boolean override_settings_restore_afterwards) {
        this.override_settings_restore_afterwards = override_settings_restore_afterwards;
    }

    public List<String> getScript_args() {
        return script_args;
    }

    public void setScript_args(List<String> script_args) {
        this.script_args = script_args;
    }

    public String getSampler_index() {
        return sampler_index;
    }

    public void setSampler_index(String sampler_index) {
        this.sampler_index = sampler_index;
    }

    public boolean isInclude_init_images() {
        return include_init_images;
    }

    public void setInclude_init_images(boolean include_init_images) {
        this.include_init_images = include_init_images;
    }

    public String getScript_name() {
        return script_name;
    }

    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }

    public String toRequestBody() {
        this.script_name = StringUtils.isEmpty(script_name) ? null : script_name;
        return new Gson().toJson(this);
    }
}
