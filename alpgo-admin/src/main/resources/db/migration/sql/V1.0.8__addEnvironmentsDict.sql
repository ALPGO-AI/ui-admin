INSERT INTO sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('环境类型', 'environment_type', '0', 'admin', '2022-11-22 14:51:31', '', NULL, '环境类型');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'stable_diffusion_webui', 'Stable Diffusion WebUI', 'environment_type','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'Stable Diffusion WebUI');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'tencent_cos', '腾讯云COS对象存储', 'environment_type','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, '腾讯云COS对象存储');
INSERT INTO sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('环境参数类型', 'environment_param_type', '0', 'admin', '2022-11-22 14:51:31', '', NULL, '环境参数类型');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'boolean', '布尔类型', 'environment_param_type','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'boolean');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'int', '整数类型', 'environment_param_type','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'int');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'double', '小数类型', 'environment_param_type','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'double');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'string', '字符串类型', 'environment_param_type','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'string');
