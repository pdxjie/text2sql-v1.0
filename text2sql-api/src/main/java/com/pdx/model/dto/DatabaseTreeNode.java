package com.pdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/19
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "数据库树形节点")
public class DatabaseTreeNode {

    @ApiModelProperty(value = "数据库表 ID")
    private String key;

    @ApiModelProperty(value = "数据库表名称")
    private String title;

    @ApiModelProperty(value = "是否叶子节点")
    private boolean isLeaf = true;

    @ApiModelProperty(value = "是否系统模版")
    private String type;
}
