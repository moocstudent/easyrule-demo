/*
 Navicat Premium Data Transfer

 Source Server         : local mysql
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : ews-cute

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 09/07/2021 18:05:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ews_mail_config
-- ----------------------------
DROP TABLE IF EXISTS `ews_mail_config`;
CREATE TABLE `ews_mail_config`  (
                                    `mail_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `email` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `password` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `topic_id` bigint(20) NULL DEFAULT NULL,
                                    `delete_flag` tinyint(1) NULL DEFAULT 0,
                                    PRIMARY KEY (`mail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_mail_config
-- ----------------------------
INSERT INTO `ews_mail_config` VALUES (1, 'implementsteam@outlook.com', 'zhangqi1112', 1, 0);
INSERT INTO `ews_mail_config` VALUES (2, 'frankimplements@outlook.com', 'zhangqi1112', 2, 0);

-- ----------------------------
-- Table structure for ews_mail_folders
-- ----------------------------
DROP TABLE IF EXISTS `ews_mail_folders`;
CREATE TABLE `ews_mail_folders`  (
                                     `ews_folder_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `folder_code` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `folder_name` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `delete_flag` tinyint(1) NULL DEFAULT 0,
                                     PRIMARY KEY (`ews_folder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_mail_folders
-- ----------------------------
INSERT INTO `ews_mail_folders` VALUES (1, 'attach-un-download', '待下载附件邮件', 0);
INSERT INTO `ews_mail_folders` VALUES (2, 'attach-already-download', '已下载附件邮件', 0);

-- ----------------------------
-- Table structure for ews_mail_topic
-- ----------------------------
DROP TABLE IF EXISTS `ews_mail_topic`;
CREATE TABLE `ews_mail_topic`  (
                                   `topic_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `topic_name` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                   `topic_desc` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                   `topic_config` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                   `delete_flag` tinyint(1) NULL DEFAULT 0,
                                   PRIMARY KEY (`topic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_mail_topic
-- ----------------------------
INSERT INTO `ews_mail_topic` VALUES (1, '配置该主题1为收件箱增加标题判定附件判定并移动下载本地', '配置该主题1为收件箱增加标题判定附件判定并移动下载本地', '{\'D\':1}', 0);
INSERT INTO `ews_mail_topic` VALUES (2, '配置该主题2为收件箱增加标题判定附件判定并移动下载本地并拷贝一份', '配置该主题2为收件箱增加标题判定附件判定并移动下载本地并拷贝一份', '{\'DC\':2}', 0);

-- ----------------------------
-- Table structure for ews_rule
-- ----------------------------
DROP TABLE IF EXISTS `ews_rule`;
CREATE TABLE `ews_rule`  (
                             `rule_id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `display_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `rule_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `rule_level` int(2) NOT NULL,
                             `rule_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `is_enabled` tinyint(1) NULL DEFAULT NULL,
                             `is_not_supported` tinyint(1) NULL DEFAULT NULL,
                             `is_in_error` tinyint(1) NULL DEFAULT NULL,
                             `conditions` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `actions` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `item_action_type` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `item_actions` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `delete_flag` tinyint(1) NULL DEFAULT 0,
                             PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_rule
-- ----------------------------
INSERT INTO `ews_rule` VALUES (1, '规则1', '该规则1监测标题和含附件移动文件夹', 1, 'moveToFolder', 1, NULL, NULL, '{\'containsSubjectStrings\':[\'带附件\',\'htf\'],\'hasAttachments\':\'true\'}', '{\'moveToFolder\':\'attach-un-download\'}', 'D', '{\'downloadPath\':\'D:/YYYYMMDDdownload1/\'}', 0);
INSERT INTO `ews_rule` VALUES (2, '规则2', '该规则2监测标题和含附件移动文件夹', 1, 'copyToFolder', 1, NULL, NULL, '{\'containsSubjectStrings\':[\'带附件\',\'htf\',\'ll\'],\'hasAttachments\':\'true\'}', '{\'moveToFolder\':\'AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==\'}', 'DC', '{\'downloadPath\':\'D:/YYYYMMDDdownload1/\',\'copyPath\':\'D:/YYYYMMDDcopy1/\'}', 0);

-- ----------------------------
-- Table structure for ews_rule_folder_relation
-- ----------------------------
DROP TABLE IF EXISTS `ews_rule_folder_relation`;
CREATE TABLE `ews_rule_folder_relation`  (
                                             `relation_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                             `rule_id` bigint(20) NOT NULL,
                                             `ews_folder_id` bigint(20) NOT NULL,
                                             `folder_id` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                             `mail_id` bigint(20) NULL DEFAULT NULL,
                                             `delete_flag` tinyint(1) NULL DEFAULT 0,
                                             PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_rule_folder_relation
-- ----------------------------
INSERT INTO `ews_rule_folder_relation` VALUES (1, 1, 1, 'AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAATdnJoAAAA=', 1, 0);
INSERT INTO `ews_rule_folder_relation` VALUES (2, 1, 2, 'AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAATdnJsAAAA=', 1, 0);
INSERT INTO `ews_rule_folder_relation` VALUES (3, 2, 1, NULL, NULL, 0);
INSERT INTO `ews_rule_folder_relation` VALUES (4, 2, 2, NULL, NULL, 0);

-- ----------------------------
-- Table structure for ews_subscription
-- ----------------------------
DROP TABLE IF EXISTS `ews_subscription`;
CREATE TABLE `ews_subscription`  (
                                     `ews_subscription_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `subscription_id` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `subscription_key` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `subscription_minutes` int(4) NULL DEFAULT NULL,
                                     `subscription_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
                                     `delete_flag` tinyint(1) NULL DEFAULT 0,
                                     PRIMARY KEY (`ews_subscription_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_subscription
-- ----------------------------
INSERT INTO `ews_subscription` VALUES (41, 'JwB0eXpwcjA0bWI0MzA0LmFwY3ByZDA0LnByb2Qub3V0bG9vay5jb20QAAAApXu2ziJ0J0SFxDcGK8stXmwP1VK5QtkIEAAAAAFAAwAdNhplAAAAAAAAAAA=', 'one_day_subscription2021-07-09implementsteam@outlook.comzhangqi1112', 1440, '2021-07-09 17:09:55', 0);
INSERT INTO `ews_subscription` VALUES (42, 'JwB0eXpwcjA0bWI0MzA0LmFwY3ByZDA0LnByb2Qub3V0bG9vay5jb20QAAAAC0IZWsFWAkakCv7bT1beFxv/Yi26QtkIEAAAAAFAAwAdNhplAAAAAAAAAAA=', 'one_day_subscription2021-07-09implementsteam@outlook.comzhangqi1112', 1440, '2021-07-09 17:16:02', 0);
INSERT INTO `ews_subscription` VALUES (43, 'JwB0eXpwcjA0bWI0MzA0LmFwY3ByZDA0LnByb2Qub3V0bG9vay5jb20QAAAABz1vRADL0UWaQGvzjvWPS3jjypa6QtkIEAAAAAFAAwAdNhplAAAAAAAAAAA=', 'one_day_subscription2021-07-09implementsteam@outlook.comzhangqi1112', 1440, '2021-07-09 17:18:59', 0);
INSERT INTO `ews_subscription` VALUES (44, 'JwB0eXpwcjA0bWI0MzA0LmFwY3ByZDA0LnByb2Qub3V0bG9vay5jb20QAAAAEYyAn4Gt3EKNcidAn8GyZlbzVsi7QtkIEAAAAAFAAwAdNhplAAAAAAAAAAA=', 'one_day_subscription2021-07-09implementsteam@outlook.comzhangqi1112', 1440, '2021-07-09 17:27:31', 0);
INSERT INTO `ews_subscription` VALUES (45, 'JwB0eXpwcjA0bWI0MzA0LmFwY3ByZDA0LnByb2Qub3V0bG9vay5jb20QAAAAD9Po7CdDSkuMjdywj+XngFgRThS8QtkIEAAAAAFAAwAdNhplAAAAAAAAAAA=', 'one_day_subscription2021-07-09implementsteam@outlook.comzhangqi1112', 1440, '2021-07-09 17:29:39', 0);
INSERT INTO `ews_subscription` VALUES (46, 'JwB0eXpwcjA0bWI0MzA0LmFwY3ByZDA0LnByb2Qub3V0bG9vay5jb20QAAAA4wl8t0avQ0udsOAFuwmd/qXwZHa8QtkIEAAAAAFAAwAdNhplAAAAAAAAAAA=', 'one_day_subscription2021-07-09implementsteam@outlook.comzhangqi1112', 1440, '2021-07-09 17:32:23', 0);

-- ----------------------------
-- Table structure for ews_topic_rule_relation
-- ----------------------------
DROP TABLE IF EXISTS `ews_topic_rule_relation`;
CREATE TABLE `ews_topic_rule_relation`  (
                                            `relation_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                            `topic_id` bigint(20) NOT NULL,
                                            `rule_id` bigint(20) NOT NULL,
                                            `rule_level` int(2) NOT NULL,
                                            `priority` int(3) NOT NULL,
                                            `delete_flag` tinyint(1) NULL DEFAULT 0,
                                            PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ews_topic_rule_relation
-- ----------------------------
INSERT INTO `ews_topic_rule_relation` VALUES (1, 1, 1, 1, 1, 0);
INSERT INTO `ews_topic_rule_relation` VALUES (2, 2, 2, 1, 1, 0);

SET FOREIGN_KEY_CHECKS = 1;
