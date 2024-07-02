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
@TableName("prompt_usage_records")
@ApiModel(value="PromptUsageRecords对象", description="")
public class PromptUsageRecords implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "PromptID")
    private String promptId;

    @ApiModelProperty(value = "Prompt ")
    private String prompt;

    @ApiModelProperty(value = "回答")
    private String respondent;

    @ApiModelProperty(value = "附加信息")
    private String additional;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;


}
