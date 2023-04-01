CREATE TABLE if not exists image (
  image_id bigint(20) NOT NULL auto_increment comment 'image id',
  uri VARCHAR(255) NOT NULL comment 'image uri',
  create_by varchar(64) default '' comment '创建者',
  create_time datetime comment '创建时间',
  update_by varchar(64) default '' comment '更新者',
  update_time datetime comment '更新时间',
  PRIMARY KEY (image_id)
) ENGINE=InnoDB COMMENT='image表';

CREATE TABLE if not exists image_provider (
  provider_id bigint(20) NOT NULL auto_increment comment 'provider id',
  env_id bigint(20) NOT NULL comment '环境id',
  image_id bigint(20) NOT NULL comment 'image id',
  url TEXT comment '完整地址',
  create_by varchar(64) default '' comment '创建者',
  create_time datetime comment '创建时间',
  update_by varchar(64) default '' comment '更新者',
  update_time datetime comment '更新时间',
  PRIMARY KEY (provider_id)
) ENGINE=InnoDB COMMENT='image_provider表';