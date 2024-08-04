package com.pdx.model.constants;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 派同学
 * @Description 基础常量
 * @Date 2023/7/24
 **/
public interface BasicConstants {

    // 基础扫描路径
    public static final String BASIC_SCAN_PATH = "com.pdx";

    public static final String TOKEN = "token";

    // 邮箱前缀 key
    public static final String CAPTCHA_PREFIX = "email_code";

    // 默认头像
    public static final String DEFAULT_AVATAR = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjsgaolrgHJ9G4en9YAEF_-lPZUPyMmgjZLD3Xhksrxw&s";

    // 用户ID
    public static final String USER_ID_KEY = "userId";

    // 生产token时间
    public static final String CLAIM_DATE_KEY = "claim_date_key";

    // 默认简介
    public static final String DEFAULT_REMARK = "说些什么介绍一下自己吧~";

    // 内网IP
    public static final String DEFAULT_ADDRESS = "内网IP";

    // 默认需要过滤掉的数据库
    public static final List<String> FILTER_DATA_BASES = Arrays.asList("information_schema", "performance_schema", "mysql", "sys");

    // 1
    public static final Integer ONE_VALUE = 1;

    // 0
    public static final Integer ZERO_VALUE = 0;

    // 默认分组 ID
    public static final String DEFAULT_GROUP_ID = "000000-000000-000000-000000-000000";
}
