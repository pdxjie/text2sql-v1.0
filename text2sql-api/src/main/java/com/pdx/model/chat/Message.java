package com.pdx.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Message {

    @ApiModelProperty(value = "聊天角色",example = "user")
    private String role;

    @ApiModelProperty(value = "聊天内容",example = "你好")
    private String content;
}
