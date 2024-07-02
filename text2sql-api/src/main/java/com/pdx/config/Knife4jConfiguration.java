package com.pdx.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: 派大星
 * @Date: 2023/05/27 2023/5/27
 * @Description: Knife4j配置类
 */
@Configuration
@EnableKnife4j
public class Knife4jConfiguration {

    @Bean
    public Docket docket () {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                         .description("服务端开发模板架构")
                         .termsOfServiceUrl("http://localhost:8080/")
                         .contact(new Contact("服务端开发模板","http://localhost:8080/","pdx_jie@163.com"))
                         .version("v1.0")
                         .build())
                .groupName("v1.0 beta")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pdx.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

}
