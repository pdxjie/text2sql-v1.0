package com.pdx.model.vo;

import com.pdx.utils.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description 查询角色请求体
 * @Date 2023/8/5
 **/
@Data
public class QueryRoleVo extends PageParams {

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "角色状态")
    private Integer status;
}
