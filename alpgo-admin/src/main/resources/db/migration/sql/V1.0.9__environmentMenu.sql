-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environments', '3', '1', 'environment', 'system/environment/index', 1, 0, 'C', '0', '0', 'system:environment:list', '#', 'admin', sysdate(), '', null, 'environments菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environments查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:environment:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environments新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:environment:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environments修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:environment:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environments删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:environment:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('environments导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:environment:export',       '#', 'admin', sysdate(), '', null, '');