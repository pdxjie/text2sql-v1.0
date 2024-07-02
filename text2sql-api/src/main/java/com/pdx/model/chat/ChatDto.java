package com.pdx.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: IT 派同学
 * @DateTime: 2023/12/24
 * @Description:
 */
@Data
public class ChatDto {

    @ApiModelProperty("chat-id")
    private String id;

    @ApiModelProperty("调用对象")
    private String object;

    @ApiModelProperty("创建ID")
    private Long created;

    @ApiModelProperty("使用模型")
    private String model;

    @ApiModelProperty("token消耗")
    private Map<String,String> usage;

    @ApiModelProperty("可选结果集合")
    List<ChoiceModel> choices;
}
