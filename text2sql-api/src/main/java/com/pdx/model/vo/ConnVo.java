package com.pdx.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/7
 * @Description: 连接信息 VO
 */
@Data
public class ConnVo {

    @ApiModelProperty(value = "连接名称")
    private String connNick;

    @ApiModelProperty(value = "主机")
    private String connHost;

    @ApiModelProperty(value = "端口")
    private String connPort;

    @ApiModelProperty(value = "用户名")
    private String connUser;

    @ApiModelProperty(value = "密码")
    private String connPass;

    @ApiModelProperty(value = "是否保存密码")
    private boolean savePass;

    @ApiModelProperty(value = "初始数据库")
    private String initDatabase;

    @ApiModelProperty(value = "数据源")
    private Integer connSource;

    @ApiModelProperty(value = "触发类型")
    private String type;
}
