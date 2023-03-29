-- 菜单 SQL
update sys_menu set parent_id = '4' where menu_name = 'environments';
update sys_menu set parent_id = '4', visible = '1' where menu_name = 'environment_parameters';
