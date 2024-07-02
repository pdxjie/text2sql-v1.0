package com.pdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/28
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "移动数据库")
public class MoveDatabaseVo {

    @ApiModelProperty(value = "数据库 ID")
    private String databaseId;

    @ApiModelProperty(value = "目标数据库")
    private String targetDatabaseId;

    @ApiModelProperty(value = "数据库表 ID")
    private String schemaId;
}
