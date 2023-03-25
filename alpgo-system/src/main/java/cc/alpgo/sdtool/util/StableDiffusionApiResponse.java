package cc.alpgo.sdtool.util;

import java.util.List;
import java.util.Map;

public class StableDiffusionApiResponse {
    private List<String> images;
    private Map<String, Object> parameters;
    private String info;

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
