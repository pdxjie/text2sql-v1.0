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
@TableName("t_schema")
@ApiModel(value="Schema对象", description="")
public class Schema implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "表名")
    private String schemaName;

    @ApiModelProperty(value = "数据库 ID")
    private String dataId;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "用户 ID")
    private String userId;

    @ApiModelProperty(value = "备注")
    private String remark;

}
