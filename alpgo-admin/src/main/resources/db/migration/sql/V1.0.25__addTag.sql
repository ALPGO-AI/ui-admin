DROP TABLE IF EXISTS common_tag;
CREATE TABLE common_tag (
  tag_id            BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '标签id',
  parentid          BIGINT(20)      DEFAULT 0                  COMMENT '父类id',
  name              VARCHAR(50)     DEFAULT NULL               COMMENT '标签名称',
  ordernum          INT(4)          DEFAULT 0                  COMMENT '显示顺序',
  desctag           VARCHAR(20)     DEFAULT NULL               COMMENT '标签描述',
  type              CHAR(1)         DEFAULT NULL               COMMENT '标签类别（0正向 1反向）',
  status            CHAR(1)         DEFAULT '0'                COMMENT '标签状态（0正常 1停用）',
  del_flag          CHAR(1)         DEFAULT '0'                COMMENT '删除标签（0代表存在 2代表删除）',
  PRIMARY KEY (tag_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='标签表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签', '3', '1', 'tag', 'tag/tag/index', 1, 0, 'C', '0', '0', 'tag:tag:list', '#', 'admin', sysdate(), '', null, '标签菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'tag:tag:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'tag:tag:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'tag:tag:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'tag:tag:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'tag:tag:export',       '#', 'admin', sysdate(), '', null, '');