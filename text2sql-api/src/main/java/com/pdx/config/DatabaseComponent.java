package com.pdx.config;

import com.pdx.model.enums.SQLType;
import com.pdx.model.vo.ConnVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/7
 * @Description: DO AnyThing...
 */
@Component
public class DatabaseComponent {

    private final ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<>();

    public void addDataSource(String userId, DataSource dataSource) {
        dataSources.put(userId, dataSource);
    }

    public DataSource getDataSource(String userId) {
        return dataSources.get(userId);
    }

    public DataSource createDataSource(ConnVo connVo) {
        String url = String.format("jdbc:%s://%s:%s/%s",
                    SQLType.getType(connVo.getConnSource()).toLowerCase(),
                    connVo.getConnHost(),
                    connVo.getConnPort(),
                    connVo.getConnUser()
                );
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(connVo.getConnUser());
        config.setPassword(connVo.getConnPass());
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
