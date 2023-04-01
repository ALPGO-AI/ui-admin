-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图片管理', '3', '1', 'image', 'system/image/index', 1, 0, 'C', '0', '0', 'system:image:list', '#', 'admin', sysdate(), '', null, 'image菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图片查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:image:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图片新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:image:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图片修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:image:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图片删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:image:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图片导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:image:export',       '#', 'admin', sysdate(), '', null, '');