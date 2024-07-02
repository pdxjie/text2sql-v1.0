package com.pdx.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 派同学
 * @since 2023-07-24
 */
@Data
public class UserDto implements Serializable {

    @ApiModelProperty(value = "主键ID ")
    private String id;

    @ApiModelProperty(value = "是否在线")
    private boolean isOnline;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "0 男 1 女 2 未知")
    private Integer sex;

    @ApiModelProperty(value = "true 禁用 false 正常")
    private boolean status;

    @ApiModelProperty(value = "是否删除 0 未删除 1 已删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "角色")
    private String role;


}
