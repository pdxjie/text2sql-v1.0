package com.pdx.utils;

import cn.hutool.core.lang.UUID;

/*
 * @Author 派同学
 * @Description 加密工具类
 * @Date 2023/7/24
 **/
public class PasswordUtils {

    /**
     * 匹配密码
     * @param salt 盐
     * @param rawPass 明文
     * @param encPass 密文
     * @return
     */
    public static boolean matches(String salt, String rawPass, String encPass) {
        return new PasswordEncoder(salt).matches(encPass, rawPass);
    }

    /**
     * 明文密码加密
     * @param rawPass 明文
     * @param salt
     * @return
     */
    public static String encode(String rawPass, String salt) {
        return new PasswordEncoder(salt).encode(rawPass);
    }

    /**
     * 获取加密盐
     * @return
     */
    public static String getSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    public static void main(String[] args) {
        String salt = getSalt();
        System.out.println("得到的加密盐："+salt);
        String encode = encode("Wumao233031", salt);
        System.out.println("加密后的密码："+encode);
        System.out.println(matches(salt, "Wumao233031", encode));
    }
}
