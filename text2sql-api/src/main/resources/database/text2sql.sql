/*
 Navicat Premium Data Transfer

 Source Server         : dev
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3306
 Source Schema         : text2sql

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 02/07/2024 22:02:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大模型',
  `remote_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `auth_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '秘钥',
  `assistant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小助手',
  `token` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Token',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '是否删除',
  `conversation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会话 ID',
  `is_active` int(0) NULL DEFAULT NULL COMMENT '是否激活',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prompt_prompts
-- ----------------------------
DROP TABLE IF EXISTS `prompt_prompts`;
CREATE TABLE `prompt_prompts`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `scene` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '场景',
  `prompt_template` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'prompt',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除',
  `is_active` tinyint(0) NULL DEFAULT NULL COMMENT '是否激活',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

INSERT INTO `text2sql`.`prompt_prompts`(`id`, `scene`, `prompt_template`, `create_time`, `update_time`, `is_deleted`, `is_active`) VALUES ('1fe63fef-be59-4bf6-88ee-6214f64f00a9', 'GENERATE_MYBATIS_XML', '你是一名资深 Java 开发工程师，请按照如下要求将提供的 SQL 语句转换为 Java 中可执行的 Mybatis 执行脚本：\r\nSQL 语句如下所示：\r\n```\r\n{{ SQL }}\r\n```\r\n需求：{{ demand }}\r\n要求：最终结果直接输出 Mybatis 的xml 写法即可并且 xml 中的 resultType 请按照提供的 SQL 自定义即可，不需要代码解释\r\n输出示例：\r\n```\r\n<select id=\"selectMembersWithReservations\" resultType=\"map\">\r\n        SELECT \r\n            m.MemberID, \r\n            m.FullName, \r\n            r.TableID\r\n        FROM \r\n            Members m\r\n        JOIN \r\n            Reservations r \r\n        ON \r\n            m.MemberID = r.MemberID\r\n        ORDER BY \r\n            m.MemberID;\r\n    </select>\r\n```', '2024-05-25 15:35:13', '2024-05-25 15:34:33', 0, 1);
INSERT INTO `text2sql`.`prompt_prompts`(`id`, `scene`, `prompt_template`, `create_time`, `update_time`, `is_deleted`, `is_active`) VALUES ('9261117a-04b5-4fa7-93e7-8e76a272cd4a', 'TRANSFORM_SQL_TO_JSON', '你是一名资深 Java 开发工程师，请你按照以下要求将 SQL 语句转换成JSON：\r\n数据库表结构如下所示：\r\n```\r\n{{ SQL }}\r\n```\r\nJSON 属性与数据库表字段之间的对应关系：\r\n```\r\n数据库表的表名对应 JSON 中的 schemaName 属性，数据库中的字段是解析在JSON对象的 fields 数组属性中，并且每个数据库表下的字段都会是 fields 数组下的一个 field 对象。\r\n并且 field 对象中的字段类型 fieldType 只能对应到如下类型中的其中一个 [varchar, int, bigint, double, float, decimal, date, datetime, timestamp, text, blob]\r\n```\r\n需求：请你将上述的 SQL 建表语句转换为 JSON 字符串，并且 SQL 语句可以是多个，建表语句之间使用分号作为分隔，每个分号划分的就是两个建表语句。\r\n要求：只需要输出转换成功的 JSON 数据即可，如果只有一个表结构时希望返回的是一个 JSON 字符串对象，如果是多个表结构，请你必须输出一个 JSON 数组，每个 JSON 对象之间不要使用分号分隔，而是统一放在数组中。并且输出内容一定要按照输出示例中的要求输出，不需要输出其他多余的解释内容。\r\n输出示例：\r\n1. 只有一个建表语句时，输出如下格式：\r\n```\r\n{\r\n  \"schemaName\": \"ChargingRecords\",\r\n  \"fields\": [\r\n    {\r\n      \"fieldName\": \"RecordID\",\r\n      \"fieldType\": \"varchar\",\r\n      \"fieldDefault\": \"\",\r\n      \"fieldComment\": \"充电记录 ID\",\r\n      \"primaryKey\": true,\r\n      \"isNull\": true\r\n    },\r\n    {\r\n      \"fieldName\": \"UserID\",\r\n      \"fieldType\": \"varchar\",\r\n      \"fieldDefault\": \"\",\r\n      \"fieldComment\": \"用户 ID\",\r\n      \"primaryKey\": false,\r\n      \"isNull\": true\r\n    }\r\n  ]\r\n}\r\n```\r\n2. 存在多个建表语句时，输出如下格式：\r\n```\r\n[\r\n  {\r\n    \"schemaName\": \"ChargingRecords\",\r\n    \"fields\": [\r\n      {\r\n        \"fieldName\": \"RecordID\",\r\n        \"fieldType\": \"varchar\",\r\n        \"fieldDefault\": \"\",\r\n        \"fieldComment\": \"充电记录 ID\",\r\n        \"primaryKey\": true,\r\n        \"isNull\": true\r\n      },\r\n      {\r\n        \"fieldName\": \"UserID\",\r\n        \"fieldType\": \"varchar\",\r\n        \"fieldDefault\": \"\",\r\n        \"fieldComment\": \"用户 ID\",\r\n        \"primaryKey\": false,\r\n        \"isNull\": true\r\n      }\r\n    ]\r\n  },\r\n  {\r\n    \"schemaName\": \"Users\",\r\n    \"fields\": [\r\n      {\r\n        \"fieldName\": \"UserID\",\r\n        \"fieldType\": \"varchar\",\r\n        \"fieldDefault\": \"\",\r\n        \"fieldComment\": \"用户 ID\",\r\n        \"primaryKey\": true,\r\n        \"isNull\": true\r\n      },\r\n      {\r\n        \"fieldName\": \"FullName\",\r\n        \"fieldType\": \"varchar\",\r\n        \"fieldDefault\": \"\",\r\n        \"fieldComment\": \"用户全名\",\r\n        \"primaryKey\": false,\r\n        \"isNull\": true\r\n      }\r\n    ]\r\n  }\r\n]\r\n```', '2024-05-25 16:19:06', '2024-05-25 16:19:08', 0, 1);
INSERT INTO `text2sql`.`prompt_prompts`(`id`, `scene`, `prompt_template`, `create_time`, `update_time`, `is_deleted`, `is_active`) VALUES ('c670b1e8-5f4e-4706-8b2a-6ca1b91fcbbf', 'GENERATE_QUERY_SQL', '你是一名数据库管理员，请按照以下要求使用 {{ driverType }} 语法编写 SQL 语句：\r\n数据库表结构如下所示：\r\n```\r\n{{ schema }}\r\n```\r\n查询需求：{{ demand }}\r\n要求：最终结果直接输出SQL 代码即可并且输出结果尽可能达到最优解和使用SQL注释的语法解释为什么 SQL 要这么写以及给出可能得优化建议并按照输出示例的格式输出，不需要代码解释\r\n输出示例：\r\n```\r\nSELECT s.id AS student_id, s.s_name AS student_name, c.id AS course_id, c.c_name AS course_name, sc.score AS score\r\nFROM students s\r\nJOIN scores sc ON s.id = sc.s_id\r\nJOIN courses c ON sc.c_id = c.id\r\nORDER BY sc.score DESC;\r\n\r\n-- 优化建议：\r\n-- 确保表中的连接字段都有索引，以提高查询性能。\r\n-- 可以考虑对 scores 表中的分数字段进行数据类型优化，例如使用整型代替字符型，以提高查询效率。\r\n-- 如果数据量较大，可以考虑对成绩表（scores）和课程表（courses）进行分区操作，以加快查询速度。\r\n\r\n```', '2024-05-25 09:58:51', '2024-05-25 09:58:53', 0, 1);
INSERT INTO `text2sql`.`prompt_prompts`(`id`, `scene`, `prompt_template`, `create_time`, `update_time`, `is_deleted`, `is_active`) VALUES ('d0bd0ec4-25d2-48ff-994f-1f216ce5783c', 'GENERATE_INSERT_SQL', '你是一名数据库管理员，请按照以下要求使用 MySQL 语法编写 Insert SQL 语句：\r\n数据库表结构如下：\r\n```\r\n{{ SQL }}\r\n```\r\n需求：请你为这张表生成 20 条测试数据\r\n要求：最终结果直接输出 Insert SQL 代码即可，按照输出示例中的格式输出即可，不需要输出其他多余的解释内容。\r\n输出示例如下：\r\n```\r\ninsert into ChargingRecords (RecordID, UserID, StationID, StartTime, EndTime, EnergyConsumed, TotalCost) values (xxx)\r\n```', '2024-05-25 15:53:04', '2024-05-25 15:53:06', 0, 1);


-- ----------------------------
-- Table structure for prompt_usage_records
-- ----------------------------
DROP TABLE IF EXISTS `prompt_usage_records`;
CREATE TABLE `prompt_usage_records`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `prompt_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'PromptID',
  `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'Prompt ',
  `respondent` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回答',
  `additional` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附加信息',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint(0) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

INSERT INTO `text2sql`.`role`(`id`, `role_name`, `role_code`, `status`, `remark`, `create_time`, `update_time`) VALUES ('0', '游客', 'VISITOR', 0, '游客角色', '2023-12-14 00:17:12', '2023-12-14 00:17:16');
INSERT INTO `text2sql`.`role`(`id`, `role_name`, `role_code`, `status`, `remark`, `create_time`, `update_time`) VALUES ('1', '普通用户', 'USER', 0, '普通用户角色', '2023-12-14 00:17:55', '2023-12-14 00:17:58');
INSERT INTO `text2sql`.`role`(`id`, `role_name`, `role_code`, `status`, `remark`, `create_time`, `update_time`) VALUES ('2', '管理员', 'ADMIN', 0, '管理员角色', '2023-12-14 00:18:19', '2023-12-14 00:18:21');

-- ----------------------------
-- Table structure for t_database
-- ----------------------------
DROP TABLE IF EXISTS `t_database`;
CREATE TABLE `t_database`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `database_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '是否删除',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户 ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_field
-- ----------------------------
DROP TABLE IF EXISTS `t_field`;
CREATE TABLE `t_field`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `schema_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `field_type` int(0) NULL DEFAULT NULL COMMENT '字段类型',
  `field_default` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `field_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注释',
  `primary_key` int(0) NULL DEFAULT NULL COMMENT '主键',
  `is_null` int(0) NULL DEFAULT NULL COMMENT '是否为 null',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_schema
-- ----------------------------
DROP TABLE IF EXISTS `t_schema`;
CREATE TABLE `t_schema`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `schema_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `data_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库 ID',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '是否删除',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户 ID',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_online` tinyint(0) NULL DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` int(0) NULL DEFAULT NULL,
  `status` tinyint(0) NULL DEFAULT NULL,
  `is_deleted` tinyint(0) NULL DEFAULT 0,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
