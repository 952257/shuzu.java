SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `path` varchar(100) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `resource` VALUES (1, '/user/addone', '用户添加');
INSERT INTO `resource` VALUES (2, '/user/modone', '用户修改');
INSERT INTO `resource` VALUES (3, '/user/query', '用户查询');
INSERT INTO `resource` VALUES (4, '/user/delone', '用户删除');
INSERT INTO `resource` VALUES (5, '/user/export', '用户导出');

DROP TABLE IF EXISTS `sysuser`;
CREATE TABLE `sysuser` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `sysuser` VALUES (1, 'aaa', '123');
INSERT INTO `sysuser` VALUES (2, 'bbb', '123');

DROP TABLE IF EXISTS `user_res`;
CREATE TABLE `user_res` (
  `user_id` int DEFAULT NULL,
  `res_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user_res` VALUES (1, 2);
INSERT INTO `user_res` VALUES (1, 3);
INSERT INTO `user_res` VALUES (1, 5);
INSERT INTO `user_res` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
