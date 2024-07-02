package com.pdx.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * @Author 派同学
 * @Description 响应枚举类
 * @Date 2023/7/24
 **/
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "操作成功"),

    FAIL(201, "操作失败"),

    TOKEN_EXPIRE(202, "Token已失效，请重新登录"),

    ERROR_PARAM(203, "参数错误"),

    ACCESS_DENIED(204, "无权访问，请联系管理员"),

    NEED_LOGIN(205, "请前往登录"),

    INVALID_LOGIN_INFO(206, "Token信息无效，请重新登录"),

    USER_INFO_EXISTED(208, "该邮箱已被注册，请重试！"),

    PASSWORD_IS_NOT_EQUAL(209, "密码与确认密码不相同"),

    INVALID_CODE(210, "验证码无效，请重新发送！"),

    ERROR_CODE(211, "验证码无效，请重新发送！"),

    USER_FORBIDDEN(212, "该账户已被禁用，请联系管理员！"),

    ILLEGAL_OPERATE(213, "非法操作！"),

    LESS_THAN_FOUR(214, "批量上传图片不得超过四张"),

    USERINFO_IS_NOT_EXISTS(207, "用户名或密码错误，请重试！");


    // 状态码
    private Integer code;

    // 响应信息
    private String message;

}
