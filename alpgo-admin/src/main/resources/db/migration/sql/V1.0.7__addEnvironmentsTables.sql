CREATE TABLE environment (
  environment_id bigint(20) not null auto_increment PRIMARY KEY,
  name VARCHAR(255),
  type VARCHAR(255),
  description TEXT,
  access_level VARCHAR(255),
  create_time DATETIME,
  create_by VARCHAR(255),
  update_time DATETIME,
  update_by VARCHAR(255)
)ENGINE=InnoDB COMMENT='environments';

CREATE TABLE environment_parameters (
  parameter_id bigint(20) not null auto_increment PRIMARY KEY,
  environment_id bigint(20) not null,
  param_name VARCHAR(255),
  param_value TEXT,
  param_type VARCHAR(255),
  description TEXT,
  access_level VARCHAR(255),
  create_time DATETIME,
  create_by VARCHAR(255),
  update_time DATETIME,
  update_by VARCHAR(255)
)ENGINE=InnoDB COMMENT='environment_parameters';
