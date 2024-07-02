package com.pdx.annotation;

import com.pdx.model.enums.CheckType;

import java.lang.annotation.*;

/*
 * @Author 派同学
 * @Description 校验角色注解
 * @Date 2023/7/25
 **/
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRoles {

    String[] value() default {};

    CheckType checkType() default CheckType.AND;
}
