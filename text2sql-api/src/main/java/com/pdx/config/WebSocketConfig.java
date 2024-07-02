package com.pdx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/*
 * @Author 派同学
 * @Description Websocket配置类
 * @Date 2023/8/12
 **/
@Configuration
public class WebSocketConfig {

    /**
     * 注入一个ServerEndpointExporter 该Bean回自动注册使用@ServerEndpoint注解申明的websocket
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter () {
        return new ServerEndpointExporter();
    }
}
