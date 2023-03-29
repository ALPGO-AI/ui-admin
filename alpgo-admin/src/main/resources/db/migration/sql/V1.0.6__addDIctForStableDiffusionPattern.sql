INSERT INTO sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('预设模板', 'stable_diffusion_preset_template', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'StableDiffusion Sampler');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'txt2img', '文生图', 'stable_diffusion_preset_template','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, '文生图');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'img2img', '图生图', 'stable_diffusion_preset_template','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, '图生图');
