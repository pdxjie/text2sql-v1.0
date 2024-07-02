package com.pdx.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * @Author 派同学
 * @Description 跨域过滤器配置
 * @Date 2023/7/24
 **/
@WebFilter(filterName = "corsConfig")
public class CorsConfig implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        addCorsResponseHeader(response);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 添加跨域响应头
     * @param response
     */
    private void addCorsResponseHeader (HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, PATCH, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
    }
}
