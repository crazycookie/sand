INSERT INTO `users` (`id`,`tbUserNick`,`pwd`,`tbUserId`,`accessToken`,`refreshToken`,`entryUpdate`) VALUES (1,'admin','WmkNhCk1xR8m9HPgJcG5eg==','377254484','6201e12f5a929f3e4658ZZd8ef781ef8d51b67b22e662b2377254484','6200d121835761af1937ZZ52bbf49796b788aeae1c44830377254484','2013-05-24 23:27:46');

INSERT INTO `userRoleRef` (`id`,`userId`,`roleId`) VALUES (1,1,1);
INSERT INTO `userRoleRef` (`id`,`userId`,`roleId`) VALUES (2,1,2);

INSERT INTO `roles` (`id`,`roleName`) VALUES (1,'Admin');
INSERT INTO `roles` (`id`,`roleName`) VALUES (2,'User');

INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (1,0,'HOST','http://127.0.0.1:8080');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (2,0,'PAGESIZE','10');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (3,0,'OAUTHURL','https://oauth.taobao.com/token');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (4,0,'APPSECRET','XX');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (5,0,'APPKEY','XX');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (6,0,'APIURL','http://gw.api.taobao.com/router/rest');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (7,0,'TB_PUSH_THREAD_NUM','1');
INSERT INTO `sys_config` (`id`,`module_id`,`key`,`value`) VALUES (8,0,'TB_PUSH_SERVICE_ENABLE','false');
