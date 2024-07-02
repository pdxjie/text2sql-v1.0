package com.pdx.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Random;

/*
 * @Author 派同学
 * @Description 密钥生成工具类
 * @Date 2023/7/24
 **/
@Slf4j
public class CipherUtil {

    private static final int AES_KEY_LENGTH = 16;

    public static String randomHexCipher() {
        String cipher = randomCipher();
        StringBuilder hexCipher = new StringBuilder(StringUtils.EMPTY);
        for (int i = 0; i < cipher.length(); i++) {
            int ch = cipher.charAt(i);
            String s4 = Integer.toHexString(ch);
            hexCipher.append(s4);
        }
        return hexCipher.toString();
    }

    /**
     * 生成密钥
     *
     * @return 密钥
     */
    public static String randomCipher() {
        char[] chars = new char[AES_KEY_LENGTH];
        SecureRandom rnd = new SecureRandom();
        chars[nextIndex(chars, rnd)] = nextUpperlLetter(rnd);
        chars[nextIndex(chars, rnd)] = nextLowerLetter(rnd);
        chars[nextIndex(chars, rnd)] = nextNumLetter(rnd);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 0) {
                chars[i] = nextChar(rnd);
            }
        }
        return new String(chars);
    }

    private static int nextIndex(char[] chars, SecureRandom rnd) {

        int index = rnd.nextInt(chars.length);
        while (chars[index] != 0) {
            index = rnd.nextInt(chars.length);
        }
        return index;
    }

    private static char nextUpperlLetter(Random rnd) {
        return (char) ('A' + rnd.nextInt(26));
    }

    private static char nextLowerLetter(Random rnd) {
        return (char) ('a' + rnd.nextInt(26));
    }

    private static char nextNumLetter(Random rnd) {
        return (char) ('0' + rnd.nextInt(10));
    }

    private static char nextChar(SecureRandom rnd) {
        switch (rnd.nextInt(3)) {
            case 0:
                return (char) ('a' + rnd.nextInt(26));
            case 1:
                return (char) ('A' + rnd.nextInt(26));
            default:
                return (char) ('0' + rnd.nextInt(10));
        }
    }
}
