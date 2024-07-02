package com.pdx.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pdx.model.enums.FieldType;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/24
 * @Description: 字符串工具类
 */
public class StringUtil {

    /**
     * 根据给定的JSON字符串生成创建表的SQL语句。
     *
     * @param tableJson 描述表结构和字段的JSON字符串，需要包含数据库名、模式名、备注以及字段详细信息。
     * @return 生成的创建表SQL语句，如果解析失败或字段列表为空，则返回null。
     */
    public static String generateCreateTableSQL(String tableJson) {
        JSONObject rootObject;
        try {
            rootObject = JSONUtil.parseObj(tableJson);
        } catch (Exception e) {
            // JSON解析失败处理逻辑
            System.err.println("JSON解析失败: " + e.getMessage());
            return null;
        }

        // 提取数据库名、模式名和备注信息
        String databaseName = rootObject.getStr("databaseName");
        String schemaName = rootObject.getStr("schemaName");
        String remark = rootObject.getStr("remark");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(schemaName).append(" (\n");

        // 解析字段信息
        JSONArray fields = rootObject.getJSONArray("fields");
        if (fields.isEmpty()) {
            // 字段列表为空处理逻辑
            System.err.println("字段列表为空，无法生成SQL");
            return null;
        }

        // 遍历字段，生成字段定义
        for (int i = 0; i < fields.size(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String fieldName = field.getStr("fieldName");
            // 字段名验证逻辑
            if (!isValidFieldName(fieldName)) {
                System.err.println("非法的字段名: " + fieldName);
                continue;
            }
            int fieldType = field.getInt("fieldType");
            String fieldDefault = field.getStr("fieldDefault");
            String fieldComment = field.getStr("fieldComment");
            boolean primaryKey = field.getBool("primaryKey");
            boolean isNullable = field.getBool("isNull");

            sqlBuilder.append("    ").append(fieldName).append(" ").append(FieldType.getType(fieldType));

            if (!isNullable) {
                sqlBuilder.append(" NOT NULL");
            }

            // 添加默认值定义
            if (isValidFieldDefault(fieldDefault)) {
                sqlBuilder.append(" DEFAULT '").append(fieldDefault).append("'");
            }

            // 添加字段注释
            sqlBuilder.append(" COMMENT '").append(fieldComment).append("'");

            // 定义主键
            if (primaryKey) {
                sqlBuilder.append(",\n    PRIMARY KEY (").append(fieldName).append(")");
            }

            sqlBuilder.append(",\n");
        }

        // 移除最后一个逗号和换行符
        sqlBuilder.setLength(sqlBuilder.length() - 2);

        // 添加表备注
        sqlBuilder.append("\n) COMMENT '").append(remark).append("';");

        return sqlBuilder.toString();
    }

    /**
     * 验证字段名是否合法。
     * 这里只是一个示例实现，实际应用中需要根据具体要求进行严格的验证。
     *
     * @param fieldName 待验证的字段名
     * @return 如果字段名合法，则返回true，否则返回false
     */
    private static boolean isValidFieldName(String fieldName) {
        // 示例验证逻辑
        return fieldName != null && !fieldName.isEmpty() && fieldName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    /**
     * 验证字段默认值是否合法。
     * 这里只是一个示例实现，实际应用中需要根据字段类型等信息进行严格的验证。
     *
     * @param fieldDefault 待验证的字段默认值
     * @return 如果字段默认值合法，则返回true，否则返回false
     */
    private static boolean isValidFieldDefault(String fieldDefault) {
        // 示例验证逻辑
        return fieldDefault != null && !fieldDefault.isEmpty();
    }

    /**
     * 根据字段类型获取对应的SQL类型字符串。
     * 这里只是一个示例实现，需要根据实际的字段类型映射关系进行具体实现。
     *
     * @param fieldType 字段的类型代码
     * @return 对应的SQL类型字符串
     */
    private static String getSQLType(int fieldType) {
        // 示例实现
        switch (fieldType) {
            case 0: return "VARCHAR(255)";
            case 1: return "INT";
            default: return "UNKNOWN";
        }
    }


    public static void main(String[] args) {
        String json = "{ \"databaseName\": \"stuent_manager\", \"schemaName\": \"students\", \"remark\": \"学生表\", \"databaseId\": \"0000000-00000\", \"schemaId\": \"000000000-0000000\", \"fields\": [{ \"id\": \"000000-000001\", \"fieldName\": \"id\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 1, \"fieldDefault\": null, \"fieldComment\": \"学号\", \"primaryKey\": true, \"createTime\": \"2024-05-19T04:35:10.000+00:00\", \"updateTime\": \"2024-05-19T04:35:12.000+00:00\", \"null\": true, \"deleted\": false }, { \"id\": \"000000-000002\", \"fieldName\": \"s_name\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 1, \"fieldDefault\": null, \"fieldComment\": \"姓名\", \"primaryKey\": false, \"createTime\": \"2024-05-19T04:35:10.000+00:00\", \"updateTime\": \"2024-05-19T04:35:12.000+00:00\", \"null\": false, \"deleted\": false }, { \"id\": \"000000-000003\", \"fieldName\": \"s_class\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 1, \"fieldDefault\": null, \"fieldComment\": \"班级\", \"primaryKey\": false, \"createTime\": \"2024-05-19T04:37:04.000+00:00\", \"updateTime\": \"2024-05-19T04:37:06.000+00:00\", \"null\": false, \"deleted\": false }, { \"id\": \"000000-000004\", \"fieldName\": \"s_gender\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 1, \"fieldDefault\": \"未知\", \"fieldComment\": \"性别\", \"primaryKey\": false, \"createTime\": \"2024-05-19T04:37:04.000+00:00\", \"updateTime\": \"2024-05-19T04:37:06.000+00:00\", \"null\": false, \"deleted\": false }, { \"id\": \"000000-000005\", \"fieldName\": \"s_major\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 1, \"fieldDefault\": null, \"fieldComment\": \"专业\", \"primaryKey\": false, \"createTime\": \"2024-05-19T04:37:04.000+00:00\", \"updateTime\": \"2024-05-19T04:37:06.000+00:00\", \"null\": false, \"deleted\": false }, { \"id\": \"000000-000006\", \"fieldName\": \"s_birthday\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 2, \"fieldDefault\": null, \"fieldComment\": \"生日\", \"primaryKey\": false, \"createTime\": \"2024-05-19T04:37:04.000+00:00\", \"updateTime\": \"2024-05-19T04:37:06.000+00:00\", \"null\": false, \"deleted\": false }, { \"id\": \"000000-000007\", \"fieldName\": \"credit_points\", \"schemaId\": \"000000000-0000000\", \"fieldType\": 1, \"fieldDefault\": null, \"fieldComment\": \"学分\", \"primaryKey\": false, \"createTime\": \"2024-05-19T04:37:04.000+00:00\", \"updateTime\": \"2024-05-19T04:37:06.000+00:00\", \"null\": false, \"deleted\": false }], \"system\": true }";
        System.out.println(generateCreateTableSQL(json));
    }
}
