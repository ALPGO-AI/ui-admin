package cc.alpgo.sdtool.util.request;

import java.util.HashMap;
import java.util.Map;

public class ControlNetUnit {

    private String input_image;
    private String mask;
    private String module;
    private String model;
    private int weight;
    private String resize_mode;
    private boolean lowvram;
    private int processor_res;
    private int threshold_a;
    private int threshold_b;
    private int guidance;
    private int guidance_start;
    private int guidance_end;
    private boolean guessmode;

    public String getResize_mode() {
        return resize_mode;
    }

    public void setResize_mode(String resize_mode) {
        this.resize_mode = resize_mode;
    }

    public String getInput_image() {
        return input_image;
    }

    public void setInput_image(String input_image) {
        this.input_image = input_image;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isLowvram() {
        return lowvram;
    }

    public void setLowvram(boolean lowvram) {
        this.lowvram = lowvram;
    }

    public int getProcessor_res() {
        return processor_res;
    }

    public void setProcessor_res(int processor_res) {
        this.processor_res = processor_res;
    }

    public int getThreshold_a() {
        return threshold_a;
    }

    public void setThreshold_a(int threshold_a) {
        this.threshold_a = threshold_a;
    }

    public int getThreshold_b() {
        return threshold_b;
    }

    public void setThreshold_b(int threshold_b) {
        this.threshold_b = threshold_b;
    }

    public int getGuidance() {
        return guidance;
    }

    public void setGuidance(int guidance) {
        this.guidance = guidance;
    }

    public int getGuidance_start() {
        return guidance_start;
    }

    public void setGuidance_start(int guidance_start) {
        this.guidance_start = guidance_start;
    }

    public int getGuidance_end() {
        return guidance_end;
    }

    public void setGuidance_end(int guidance_end) {
        this.guidance_end = guidance_end;
    }

    public boolean isGuessmode() {
        return guessmode;
    }

    public void setGuessmode(boolean guessmode) {
        this.guessmode = guessmode;
    }
}
