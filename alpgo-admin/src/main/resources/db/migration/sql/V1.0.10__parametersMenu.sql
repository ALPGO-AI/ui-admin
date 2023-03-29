-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environment_parameters', '3', '1', 'parameters', 'system/parameters/index', 1, 0, 'C', '0', '0', 'system:parameters:list', '#', 'admin', sysdate(), '', null, 'environment_parameters菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environment_parameters查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:parameters:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environment_parameters新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:parameters:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environment_parameters修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:parameters:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environment_parameters删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:parameters:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environment_parameters导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:parameters:export',       '#', 'admin', sysdate(), '', null, '');