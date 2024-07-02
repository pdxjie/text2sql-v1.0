package com.pdx.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @DateTime: 2023/12/24
 * @Description:
 */
@Data
public class ChoiceModel {

    @ApiModelProperty(value = "结果下标")
    private Integer index;

    @ApiModelProperty(value = "终止原因")
    private String finish_reason;

    @ApiModelProperty(value = "可选结果")
    private ChatMessage message;
}
