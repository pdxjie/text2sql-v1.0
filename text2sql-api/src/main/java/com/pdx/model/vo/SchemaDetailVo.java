package com.pdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/21
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "Schema 详细信息")
public class SchemaDetailVo {

    @ApiModelProperty(value = "数据库 ID")
    private String dataId;

    @ApiModelProperty(value = "Schema ID")
    private String schemaId;
}
