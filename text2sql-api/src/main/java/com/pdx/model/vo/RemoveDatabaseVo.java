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
@ApiModel(description = "删除数据库的请求体")
public class RemoveDatabaseVo {

    @ApiModelProperty(value = "数据库 ID")
    private String databaseId;

    @ApiModelProperty(value = "是否强制删除")
    private boolean force;
}
