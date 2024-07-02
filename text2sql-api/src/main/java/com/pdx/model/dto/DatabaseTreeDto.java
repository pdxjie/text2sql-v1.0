package com.pdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/19
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "数据库树结构")
public class DatabaseTreeDto {

    @ApiModelProperty(value = "数据库 ID")
    private String key;

    @ApiModelProperty(value = "数据库名称")
    private String title;

    @ApiModelProperty(value = "是否叶子节点")
    private boolean isLeaf = false;

    @ApiModelProperty(value = "是否系统模版")
    private String type;

    @ApiModelProperty(value = "数据库节点")
    private List<DatabaseTreeNode> children;

    public DatabaseTreeDto() {
        this.children = new ArrayList<>();
    }
}
