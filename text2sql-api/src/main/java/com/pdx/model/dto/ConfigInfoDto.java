package com.pdx.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/9
 * @Description: DO AnyThing...
 */
@Data
public class ConfigInfoDto {

    @ApiModelProperty(value = "主键")
    private String key;

    @ApiModelProperty(value = "连接名称")
    private String title;

    @ApiModelProperty(value = "保存密码")
    private Integer savePass;

    @ApiModelProperty(value = "用户 ID")
    private String userId;

    @ApiModelProperty(value = "层级")
    private String level;

    @ApiModelProperty(value = "子节点")
    private List<ConfigInfoDto> children;
}
