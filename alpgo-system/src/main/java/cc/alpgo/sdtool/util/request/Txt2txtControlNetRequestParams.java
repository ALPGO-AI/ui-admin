package cc.alpgo.sdtool.util.request;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.alpgo.sdtool.constant.ApiContants.maskImg;
import static cc.alpgo.sdtool.constant.ApiContants.whiteBgImg;

public class Txt2txtControlNetRequestParams extends Txt2txtRequestParams {
    private List<ControlNetUnit> controlnet_units;

    private Map<String, Object> alwayson_scripts = new HashMap<>();

    public Map<String, Object> getAlwayson_scripts() {
        return alwayson_scripts;
    }

    public void setAlwayson_scripts(Map<String, Object> alwayson_scripts) {
        this.alwayson_scripts = alwayson_scripts;
    }
    /**
     * Constructor for StableDiffusionApiParamsBuilder.
     *
     * @param positiveprompt
     * @param negativeprompt
     * @param parametersJson
     * @param seed
     */
    public Txt2txtControlNetRequestParams(String positiveprompt, String negativeprompt, String parametersJson, String seed, String image) {
        super(positiveprompt, negativeprompt, parametersJson, seed);
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map = gson.fromJson(parametersJson, map.getClass());
        Map<String, Object> mapChild = new HashMap<>();
        String jsonStr = "{\n" +
                "            \"enabled\": true,\n" +
                "            \"module\": null,\n" +
                "            \"model\": \"control_sd15_canny [fef5e48e]\",\n" +
                "            \"weight\": 1.0,\n" +
                "            \"image\": \"" + image + "\",\n" +
                "            \"mask\": \"" + maskImg + "\",\n" +
                "            \"invert_image\": false,\n" +
                "            \"resize_mode\": \"Scale to Fit (Inner Fit)\",\n" +
                "            \"rgbbgr_mode\": false,\n" +
                "            \"lowvram\": false,\n" +
                "            \"processor_res\": 64,\n" +
                "            \"threshold_a\": 64,\n" +
                "            \"threshold_b\": 64,\n" +
                "            \"guidance\": 1,\n" +
                "            \"guidance_start\": 0.0,\n" +
                "            \"guidance_end\": 1.0,\n" +
                "            \"guessmode\": false\n" +
                "        }";
        mapChild = gson.fromJson(jsonStr, map.getClass());
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(mapChild);
        map.put("controlnet_units", list);
        List<Map<String, Object>> controlnet_units = (List<Map<String, Object>>) map.get("controlnet_units");
        List<ControlNetUnit> controlNetUnits = new ArrayList<>();
        for (Map<String, Object> unit : controlnet_units) {
            ControlNetUnit controlNetUnit = new ControlNetUnit();
            controlNetUnit.setInput_image((String) unit.get("input_image"));
            controlNetUnit.setMask((String) unit.get("mask"));
            controlNetUnit.setModule((String) unit.get("module"));
            controlNetUnit.setModel((String) unit.get("model"));
            controlNetUnit.setWeight(((Double) unit.get("weight")).intValue());
            controlNetUnit.setResize_mode((String) unit.get("resize_mode"));
            controlNetUnit.setLowvram((Boolean) unit.get("lowvram"));
            controlNetUnit.setProcessor_res(((Double) unit.get("processor_res")).intValue());
            controlNetUnit.setThreshold_a(((Double) unit.get("threshold_a")).intValue());
            controlNetUnit.setThreshold_b(((Double) unit.get("threshold_b")).intValue());
            controlNetUnit.setGuidance(((Double) unit.get("guidance")).intValue());
            controlNetUnit.setGuidance_start(((Double) unit.get("guidance_start")).intValue());
            controlNetUnit.setGuidance_end(((Double) unit.get("guidance_end")).intValue());
            controlNetUnit.setGuessmode((Boolean) unit.get("guessmode"));
            controlNetUnits.add(controlNetUnit);
        }
        this.controlnet_units = controlNetUnits;
    }

    // getters and setters

    public List<ControlNetUnit> getControlnet_units() {
        return controlnet_units;
    }

    public void setControlnet_units(List<ControlNetUnit> controlnet_units) {
        this.controlnet_units = controlnet_units;
    }

    @Override
    public String toRequestBody() {
        return new Gson().toJson(this);
    }
}
