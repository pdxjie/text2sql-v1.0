package com.pdx.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

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
@TableName("t_field")
@ApiModel(value="Field对象", description="")
public class Field implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "表名")
    private String schemaId;

    @ApiModelProperty(value = "字段类型")
    private Integer fieldType;

    @ApiModelProperty(value = "默认值")
    private String fieldDefault;

    @ApiModelProperty(value = "注释")
    private String fieldComment;

    @ApiModelProperty(value = "主键")
    private boolean primaryKey;

    @ApiModelProperty(value = "是否为 null")
    private boolean isNull;

    @ApiModelProperty(value = "是否删除")
    private boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
