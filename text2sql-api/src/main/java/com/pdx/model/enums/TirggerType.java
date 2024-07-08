package com.pdx.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/8
 * @Description: DO AnyThing...
 */
@Getter
@AllArgsConstructor
public enum TirggerType {

    // 测试
    CHECK,

    // 保存
    SAVE,

    // 更新
    UPDATE,

    // 删除
    DELETE;

}
