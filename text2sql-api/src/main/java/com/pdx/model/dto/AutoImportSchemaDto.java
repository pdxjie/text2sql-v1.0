package com.pdx.model.dto;

import com.pdx.model.entity.Field;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/25
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "自动导入表结构")
public class AutoImportSchemaDto {

    private String schemaName;

    private List<FieldDto> fields;
}
