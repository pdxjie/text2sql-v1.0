package com.pdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/18
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "数据库分页查询 DTO")
public class DataBasePageDto {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "数据库名称")
    private String databaseName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    private Date createTime;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
