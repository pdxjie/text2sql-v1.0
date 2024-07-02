package com.pdx.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
public class FieldDto {

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

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
}
