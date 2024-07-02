package com.pdx;

import com.pdx.model.constants.BasicConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * @Author 派同学
 * @Description  主启动类
 * @Date 2023/7/24
 **/
@SpringBootApplication
// 开启定时任务
@EnableScheduling
@ComponentScan(basePackages = "com.pdx.*")
@MapperScan(basePackages = BasicConstants.BASIC_SCAN_PATH + ".mapper")
public class Text2SqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(Text2SqlApplication.class, args);
    }
}
