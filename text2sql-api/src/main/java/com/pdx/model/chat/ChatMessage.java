package com.pdx.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @DateTime: 2023/12/24
 * @Description: 聊天信息
 */
@Data
public class ChatMessage {

    @ApiModelProperty(value = "聊天角色",example = "user")
    String role;

    @ApiModelProperty(value = "聊天内容", example = "hello")
    String content;

}
