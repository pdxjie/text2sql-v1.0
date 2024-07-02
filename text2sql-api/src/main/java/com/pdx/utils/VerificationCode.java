package com.pdx.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Random;

/**
 * @Author: 派大星
 * @Date: 2023/05/27 2023/5/27
 * @Description: 生成验证码Code
 */
@Component
@Slf4j
public class VerificationCode {

    /**
     * 生成验证码长度
     */
    private static final int codeLength = 4;

    private final String[] metaCode={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 生成注册的验证码
     * @return
     */
    public String VerificationCode(Integer codeLength) {
        if (codeLength == null) {
            codeLength = 4;
        }
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();
        while (verificationCode.length() < codeLength) {
            int i = random.nextInt(metaCode.length);
            verificationCode.append(metaCode[i]);
        }
        return verificationCode.toString();
    }


    /**
     * 随机生成中文初始化昵称
     * @return 昵称
     */
    public String generateChineseName() {
        StringBuilder chineseName = new StringBuilder();
        // Unicode范围：4e00-9fa5 是中文字符的范围
        int unicodeStart = 0x4e00;
        int unicodeEnd = 0x9fa5;
        Random random = new Random();
        //参数控制生成名字的长度（2个字/3个字）
        for (int i = 0; i < random.nextInt(2) + 2; i++) {
            // 生成随机中文字符
            char randomChar = (char) (unicodeStart + random.nextInt(unicodeEnd - unicodeStart + 1));
            chineseName.append(randomChar);
        }
        return chineseName.toString();
    }

    /**
     * 构建邮件模板信息
     *
     * @param one
     * @param two
     * @param three
     * @param four
     * @return
     */
    public String buildContent(String one, String two, String three, String four) {
        //获取文件路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/register.html");
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            fileReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.error(String.format("发送邮件读取模板失败，错误堆栈信息：%s", e.getMessage()));
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //使用动态参数替换html模板中的占位符参数
        String htmlText = MessageFormat.format(buffer.toString(), one, two, three, four);
        return htmlText;
    }
}
