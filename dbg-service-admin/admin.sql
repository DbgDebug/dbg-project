DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` char(16) NOT NULL COMMENT '用户名',
  `password` char(60) NOT NULL COMMENT '密码',
  `real_name` char(20) NOT NULL,
  `register_time` bigint(20) NOT NULL COMMENT '注册时间',
  `last_time` bigint(20) NOT NULL COMMENT '最后登录时间',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  `sex` int(11) NOT NULL COMMENT '性别',
  `email` varchar(128) NOT NULL COMMENT '邮箱',
  `last_ip` varchar(128) NOT NULL COMMENT '最后登录IP',
  `avatar` varchar(120) NOT NULL DEFAULT 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
  `status` int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


DROP TABLE IF EXISTS `tb_account_role`;
CREATE TABLE `tb_account_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_bilibili_danmu`;
CREATE TABLE `tb_bilibili_danmu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roomid` bigint(20) NOT NULL,
  `uid` bigint(20) NOT NULL,
  `nickname` varchar(30) NOT NULL,
  `danmu` varchar(30) NOT NULL,
  `send_time` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `PK` (`id`) USING BTREE,
  KEY `SEND_TIME` (`send_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_gift`;
CREATE TABLE `tb_gift` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `gift_num` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `gift_id` int(11) NOT NULL,
  `gift_name` varchar(10) NOT NULL,
  `username` varchar(20) NOT NULL,
  `send_time` int(11) NOT NULL,
  `paid_gift` tinyint(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_gift_statistic`;
CREATE TABLE `tb_gift_statistic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `gift` text NOT NULL,
  `start_time` int(11) NOT NULL,
  `end_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_gift_statistics`;
CREATE TABLE `tb_gift_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `gift_name` char(10) NOT NULL,
  `num` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `date` int(11) NOT NULL,
  `paid_gift` tinyint(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_guard`;
CREATE TABLE `tb_guard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `username` char(20) NOT NULL,
  `guard_level` int(11) NOT NULL,
  `gift_name` char(10) NOT NULL,
  `num` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `send_time` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_live_room`;
CREATE TABLE `tb_live_room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roomid` int(11) NOT NULL,
  `up` varchar(16) NOT NULL,
  `create_time` int(11) NOT NULL,
  `update_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL COMMENT '所属服务id',
  `permission_id` char(32) NOT NULL,
  `permission_name` char(30) NOT NULL,
  `path` char(100) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `method` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_permission_tmp`;
CREATE TABLE `tb_permission_tmp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL COMMENT '所属服务id',
  `permission_id` char(32) NOT NULL,
  `permission_name` char(30) NOT NULL,
  `path` char(80) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `method` char(120) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creator_id` int(11) NOT NULL COMMENT '上级角色（创建角色的角色）',
  `role_level` int(11) NOT NULL,
  `role_name` varchar(30) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_service`;
CREATE TABLE `tb_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` char(30) NOT NULL,
  `display_name` char(30) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_service_tmp`;
CREATE TABLE `tb_service_tmp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(120) NOT NULL,
  `display_name` varchar(120) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_welcome_statistic`;
CREATE TABLE `tb_welcome_statistic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `start_time` int(11) NOT NULL,
  `end_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_program`;
CREATE TABLE `tb_program` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` INT NOT NULL COMMENT '0 - 未编译，不可执行\n1 - 已编译，可正常执行',
  `create_time` INT NOT NULL,
  `update_time` INT NOT NULL,
  `program_language` VARCHAR(30) NOT NULL,
  `described` VARCHAR(120) NOT NULL,
  `source_code` TEXT NOT NULL,
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_weather`;
CREATE TABLE `dbg`.`tb_weather` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `device_id` INT NOT NULL,
  `creation_time` INT NOT NULL,
  `temperature` FLOAT NOT NULL COMMENT '摄氏度,xx.xx',
  `humidity` FLOAT NOT NULL COMMENT '湿度,xx.xx',
  `atmospheric_pressure` FLOAT NULL DEFAULT -1 COMMENT 'hPa',
  PRIMARY KEY (`id`));

CREATE TABLE `dbg`.`stable_diffusion_task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` CHAR(36) NOT NULL,
  `creation_time` INT NOT NULL,
  `completion_time` INT NULL DEFAULT NULL,
  `width` INT NOT NULL,
  `height` INT NOT NULL,
  `sampling_steps` INT NOT NULL,
  `cfg_scale` FLOAT NOT NULL,
  `seed` BIGINT NOT NULL,
  `sampling_method` VARCHAR(16) NOT NULL,
  `params` VARCHAR(2048) NOT NULL,
  `image_path` VARCHAR(128) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


INSERT INTO `tb_account` VALUES (1,'admin','$2a$10$VUgTuDlQtoBc.KCrYu3JuOQqNLLnGWMBnmvRRDEkSxAqQBoM1fM8u','admin',unix_timestamp(now()),unix_timestamp(now()),unix_timestamp(now()),unix_timestamp(now()),1,'admin@qq.com','127.0.0.1','https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',1);

INSERT INTO `tb_account_role` VALUES (1,1,2);

INSERT INTO `tb_role` VALUES (1,0,0,'system',unix_timestamp(now()),unix_timestamp(now()),1),(2,0,1,'admin',unix_timestamp(now()),unix_timestamp(now()),1);

INSERT INTO `tb_service` VALUES (1,'dbg-service-admin','后台管理模块',unix_timestamp(now()),unix_timestamp(now()),1),(2,'dbg-service-blog','博客服务',unix_timestamp(now()),unix_timestamp(now()),1);