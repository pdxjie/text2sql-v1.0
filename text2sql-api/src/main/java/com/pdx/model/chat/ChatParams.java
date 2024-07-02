package com.pdx.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @Author: IT 派同学
 * @DateTime: 2023/12/24
 * @Description: 聊天参数
 */
@Data
public class ChatParams {

    @ApiModelProperty(value = "模型", example = "gpt-3.5-turbo")
    private String model = "gpt-3.5-turbo";

    @ApiModelProperty(value = "消息列表", notes = "获取上下文即携带返回消息")
    private List<ChatMessage> messages;

    @DecimalMin("0.0")
    @DecimalMax("2.0")
    @ApiModelProperty(value = "采样温度",notes = "较高的值（如 0.8）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定", example = "1.0")
    private Double temperature = 1.0;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @ApiModelProperty(value = "核心采样",notes = "其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的结果集",example = "1.0")
    private Double top_p = 1.0;

    @Min(1)
    @ApiModelProperty(value = "结果数",notes = "如果需要返回多个结果可以设置 n > 1",example = "1")
    private Integer n = 1;

    @ApiModelProperty(value = "当前用户ID",notes = "用于处理滥用行为",example = "匿名用户")
    private String user;

    @ApiModelProperty(value = "是否流式")
    private boolean stream;
}
