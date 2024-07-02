package com.pdx.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/*
 * @Author 派同学
 * @Description AES128加密算法
 * @Date 2023/7/24
 **/
@Slf4j
public class Aes128Util {

    private static final String ALGORITHM = "AES";

    private static final String ALGORITHM_NAME = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param hexKey  密钥
     * @param content 明文
     * @return 加密内容
     */
    public static String encrypt(String hexKey, String content) {
        String encryptContent = StringUtils.EMPTY;
        SecretKeySpec keySpec = new SecretKeySpec(hexKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            encryptContent = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("AES128Utils.encrypt error {}", e.getMessage(), e);
        }
        return encryptContent;
    }

    /**
     * 解密
     *
     * @param hexKey         明文密钥
     * @param encryptContent 加密内容
     * @return 明文
     */
    public static String decrypt(String hexKey, String encryptContent) {
        String content = StringUtils.EMPTY;
        SecretKeySpec keySpec = new SecretKeySpec(hexKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptData = cipher.doFinal(Base64.getDecoder().decode(encryptContent));
            content = new String(decryptData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES128Utils.encrypt decrypt {}", e.getMessage(), e);
        }
        return content;
    }
}
