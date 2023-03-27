package cc.alpgo.sdtool.util;

import cc.alpgo.sdtool.util.request.ControlNetUnit;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Txt2img {
    private String default_sampler = "";
    private int default_steps = 30;

    public Map<String, Object> txt2img(boolean enable_hr,
                            double denoising_strength,
                            int firstphase_width,
                            int firstphase_height,
                            int hr_scale,
                            HiResUpscaler hr_upscaler,
                            int hr_second_pass_steps,
                            int hr_resize_x,
                            int hr_resize_y,
                            String prompt,
                            List<String> styles,
                            int seed,
                            int subseed,
                            double subseed_strength,
                            int seed_resize_from_h,
                            int seed_resize_from_w,
                            String sampler_name,
                            int batch_size,
                            int n_iter,
                            Integer steps,
                            double cfg_scale,
                            int width,
                            int height,
                            boolean restore_faces,
                            boolean tiling,
                            boolean do_not_save_samples,
                            boolean do_not_save_grid,
                            String negative_prompt,
                            double eta,
                            int s_churn,
                            int s_tmax,
                            int s_tmin,
                            int s_noise,
                            Map<String, Object> override_settings,
                            boolean override_settings_restore_afterwards,
                            List<ControlNetUnit> controlnet_units) {
        if (sampler_name == null) {
            sampler_name = this.default_sampler;
        }
        if (steps == null) {
            steps = this.default_steps;
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("enable_hr", enable_hr);
        payload.put("hr_scale", hr_scale);
        payload.put("hr_upscaler", hr_upscaler);
        payload.put("hr_second_pass_steps", hr_second_pass_steps);
        payload.put("hr_resize_x", hr_resize_x);
        payload.put("hr_resize_y", hr_resize_y);
        payload.put("denoising_strength", denoising_strength);
        payload.put("firstphase_width", firstphase_width);
        payload.put("firstphase_height", firstphase_height);
        payload.put("prompt", prompt);
        payload.put("styles", styles);
        payload.put("seed", seed);
        payload.put("subseed", subseed);
        payload.put("subseed_strength", subseed_strength);
        payload.put("seed_resize_from_h", seed_resize_from_h);
        payload.put("seed_resize_from_w", seed_resize_from_w);
        payload.put("batch_size", batch_size);
        payload.put("n_iter", n_iter);
        payload.put("steps", steps);
        payload.put("cfg_scale", cfg_scale);
        payload.put("width", width);
        payload.put("height", height);
        payload.put("restore_faces", restore_faces);
        payload.put("tiling", tiling);
        payload.put("do_not_save_samples", do_not_save_samples);
        payload.put("do_not_save_grid", do_not_save_grid);
        payload.put("negative_prompt", negative_prompt);
        payload.put("eta", eta);
        payload.put("s_churn", s_churn);
        payload.put("s_tmax", s_tmax);
        payload.put("s_tmin", s_tmin);
        payload.put("s_noise", s_noise);
        payload.put("override_settings", override_settings);
        payload.put("override_settings_restore_afterwards", override_settings_restore_afterwards);
        payload.put("sampler_name", sampler_name);
        payload.put("sampler_index", null);
        payload.put("script_name", null);
        payload.put("script_args", null);
        payload.put("send_images", true);
        payload.put("save_images", false);
        payload.put("alwayson_scripts", new HashMap<>());

//        if (use_deprecated_controlnet && controlnet_units != null && controlnet_units.size() > 0) {
//            List<Map<String,
//                    Object>> controlnet_units_dict = new ArrayList<>();
//            for (ControlNetUnit unit : controlnet_units) {
//                controlnet_units_dict.add(unit.toDict());
//            }
//            payload.put("controlnet_units", controlnet_units_dict);
//            return payload;
//        }

        if (controlnet_units != null && controlnet_units.size() > 0) {
            Map<String, Object> controlnet_payload = new HashMap<>();
            controlnet_payload.put("args", controlnet_units);
            payload.put("alwayson_scripts", controlnet_payload);
        }
        return payload;
    }
}
