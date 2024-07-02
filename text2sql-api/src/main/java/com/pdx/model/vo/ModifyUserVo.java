package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description 修改用户字段
 * @Date 2023/8/5
 **/
@Data
public class ModifyUserVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "个人描述")
    private String remark;
}
