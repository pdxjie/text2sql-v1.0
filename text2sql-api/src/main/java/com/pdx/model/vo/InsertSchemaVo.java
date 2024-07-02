package com.pdx.model.vo;

import com.pdx.model.entity.Field;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/22
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "新增 Schema")
public class InsertSchemaVo {

    private String databaseId;

    private String databaseName;

    private String schemaName;

    private String remark;

    private String schemaId;

    private List<Field> fields;
}
