package com.pdx.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/7
 * @Description: DO AnyThing...
 */
@Configuration
public class HikariCPConfig {

    // 省略其他配置
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/default_db");
        config.setUsername("default_user");
        config.setPassword("default_password");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
