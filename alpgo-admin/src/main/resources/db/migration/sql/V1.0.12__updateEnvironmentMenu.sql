-- 菜单 SQL
update sys_menu set menu_name = '环境信息' where menu_name = 'environments';
update sys_menu set menu_name = '环境参数信息', visible = '1' where menu_name = 'environment_parameters';
