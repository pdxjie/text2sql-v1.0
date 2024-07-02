package com.pdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/18
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "数据库插入 Vo")
public class DatabaseInsertVo {

    @ApiModelProperty(value = "数据库名称")
    private String databaseName;

}
