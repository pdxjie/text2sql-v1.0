package com.pdx.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("model")
@ApiModel(value="Model对象", description="")
public class Model implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "大模型")
    private String model;

    @ApiModelProperty(value = "路由地址")
    private String remoteUrl;

    @ApiModelProperty(value = "秘钥")
    private String authKey;

    @ApiModelProperty(value = "小助手")
    private String assistantId;

    @ApiModelProperty(value = "Token")
    private String token;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    @ApiModelProperty(value = "是否启用")
    private Integer isActive;

    @ApiModelProperty(value = "会话 ID")
    private String conversationId;


}
