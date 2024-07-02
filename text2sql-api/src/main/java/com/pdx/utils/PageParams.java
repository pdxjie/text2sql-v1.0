package com.pdx.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description 分页参数
 * @Date 2023/8/4
 **/
@Data
public class PageParams {

    @ApiModelProperty(value = "当前页")
    private Integer current = 1;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 10;
}
