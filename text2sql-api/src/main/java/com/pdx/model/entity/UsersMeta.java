package com.pdx.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 派同学
 * @since 2024-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users_meta")
@ApiModel(value="UsersMeta对象", description="")
public class UsersMeta implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String metaKey;

    private String metaValue;

    private String userId;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;


}
