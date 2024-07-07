package com.pdx.strategy.database;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pdx.config.DatabaseComponent;
import com.pdx.model.entity.ConnConfig;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;
import com.pdx.service.ConnConfigService;
import com.pdx.utils.SpringUtils;
import org.springframework.beans.BeanUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/7
 * @Description: MySQL 数据源连接策略
 */
public class MySQLDatasourceStrategy implements DataSourceCheckStrategy {

    // 数据库组件
    private DatabaseComponent databaseComponent;

    // 连接配置服务
    private ConnConfigService connConfigService;

    public MySQLDatasourceStrategy() {
        databaseComponent = SpringUtils.getBean(DatabaseComponent.class);
        connConfigService = SpringUtils.getBean(ConnConfigService.class);
    }

    /**
     * 获取数据源连接策略
     * @param connVo 连接信息
     * @param userId 用户 ID
     * @return 结果
     */
    @Override
    public Result<?> checkDatasource(ConnVo connVo, String userId) {
        // 获取数据源连接信息
        DataSource dataSource = databaseComponent.getDataSource(userId);
        // 判断数据源是否为空
        if (Objects.isNull(dataSource)) {
            // 创建数据源连接
            dataSource = databaseComponent.createDataSource(connVo);
            // 保存数据源连接
            databaseComponent.addDataSource(userId, dataSource);
        }
        Connection connection = null;
        // 判断是否是有效数据源
        if (!connVo.isSavePass()) {
            try {
                connection = dataSource.getConnection();
                // 判断数据源是否有效
                if (connection.isValid(10)) {
                    // 关闭连接
                    connection.close();
                    // 返回成功结果
                    return Result.success();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        // 连接信息不为空，且保存密码不为空，则进行连接测试
        try {
            connection = dataSource.getConnection();
            // 判断当前数据源连接是否存在
            int count = connConfigService.count(new QueryWrapper<ConnConfig>().eq("user_id", userId).eq("conn_host", connVo.getConnHost()).eq("conn_port", connVo.getConnPort()).eq("conn_user", connVo.getConnUser()));
            // 判断数据源是否存在
            if (count == 0) {
                // 保存数据源连接信息
                ConnConfig config = new ConnConfig();
                BeanUtils.copyProperties(connVo, config);
                config.setId(UUID.randomUUID().toString());
                config.setUserId(userId);
                config.setCreateTime(new Date());
                config.setUpdateTime(new Date());
                connConfigService.save(config);
            }
            // TODO 连接成功，返回数据库相关信息
        } catch (Exception e) {
            // 连接失败
            return Result.fail(ResponseCode.CONN_FAIL);
        }
        return Result.fail(ResponseCode.FAIL);
    }
}
