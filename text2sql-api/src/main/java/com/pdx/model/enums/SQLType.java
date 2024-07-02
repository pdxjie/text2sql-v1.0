package com.pdx.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/24
 * @Description: DO AnyThing...
 */
@Getter
@AllArgsConstructor
public enum SQLType {

    MYSQL(1, "MYSQL"),

    POSTGRESQL(2, "POSTGRESQL"),

    ORACLE(4, "ORACLE"),

    SQLSERVER(3, "SQLSERVER"),
    ;

    private final Integer code;

    private final String type;

    public static String getType(Integer code) {
        for (SQLType sqlType : SQLType.values()) {
            if (sqlType.getCode().equals(code)) {
                return sqlType.getType();
            }
        }
        return "MYSQL";
    }
}
