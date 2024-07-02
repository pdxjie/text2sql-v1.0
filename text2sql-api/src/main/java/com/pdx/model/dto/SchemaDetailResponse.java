package com.pdx.model.dto;

import com.pdx.model.entity.Field;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/21
 * @Description: DO AnyThing...
 */
@Data
public class SchemaDetailResponse {

    @ApiModelProperty(value = "数据库名称")
    private String databaseName;

    @ApiModelProperty(value = "模式名称")
    private String schemaName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "数据库 ID")
    private String databaseId;

    @ApiModelProperty(value = "schema ID")
    private String schemaId;

    @ApiModelProperty(value = "字段列表")
    private List<Field> fields;

    @ApiModelProperty(value = "类型")
    private boolean system;
}
