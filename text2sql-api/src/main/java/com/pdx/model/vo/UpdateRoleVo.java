package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description 修改角色Vo
 * @Date 2023/8/5
 **/
@Data
public class UpdateRoleVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "Role Code")
    private String roleCode;

    @ApiModelProperty(value = "Role名称")
    private String roleName;

    @ApiModelProperty(value = "描述")
    private String remark;
}
