CREATE TABLE stable_diffusion_pattern (
  pattern_id bigint(20) NOT NULL auto_increment comment 'pattern设置id',
  model longtext comment '模型',
  positive_prompt longtext comment '正向提示',
  negative_prompt longtext comment '负向提示',
  sample_image longtext comment '样例图片',
  preset_template longtext comment '预设模板',
  pattern_style longtext comment 'pattern风格',
  parameters_json json comment '参数',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  PRIMARY KEY (pattern_id)
) ENGINE=InnoDB COMMENT='stable_diffusion_pattern表';
INSERT INTO sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('StableDiffusion模型', 'stable_diffusion_model', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'StableDiffusion模型');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'CounterfeitV25', 'CounterfeitV25', 'stable_diffusion_model','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'CounterfeitV25');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'animefull', 'animefull', 'stable_diffusion_model','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'animefull');
INSERT INTO sys_dict_data
(dict_sort, dict_value, dict_label, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, 'chilloutmix', 'chilloutmix', 'stable_diffusion_model','', 'info', 'N', '0', 'admin', '2022-11-22 14:51:31', '', NULL, 'chilloutmix');
DROP TABLE IF EXISTS `stable_diffusion_output`;
CREATE TABLE `stable_diffusion_output` (
  `output_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'output id',
  `output_image_url` longtext COMMENT '输出图片地址',
  `reference_image_url` longtext COMMENT '参考原图地址',
  `seed` longtext COMMENT '使用的种子',
  `type` longtext COMMENT '类型（绘图/线稿/三视图）',
  `straight_parameter` longtext COMMENT '直出参数',
  `reference_ouput_id` bigint(20) NOT NULL COMMENT '参考输出图片id',
  `pattern_id` bigint(20) NOT NULL COMMENT 'patternId',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`output_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='stable_diffusion_output表';
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Stable Diffusion 输出图片', '4', '1', 'output', 'sdtool/output/index', 1, 0, 'C', '0', '0', 'sdtool:output:list', '#', 'admin', sysdate(), '', null, 'stable_diffusion_output菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Stable Diffusion 输出图片查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sdtool:output:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Stable Diffusion 输出图片新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sdtool:output:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Stable Diffusion 输出图片修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sdtool:output:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Stable Diffusion 输出图片删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sdtool:output:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Stable Diffusion 输出图片导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sdtool:output:export',       '#', 'admin', sysdate(), '', null, '');