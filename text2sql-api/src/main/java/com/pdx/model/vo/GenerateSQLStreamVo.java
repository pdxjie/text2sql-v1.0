package com.pdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/24
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "生成 SQL 请求体")
public class GenerateSQLStreamVo {

    @ApiModelProperty(value = "数据库 ID")
    private String databaseId;

    @ApiModelProperty(value = "数据库表 ID 集合")
    private List<String> schemaIds;

    @ApiModelProperty(value = "需求")
    private String demand;

    @ApiModelProperty(value = "数据库表内容")
    private String schemas;

    @ApiModelProperty(value = "生成方式 CUSTOM || SYSTEM")
    private String type;

    @ApiModelProperty(value = "数据库驱动 driver")
    private Integer sqlDriver;
}
