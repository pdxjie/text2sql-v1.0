package com.pdx.model.entity;

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
 * @since 2024-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("group_config")
@ApiModel(value="GroupConfig对象", description="")
public class GroupConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "分组 ID")
    private String groupId;

    @ApiModelProperty(value = "连接 ID")
    private String connId;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "用户 ID")
    private String userId;
}
