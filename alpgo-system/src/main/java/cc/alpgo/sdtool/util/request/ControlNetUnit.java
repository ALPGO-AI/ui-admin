package cc.alpgo.sdtool.util.request;


import java.util.HashMap;
import java.util.Map;

public class ControlNetUnit {
    private String input_image;
    private String mask;
    private String module;
    private String model;
    private float weight;
    private String resize_mode;
    private boolean lowvram;
    private int processor_res;
    private float threshold_a;
    private float threshold_b;
    private float guidance;
    private float guidance_start;
    private float guidance_end;
    private boolean guessmode;

    public ControlNetUnit(String input_image, String mask, String module, String model, float weight, String resize_mode, boolean lowvram, int processor_res, float threshold_a, float threshold_b, float guidance, float guidance_start, float guidance_end, boolean guessmode) {
        this.input_image = input_image;
        this.mask = mask;
        this.module = module;
        this.model = model;
        this.weight = weight;
        this.resize_mode = resize_mode;
        this.lowvram = lowvram;
        this.processor_res = processor_res;
        this.threshold_a = threshold_a;
        this.threshold_b = threshold_b;
        this.guidance = guidance;
        this.guidance_start = guidance_start;
        this.guidance_end = guidance_end;
        this.guessmode = guessmode;
    }
    public Map<String, Object> toDict() {
        Map<String, Object> dict = new HashMap<>();
        dict.put("enabled", true);
        dict.put("input_image", this.input_image);
        dict.put("mask", this.mask);
        dict.put("module", this.module);
        dict.put("model", this.model);
        dict.put("weight", this.weight);
        dict.put("resize_mode", this.resize_mode);
        dict.put("lowvram", this.lowvram);
        dict.put("processor_res", this.processor_res);
        dict.put("threshold_a", this.threshold_a);
        dict.put("threshold_b", this.threshold_b);
        dict.put("guidance", this.guidance);
        dict.put("guidance_start", this.guidance_start);
        dict.put("guidance_end", this.guidance_end);
        dict.put("guessmode", this.guessmode);
        return dict;
    }

}