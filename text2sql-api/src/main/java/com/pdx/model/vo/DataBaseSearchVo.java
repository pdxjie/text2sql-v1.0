package com.pdx.model.vo;

import com.pdx.utils.PageParams;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/18
 * @Description: DO AnyThing...
 */
@Data
@ApiModel(description = "数据库查询参数")
public class DataBaseSearchVo extends PageParams {

    private String databaseName;

}
