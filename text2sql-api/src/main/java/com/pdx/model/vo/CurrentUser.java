package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/*
 * @Author 派同学
 * @Description 当前用户
 * @Date 2023/7/27
 **/
@Data
@Builder
public class CurrentUser {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "是否在线")
    private boolean isOnline;

    @ApiModelProperty(value = "登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "用户所在地")
    private String address;
}
