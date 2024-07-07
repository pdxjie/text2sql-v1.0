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
 * @since 2024-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("conn_config")
@ApiModel(value="ConnConfig对象", description="")
public class ConnConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "连接名称")
    private String connNick;

    @ApiModelProperty(value = "主机")
    private String connHost;

    @ApiModelProperty(value = "端口")
    private String connPort;

    @ApiModelProperty(value = "用户名")
    private String connUser;

    @ApiModelProperty(value = "密码")
    private String connPass;

    @ApiModelProperty(value = "保存密码")
    private Integer savePass;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "初始数据库")
    private String initDatabase;

    @ApiModelProperty(value = "用户 ID")
    private String userId;


}
