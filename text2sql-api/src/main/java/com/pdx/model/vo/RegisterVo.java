package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/*
 * @Author 派同学
 * @Description 注册用户参数
 * @Date 2023/7/26
 **/
@Data
public class RegisterVo {

    @ApiModelProperty(value = "邮箱")
    @Email
    @NotNull(message = "邮箱不能为空！")
    private String email;

    @ApiModelProperty(value = "登录密码")
    @NotNull(message = "密码不能为空！")
    private String password;

    @ApiModelProperty(value = "确认密码")
    @NotNull(message = "确认密码不能为空！")
    private String confirmPassword;

    @ApiModelProperty(value = "验证码")
    @NotNull(message = "验证码不能为空！")
    private String code;
}
