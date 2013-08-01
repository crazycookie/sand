delimiter $$

# CREATE DATABASE `tbcoredb` /*!40100 DEFAULT CHARACTER SET utf8 */$$

delimiter $$

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster AUTO_INCREMENT=3 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_id` int(11) NOT NULL,
  `key` varchar(45) NOT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster AUTO_INCREMENT=7 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `userRoleRef` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster AUTO_INCREMENT=4 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tbUserNick` varchar(40) NOT NULL,
  `pwd` varchar(24) NOT NULL,
  `tbUserId` varchar(45) DEFAULT NULL,
  `accessToken` varchar(80) DEFAULT NULL,
  `refreshToken` varchar(80) DEFAULT NULL,
  `entryUpdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster AUTO_INCREMENT=3 DEFAULT CHARSET=utf8$$


