package cc.alpgo.sdtool.util;

import java.util.List;
import java.util.Map;

public class StableDiffusionApiResponse {
    private List<String> images;
    private Map<String, Object> parameters;
    private String info;
    private List<String> patternImages;

    public List<String> getPatternImages() {
        return patternImages;
    }

    public void setPatternImages(List<String> patternImages) {
        this.patternImages = patternImages;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
