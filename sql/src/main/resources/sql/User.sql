CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(80) NOT NULL,
  `password` varchar(80) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

CREATE TABLE `t_upload_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '''UUID''',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小 不超过10M',
  `file_path` varchar(200) COLLATE utf8mb4_bin NOT NULL,
  `file_hash` varchar(200) COLLATE utf8mb4_bin NOT NULL COMMENT 'md5',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(512) COLLATE utf8mb4_bin NOT NULL,
  `config_value` varchar(512) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
