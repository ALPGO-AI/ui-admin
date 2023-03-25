package cc.alpgo.sdtool.util;

import cc.alpgo.sdtool.constant.ApiContants;
import com.google.gson.Gson;

import java.util.*;

/**
 * This class is used to build parameters for the Stable Diffusion API.
 */
public class StableDiffusionApiParams {
    // Enable HR
    private boolean enable_hr;
    // Denoising strength
    private float denoising_strength;
    // First phase width
    private int firstphase_width;
    // First phase height
    private int firstphase_height;
    // HR scale
    private float hr_scale;
    // HR upscaler
    private String hr_upscaler;
    // HR second pass steps
    private int hr_second_pass_steps;
    // HR resize x
    private int hr_resize_x;
    // HR resize y
    private int hr_resize_y;
    // Prompt
    private String prompt;
    // Styles
    private List<String> styles;
    // Seed
    private int seed;
    // Subseed
    private int subseed;
    // Subseed strength
    private float subseed_strength;
    // Seed resize from h
    private int seed_resize_from_h;
    // Seed resize from w
    private int seed_resize_from_w;
    // Sampler name
    private String sampler_name;
    // Batch size
    private int batch_size;
    // N iter
    private int n_iter;
    // Steps
    private int steps;
    // Cfg scale
    private float cfg_scale;
    // Width
    private int width;
    // Height
    private int height;
    // Restore faces
    private boolean restore_faces;
    // Tiling
    private boolean tiling;
    // Negative prompt
    private String negative_prompt;
    // Eta
    private float eta;
    // S churn
    private float s_churn;
    // S tmax
    private float s_tmax;
    // S tmin
    private float s_tmin;
    // S noise
    private float s_noise;
    // Override settings
    private Object override_settings;
    // Override settings restore afterwards
    private boolean override_settings_restore_afterwards;
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
        Map<String, String> map = new Gson().fromJson(parametersJson, HashMap.class);
        this.sampler_index = map.getOrDefault("sampler", "Euler a");
        this.script_name = null;
        this.enable_hr = true;
        this.denoising_strength = 0.6f;
        this.firstphase_width = 0;
        this.firstphase_height = 0;
        this.hr_scale = 2;
        this.hr_upscaler = "Latent";
        this.hr_second_pass_steps = 0;
        this.hr_resize_x = 0;
        this.hr_resize_y = 0;
        this.prompt = positiveprompt;
        this.styles = new ArrayList<>();
        this.seed = Integer.parseInt(seed);
        this.subseed = -1;
        this.subseed_strength = 0;
        this.seed_resize_from_h = -1;
        this.seed_resize_from_w = -1;
        this.sampler_name = "";
        this.batch_size = 1;
        this.n_iter = 1;
        this.steps = Integer.parseInt(map.getOrDefault("steps", "30"));
        this.cfg_scale = Float.parseFloat(map.getOrDefault("CFG", "7"));
        this.width = 512;
        this.height = 512;
        this.restore_faces = false;
        this.tiling = false;
        this.negative_prompt = negativeprompt;
        this.eta = 0;
        this.s_churn = 0;
        this.s_tmax = 0;
        this.s_tmin = 0;
        this.s_noise = 1;
        this.override_settings = new Object();
        this.override_settings_restore_afterwards = true;
        this.script_args = new ArrayList<>();
    }

    public String toPreDict(String sessionHash) {
        return new Gson().toJson(this);
    }

    public String toPreDictForSketch(String fileName, String sessionHash) {
        return new Gson().toJson(this);
    }
    public String toPreDictForControlNet(String base64ImgString, String sessionHash) {
        return new Gson().toJson(this);
    }
}
