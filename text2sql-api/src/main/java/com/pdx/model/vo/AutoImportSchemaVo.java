package com.pdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/25
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "自动导入表结构")
public class AutoImportSchemaVo {

    @ApiModelProperty(value = "数据库 ID")
    private String databaseId;

    @ApiModelProperty(value = "导入内容")
    private String importContent;

    @ApiModelProperty(value = "是否多表导入")
    private boolean moreImport;
}
