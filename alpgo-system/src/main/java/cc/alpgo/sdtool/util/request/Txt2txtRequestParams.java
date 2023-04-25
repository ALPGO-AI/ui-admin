package cc.alpgo.sdtool.util.request;

import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.sdtool.domain.ControlNetRequestBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

import static cc.alpgo.sdtool.constant.ApiContants.*;

/**
 * This class is used to build parameters for the Stable Diffusion API.
 */
public class Txt2txtRequestParams {
    // Enable HR
    private boolean enable_hr;
    // Denoising strength
    private double denoising_strength;
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
    private long seed;
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
    private double cfg_scale;
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
    // ControlNet params
    private String controlnet_input_image;
    // Script name
    private String controlnet_module;
    // Script name
    private String controlnet_model;
    // modelVersionMap
    private Map<String, String> modelVersionMap;
    private String session_hash;
    /**
     * Constructor for StableDiffusionApiParamsBuilder.
     */
    public Txt2txtRequestParams(String session_hash, String positiveprompt, String negativeprompt,
                                Map<String, Object> map) {
        this.session_hash = session_hash;
        this.modelVersionMap = (LinkedHashMap) map.getOrDefault("modelVersionMap", new LinkedHashMap<>());
        this.sampler_index = (String) map.getOrDefault("sampler", "Euler a");
        this.script_name = null;
        this.enable_hr = convertToBoolean(map.getOrDefault("enable_hr", false));;
        this.denoising_strength = convertToDouble(map.getOrDefault("denoising_strength", 0.6f));
        this.firstphase_width = 0;
        this.firstphase_height = 0;
        this.hr_scale = 2;
        this.hr_upscaler = "Latent";
        this.hr_second_pass_steps = 0;
        this.hr_resize_x = 0;
        this.hr_resize_y = 0;
        this.prompt = positiveprompt == null ? "" : positiveprompt;
        this.styles = new ArrayList<>();
        this.seed = convertToLong(map.getOrDefault("seed", -1));
        this.subseed = -1;
        this.subseed_strength = 0;
        this.seed_resize_from_h = -1;
        this.seed_resize_from_w = -1;
        this.sampler_name = (String) map.getOrDefault("sampler", "Euler a");
        this.batch_size = convertToInteger(map.getOrDefault("batch_size", 1));
        this.n_iter = 1;
        this.steps = convertToInteger(map.getOrDefault("steps", 30));
        this.cfg_scale = convertToDouble(map.getOrDefault("CFG", 7));
        this.width = convertToInteger(map.getOrDefault("width", 512));
        this.height = convertToInteger(map.getOrDefault("height", 512));
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

    private long convertToLong(Object cfg) {
        if (cfg instanceof Long) {
            return (Long) cfg;
        }
        if (cfg instanceof Integer) {
            return ((Integer) cfg).longValue();
        }
        return Long.parseLong((String) cfg);
    }

    private double convertToDouble(Object cfg) {
        if (cfg instanceof Double) {
            return (Double) cfg;
        }
        if (cfg instanceof Integer) {
            return ((Integer) cfg).doubleValue();
        }
        return Double.parseDouble((String) cfg);
    }

    private int convertToInteger(Object steps) {
        if (steps instanceof Double) {
            return ((Double) steps).intValue();
        }
        if (steps instanceof Integer) {
            return (Integer) steps;
        }
        int v = Integer.parseInt((String) steps);
        return v;
    }
    private boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return value != null && ((String) value).toLowerCase().equals("true");
        }
        return false;
    }

    public Txt2txtRequestParams(String sessionHash, String positivePrompt,
                                String negativePrompt,
                                Map<String, Object> parametersJsonMap,
                                String image,
                                String module,
                                String model) {
        this(sessionHash, positivePrompt, negativePrompt, parametersJsonMap);
        this.session_hash = sessionHash;
        this.controlnet_input_image = image;
        this.controlnet_module = module;
        this.controlnet_model = model;
    }

    public String toPreDict(StableDiffusionEnv env) {
        String sessionHash = this.session_hash;
        Integer fn_index = convertToInteger(env.getTxt2imgFnIndex());
        Boolean loraPluginInstalled = convertToBoolean(env.getLoraPluginInstalled());
        String keyUuid = UUID.randomUUID().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("fn_index", fn_index);
        List<Object> data = new ArrayList<>();
        data.add("task("+sessionHash+")");
        data.add(this.prompt);
        data.add(this.negative_prompt);
        data.add(new ArrayList<>());
        data.add(this.steps); // steps
        data.add(this.sampler_index); // sampler
        data.add(false);
        data.add(false);
        data.add(1); // 批次固定为1，防止bug，需要多批次通过多次api调用来实现
        data.add(this.batch_size); // 每批数量
        data.add(this.cfg_scale); // cfg scale
        data.add(this.seed);
        data.add(-1);
        data.add(0);
        data.add(0);
        data.add(0);
        data.add(false);
        data.add(this.height);
        data.add(this.width);
        data.add(this.enable_hr);
        data.add(this.denoising_strength);
        data.add(this.hr_scale);
        data.add(this.hr_upscaler);
        data.add(0);
        data.add(0);
        data.add(0);
        data.add(new ArrayList<>());
        data.add("None");
        if (loraPluginInstalled) {
            data.add(false);
            data.add(false);
            data.add("LoRA");
            data.add("None");
            data.add(1);
            data.add(1);
            data.add("LoRA");
            data.add("None");
            data.add(1);
            data.add(1);
            data.add("LoRA");
            data.add("None");
            data.add(1);
            data.add(1);
            data.add("LoRA");
            data.add("None");
            data.add(1);
            data.add(1);
            data.add("LoRA");
            data.add("None");
            data.add(1);
            data.add(1);
            data.add(null);
            data.add("Refresh models");
        }
        data.add(null);
        data.add(false);
        data.add(false);
        data.add("positive");
        data.add("comma");
        data.add(0);
        data.add(false);
        data.add(false);
        data.add("");
        data.add("Seed");
        data.add("");
        data.add("Nothing");
        data.add("");
        data.add("Nothing");
        data.add("");
        data.add(true);
        data.add(false);
        data.add(false);
        data.add(false);
        data.add(0);
        data.add(null);
        data.add(false);
        data.add(50);
        List<Object> imageList = new ArrayList<>();
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("name", "D:\\workspace\\novelai-webui\\novelai-webui-aki-v3\\outputs\\txt2img-images\\"+keyUuid+".png");
        imageMap.put("data", "file=D:\\workspace\\novelai-webui\\novelai-webui-aki-v3\\outputs\\txt2img-images\\"+keyUuid+".png");
        imageMap.put("is_file", true);
        imageList.add(imageMap);
        data.add(new Gson().toJson(imageList));
        Map<String, Object> extraGenerationParams = new HashMap<>();
        extraGenerationParams.put("prompt", "");
        extraGenerationParams.put("all_prompts", new ArrayList<>());
        extraGenerationParams.put("negative_prompt", "");
        extraGenerationParams.put("all_negative_prompts", new ArrayList<>());
        extraGenerationParams.put("seed", 4275422043L);
        extraGenerationParams.put("all_seeds", new ArrayList<>(Collections.singletonList(4275422043L)));
        extraGenerationParams.put("subseed", 4014965911L);
        extraGenerationParams.put("all_subseeds", new ArrayList<>(Collections.singletonList(4014965911L)));
        extraGenerationParams.put("subseed_strength", 0);
        extraGenerationParams.put("width", this.width);
        extraGenerationParams.put("height", this.height);
        extraGenerationParams.put("sampler_name", "Euler a");
        extraGenerationParams.put("cfg_scale", 7);
        extraGenerationParams.put("steps", this.steps);
        extraGenerationParams.put("batch_size", 1);
        extraGenerationParams.put("restore_faces", false);
        extraGenerationParams.put("face_restoration_model", null);
        extraGenerationParams.put("sd_model_hash", "a074b8864e");
        extraGenerationParams.put("seed_resize_from_w", 0);
        extraGenerationParams.put("seed_resize_from_h", 0);
        extraGenerationParams.put("denoising_strength", null);
        extraGenerationParams.put("extra_generation_params", new HashMap<>());
        extraGenerationParams.put("index_of_first_image", 0);
        extraGenerationParams.put("infotexts", new ArrayList<>(Collections.singletonList("Steps: 20, Sampler: Euler a, CFG scale: 7, Seed: 4275422043, Size: 512x512, Model hash: a074b8864e, Model: CounterfeitV25_25, Clip skip: 2, ENSD: 31337")));
        extraGenerationParams.put("styles", new ArrayList<>());
        extraGenerationParams.put("job_timestamp", "20230321210335");
        extraGenerationParams.put("clip_skip", 2);
        extraGenerationParams.put("is_using_inpainting_conditioning", false);
        data.add(new Gson().toJson(extraGenerationParams));
        data.add("<p>Steps: 20, Sampler: Euler a, CFG scale: 7, Seed: 4275422043, Size: 512x512, Model hash: a074b8864e, Model: CounterfeitV25_25, Clip skip: 2, ENSD: 31337</p>");
        data.add("<p></p><div class='performance'><p class='time'>Time taken: <wbr>3.23s</p><p class='vram'>Torch active/reserved: 4579/4990 MiB, <wbr>Sys VRAM: 7625/8192 MiB (93.08%)</p></div>");
        params.put("data", data);
        params.put("session_hash", sessionHash);
        return new Gson().toJson(params);
    }

    public String toPreDictForImg2img(String initImage, StableDiffusionEnv env) {
        String sessionHash = this.session_hash;
        Integer fn_index = convertToInteger(env.getImg2imgFnIndex());
        Boolean loraPluginInstalled = convertToBoolean(env.getLoraPluginInstalled());
        Boolean isUltimateUpscalePluginInstalled = convertToBoolean(env.getUltimateUpscalePluginInstalled());
        String keyUuid = UUID.randomUUID().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("fn_index", fn_index);
        List<Object> list = new ArrayList<>();
        list.add("task("+keyUuid+")");
        list.add(0);
        list.add(this.prompt);
        list.add(this.negative_prompt);
        list.add(new ArrayList<>());
        list.add("data:image/png;base64,"+initImage);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(this.steps);
        list.add(this.sampler_index);
        list.add(4);
        list.add(0);
        list.add("original");
        list.add(false);
        list.add(false);
        list.add(1); // 批次固定为1，防止bug，需要多批次通过多次api调用来实现
        list.add(this.batch_size); // 每批数量
        list.add(this.cfg_scale);
        list.add(1.5);
        list.add(this.denoising_strength);
        list.add(this.seed);
        list.add(-1);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(false);
        list.add(this.height);
        list.add(this.width);
        list.add("Just resize");
        list.add("Whole picture");
        list.add(32);
        list.add("Inpaint masked");
        list.add("");
        list.add("");
        list.add("");
        list.add(new ArrayList<>());
        list.add("None");
        if (loraPluginInstalled) {
            list.add(false);
            list.add(false);
            list.add("LoRA");
            list.add("None");
            list.add(1);
            list.add(1);
            list.add("LoRA");
            list.add("None");
            list.add(1);
            list.add(1);
            list.add("LoRA");
            list.add("None");
            list.add(1);
            list.add(1);
            list.add("LoRA");
            list.add("None");
            list.add(1);
            list.add(1);
            list.add("LoRA");
            list.add("None");
            list.add(1);
            list.add(1);
            list.add(null);
            list.add("Refresh models");
        }
        list.add(null);
        list.add("");
        list.add(true);
        list.add(true);
        list.add("");
        list.add("");
        list.add(true);
        list.add(50);
        list.add(true);
        list.add(1);
        list.add(0);
        list.add(false);
        list.add(4);
        list.add(1);
        list.add("None");
        list.add("");
        list.add(128);
        list.add(8);
        list.add(generateList("left","right","up","down"));
        list.add(1);
        list.add(0.05);
        list.add(128);
        list.add(4);
        list.add("fill");
        list.add(generateList("left","right","up","down"));
        list.add(false);
        list.add(false);
        list.add("positive");
        list.add("comma");
        list.add(0);
        list.add(false);
        list.add(false);
        list.add("");
        list.add("");
        list.add(64);
        list.add("None");
        list.add(2);
        list.add("Seed");
        list.add("");
        list.add("Nothing");
        list.add("");
        list.add("Nothing");
        list.add("");
        list.add(true);
        list.add(false);
        list.add(false);
        list.add(false);
        list.add(0);
        list.add(null);
        list.add(false);
        list.add(50);
        if (isUltimateUpscalePluginInstalled) {
            list.add("");
            list.add(512);
            list.add(0);
            list.add(8);
            list.add(32);
            list.add(64);
            list.add(0.35);
            list.add(32);
            list.add("None");
            list.add(true);
            list.add("Linear");
            list.add(false);
            list.add(8);
            list.add("None");
            list.add("From img2img2 settings");
            list.add(2048);
            list.add(2048);
            list.add(2);
        }
        list.add(new ArrayList<>());
        list.add("");
        list.add("");
        list.add("");
        params.put("data", list);
        params.put("session_hash", sessionHash);
        return new GsonBuilder().disableHtmlEscaping().create().toJson(params);
    }

    private List<String> generateList(String ...args) {
        List<String> result = new ArrayList<>();
        for (String arg : args) {
            result.add(arg);
        }
        return result;
    }

    public String toPreDictForControlNet(StableDiffusionEnv env, ControlNetRequestBody controlNetRequestBody) {
        return toPreDictForControlNet(env, false, controlNetRequestBody);
    }
    public String toPreDictForControlNet(StableDiffusionEnv env, boolean isImg2img, ControlNetRequestBody requestBody) {
        String sessionHash = this.session_hash;
        Integer fn_index = convertToInteger(env.getTxt2imgControlNetFnIndex());
        if (isImg2img) {
            fn_index = convertToInteger(env.getImg2imgControlNetFnIndex());
            return "{\n" +
                    "  \"fn_index\": "+fn_index+",\n" +
                    "  \"data\": [\n" +
                    "    true,\n" +
                    "        \"" + controlnet_module + "\",\n" +
                    "        \"" + controlnet_model + "\",\n" +
                    "    1,\n" +
                    "        {\n" +
                    "           \"image\": \"data:image/png;base64," + controlnet_input_image + "\"," +
                    "            \"mask\": \"data:image/png;base64," + maskImg +"\"" +
                    "        },\n" +
                    "    "+requestBody.isInvertImage()+",\n" +
                    "    \"Scale to Fit (Inner Fit)\",\n" +
                    "    false,\n" +
                    "    false,\n" +
                    "    64,\n" +
                    "    64,\n" +
                    "    64,\n" +
                    "    0,\n" +
                    "    1,\n" +
                    "    false\n" +
                    "  ],\n" +
                    "  \"session_hash\": \""+sessionHash+"\"\n" +
                    "}\n";
        }
        return "{\n" +
                "    \"fn_index\": " + fn_index + ",\n" +
                "    \"data\": [\n" +
                "        true,\n" +
                "        \"" + controlnet_module + "\",\n" +
                "        \"" + controlnet_model + "\",\n" +
                "        1,\n" +
                "        {\n" +
                "           \"image\": \"data:image/png;base64," + controlnet_input_image + "\"," +
                "            \"mask\": \"data:image/png;base64," + maskImg +"\"" +
                "        },\n" +
                "        false,\n" +
                "        \"Scale to Fit (Inner Fit)\",\n" +
                "        false,\n" +
                "        false,\n" +
                "        64,\n" +
                "        64,\n" +
                "        64,\n" +
                "        0,\n" +
                "        1,\n" +
                "        false\n" +
                "    ],\n" +
                "    \"session_hash\": \""+sessionHash+"\"\n" +
                "}";
    }

    public String toPreDictSwitchModel(String sessionHash, StableDiffusionEnv env){
        if (modelVersionMap != null) {
            String modelVersionMapValue = modelVersionMap.get(env.getEnvId().toString());
            return "{\"fn_index\":"+env.getSwitchModelFnIndex()+",\"data\":[\""+modelVersionMapValue+"\"],\"session_hash\":\""+sessionHash+"\"}";
        }
        return null;
    }

    /**
     * @param sdEnv
     * @param content
     * @return
     */
    public String toPreDictUpdateString(StableDiffusionEnv sdEnv, String content) {
        Map<String, Object> replaceMap = getReplaceMap();
        Set<Map.Entry<String, Object>> entries = replaceMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();
            content = content.replaceAll(key, value.toString());
        }
        return content;
    }

    private Map<String, Object> getReplaceMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("`session_hash`", session_hash);
        result.put("`prompt`", prompt);
        result.put("`negative_prompt`", negative_prompt);
        result.put("\"`steps`\"", steps);
        result.put("`sampler_index`", sampler_index);
        result.put("\"`cfg_scale`\"", cfg_scale);
        result.put("\"`width`\"", width);
        result.put("\"`height`\"", height);
        return result;
    }
}
