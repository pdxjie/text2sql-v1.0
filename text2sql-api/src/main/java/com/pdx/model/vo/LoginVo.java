package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/*
 * @Author 派同学
 * @Description 登录参数
 * @Date 2023/7/26
 **/
@Data
public class LoginVo {

    @ApiModelProperty(value = "邮箱")
    @NotNull(message = "邮箱不能为空！")
    @Email
    private String email;

    @ApiModelProperty(value = "验证码")
    @NotNull(message = "验证码不能为空！")
    private String code;
}
