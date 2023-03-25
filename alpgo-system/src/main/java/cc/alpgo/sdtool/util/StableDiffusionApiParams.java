package cc.alpgo.sdtool.util;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.*;

/**
 * This class is used to build parameters for the Stable Diffusion API.
 */
public class StableDiffusionApiParams {
    // Enable HR
    private Boolean enable_hr;
    // Denoising strength
    private Float denoising_strength;
    // First phase width
    private Integer firstphase_width;
    // First phase height
    private Integer firstphase_height;
    // HR scale
    private Float hr_scale;
    // HR upscaler
    private String hr_upscaler;
    // HR second pass steps
    private Integer hr_second_pass_steps;
    // HR resize x
    private Integer hr_resize_x;
    // HR resize y
    private Integer hr_resize_y;
    // Prompt
    private String prompt;
    // Styles
    private List<String> styles;
    // Seed
    private Integer seed;
    // Subseed
    private Integer subseed;
    // Subseed strength
    private Float subseed_strength;
    // Seed resize from h
    private Integer seed_resize_from_h;
    // Seed resize from w
    private Integer seed_resize_from_w;
    // Sampler name
    private String sampler_name;
    // Batch size
    private Integer batch_size;
    // N iter
    private Integer n_iter;
    // Steps
    private Integer steps;
    // Cfg scale
    private Float cfg_scale;
    // Width
    private Integer width;
    // Height
    private Integer height;
    // Restore faces
    private Boolean restore_faces;
    // Tiling
    private Boolean tiling;
    // Negative prompt
    private String negative_prompt;
    // Eta
    private Float eta;
    // S churn
    private Float s_churn;
    // S tmax
    private Float s_tmax;
    // S tmin
    private Float s_tmin;
    // S noise
    private Float s_noise;
    // Override settings
    private Object override_settings;
    // Override settings restore afterwards
    private Boolean override_settings_restore_afterwards;
    // Script args
    private List<Object> script_args;
    // Sampler index
    private String sampler_index;
    // Script name
    private String script_name;
    /**
     * Constructor for StableDiffusionApiParamsBuilder.
     */
    public StableDiffusionApiParams(String positiveprompt, String negativeprompt, String parametersJson, String seed) {
        Map<String, Object> map = new Gson().fromJson(parametersJson, HashMap.class);
        this.sampler_index = (String) map.getOrDefault("sampler", "Euler a");
        this.script_name = null;
        this.enable_hr = (Boolean) map.getOrDefault("enable_hr", Boolean.FALSE);
        this.denoising_strength = 0.6f;
        this.firstphase_width = 0;
        this.firstphase_height = 0;
        this.hr_scale = 2f;
        this.hr_upscaler = "Latent";
        this.hr_second_pass_steps = 0;
        this.hr_resize_x = 0;
        this.hr_resize_y = 0;
        this.prompt = positiveprompt;
        this.styles = new ArrayList<>();
        this.seed = Integer.parseInt(seed);
        this.subseed = -1;
        this.subseed_strength = 0f;
        this.seed_resize_from_h = -1;
        this.seed_resize_from_w = -1;
        this.sampler_name = "";
        this.batch_size = 1;
        this.n_iter = 1;
        this.steps = BigDecimal.valueOf((Double)map.getOrDefault("steps", 30)).intValue();
        this.cfg_scale = BigDecimal.valueOf((Double)map.getOrDefault("CFG", 7)).floatValue();
        this.width = 512;
        this.height = 512;
        this.restore_faces = false;
        this.tiling = false;
        this.negative_prompt = negativeprompt;
        this.eta = 0f;
        this.s_churn = 0f;
        this.s_tmax = 0f;
        this.s_tmin = 0f;
        this.s_noise = 1f;
        this.override_settings = new Object();
        this.override_settings_restore_afterwards = true;
        this.script_args = new ArrayList<>();
    }

    private Boolean tryBoolean(Map<String, String> map, String key) {
        String booleanValue = map.get(key);
        boolean result = false;
        try {
            result = Boolean.parseBoolean(booleanValue);
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    public String toRequestBody() {
        return new Gson().toJson(this);
    }
}
