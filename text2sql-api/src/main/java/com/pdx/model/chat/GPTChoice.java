package com.pdx.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GPTChoice {

    @ApiModelProperty(value = "The generated text")
    private String text;

    @ApiModelProperty(value = "The index of the choice")
    private Integer index;

    @ApiModelProperty(value = "The message object for the choice")
    private Message message;

}
