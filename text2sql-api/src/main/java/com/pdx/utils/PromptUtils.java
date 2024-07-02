package com.pdx.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/24
 * @Description: DO AnyThing...
 */
public class PromptUtils {

    /**
     * 生成生成 SQL 语句的 Prompt
     *
     * @param schemaContent 数据库表内容
     * @param promptTemplate 模板
     * @return Prompt
     */
    public static String createGenerateSQLPrompt(String schemaContent, String promptTemplate, String driverType, String demand) {
        if (StringUtils.isEmpty(schemaContent)) {
            return promptTemplate;
        }
        return promptTemplate
                .replace("{{ driverType }}", driverType)
                .replace("{{ demand }}", demand)
                .replace("{{ schema }}", schemaContent);
    }

    /**
     * 生成 SQL 转换为 JSON 的 Prompt
     *
     * @param importContent SQL 内容
     * @param abstractPrompt 模板
     * @return Prompt
     */
    public static String createTransformSQLTOJson(String importContent, String abstractPrompt) {
        if (StringUtils.isEmpty(importContent)) {
            return abstractPrompt;
        }
        return abstractPrompt.replace("{{ SQL }}", importContent);
    }
}
