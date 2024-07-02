package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/*
 * @Author 派同学
 * @Description 修改用户密码请求体
 * @Date 2023/8/5
 **/
@Data
public class UpdatePasswordVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "旧密码")
    @NotNull
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotNull
    private String newPassword;
}
