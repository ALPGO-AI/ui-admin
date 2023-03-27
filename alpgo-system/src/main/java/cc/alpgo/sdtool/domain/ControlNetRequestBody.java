package cc.alpgo.sdtool.domain;

import com.google.gson.internal.LinkedTreeMap;

public class ControlNetRequestBody {
    private Boolean enable;
    private String inputImage;
    private String model;
    private String module;

    public ControlNetRequestBody() {
    }

    public ControlNetRequestBody(LinkedTreeMap controlnet) {
        this.enable = (Boolean) controlnet.get("enable");
        this.inputImage = (String) controlnet.get("inputImage");
        this.model = (String) controlnet.get("model");
        this.module = (String) controlnet.get("module");
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getInputImage() {
        return inputImage;
    }

    public void setInputImage(String inputImage) {
        this.inputImage = inputImage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
