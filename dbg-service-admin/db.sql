DROP TABLE IF EXISTS `tb_gift`;
CREATE TABLE `tb_gift` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `gift_num` int(11) NOT NULL,
  `gift_id` int(11) NOT NULL,
  `price` INT(11) NOT NULL,
  `gift_name` varchar(10) NOT NULL,
  `username` varchar(20) NOT NULL,
  `send_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_gift_statistic`;
CREATE TABLE `tb_gift_statistic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `gift` text NOT NULL,
  `start_time` int(11) NOT NULL,
  `end_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `dbg`.`tb_welcome_statistic` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `room_id` INT NOT NULL,
  `num` INT NOT NULL,
  `start_time` INT NOT NULL,
  `end_time` INT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `dbg`.`tb_guard` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uid` INT NOT NULL,
  `username` CHAR(20) NOT NULL,
  `guard_level` INT NOT NULL,
  `gift_name` CHAR(10) NOT NULL,
  `num` INT NOT NULL,
  `price` INT NOT NULL,
  `send_time` INT NOT NULL,
  `room_id` INT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `dbg`.`tb_gift_statistics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `room_id` INT NOT NULL,
  `gift_name` CHAR(10) NOT NULL,
  `num` INT NOT NULL,
  `price` INT NOT NULL,
  `date` INT NOT NULL,
  PRIMARY KEY (`id`));

