package com.pdx.model.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/21
 * @Description: 字段类型枚举
 */
@AllArgsConstructor
@Getter
public enum FieldType {

    VARCHAR("varchar", 1),

    INT("int", 2),

    BIGINT("bigint", 3),

    DOUBLE("double", 4),

    FLOAT("float", 5),

    DECIMAL("decimal", 6),

    DATE("date", 7),

    DATETIME("datetime", 8),

    TIMESTAMP("timestamp", 9),

    TEXT("text", 10),

    BLOB("blob", 11),

    ;

    @ApiModelProperty(value = "字段类型")
    private final String type;

    @ApiModelProperty(value = "字段类型编码")
    private final Integer code;

    /**
     * 根据编码获取字段类型
     * @param code 字段类型编码
     * @return 字段类型
     */
    public static String getType(Integer code) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.getCode().equals(code)) {
                return fieldType.getType();
            }
        }
        return "varchar(255)";
    }

    /**
     * 根据类型获取字段类型编码
     *
     * @param type 字段类型
     * @return 字段类型编码
     */
    public static Integer getCode(String type) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.getType().equals(type)) {
                return fieldType.getCode();
            }
        }
        return 1;
    }
}
