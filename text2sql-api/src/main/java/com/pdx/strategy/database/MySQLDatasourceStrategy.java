package com.pdx.strategy.database;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pdx.config.DatabaseComponent;
import com.pdx.model.entity.ConnConfig;
import com.pdx.model.enums.TirggerType;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;
import com.pdx.service.ConnConfigService;
import com.pdx.utils.Aes128Util;
import com.pdx.utils.DBProvider;
import com.pdx.utils.SpringUtils;
import com.pdx.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.pdx.model.constants.BasicConstants.FILTER_DATA_BASES;


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

    // 默认密钥
    private static final String DEFAULT_KEY = "1234567890abcdef";

    // 数据库工具类
    private DBProvider dbProvider;

    public MySQLDatasourceStrategy() {
        databaseComponent = SpringUtils.getBean(DatabaseComponent.class);
        connConfigService = SpringUtils.getBean(ConnConfigService.class);
        dbProvider = SpringUtils.getBean(DBProvider.class);
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
        // 判断当前数据源连接是否存在
        ConnConfig connEntity = connConfigService.getOne(new QueryWrapper<ConnConfig>().eq("user_id", userId).eq("conn_host", connVo.getConnHost()).eq("conn_port", connVo.getConnPort()).eq("conn_user", connVo.getConnUser()));
        // 判断数据源是否为空
        if (Objects.isNull(dataSource)) {
            if (Objects.nonNull(connEntity)) {
                // 判断密码是否正确
                if (StringUtils.isNotEmpty(connEntity.getConnPass()) && !Aes128Util.decrypt(DEFAULT_KEY, connEntity.getConnPass()).equals(connVo.getConnPass())) {
                    return Result.fail(ResponseCode.CONN_PASS_ERROR);
                }
                // 更新数据源连接信息
                if (connVo.isSavePass() && StringUtils.isEmpty(connEntity.getConnPass())) {
                    // 保存密码
                    String encrypt = Aes128Util.encrypt(DEFAULT_KEY, connVo.getConnPass());
                    connEntity.setConnPass(encrypt);
                    connEntity.setSavePass(1);
                    connConfigService.updateById(connEntity);
                }
            }
        } else {
            connConfigService.closeConn();
        }
        // 创建数据源连接
        dataSource = databaseComponent.createDataSource(connVo);
        // 保存数据源连接
        databaseComponent.addDataSource(userId, dataSource);
        Connection connection = null;
        // 判断是否是有效数据源
        if (TirggerType.CHECK.toString().equalsIgnoreCase(connVo.getType())) {
            // 连接信息为空，则进行连接测试
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
        } else {
            // 连接信息不为空，且保存密码不为空，则进行连接测试
            try {
                connection = dataSource.getConnection();
                // 判断数据源是否存在
                if (Objects.isNull(connEntity)) {
                    // 保存数据源连接信息
                    ConnConfig config = new ConnConfig();
                    BeanUtils.copyProperties(connVo, config);
                    config.setId(UUID.randomUUID().toString());
                    config.setUserId(userId);
                    config.setCreateTime(new Date());
                    config.setUpdateTime(new Date());
                    // 判断是否需要保存密码
                    if (connVo.isSavePass()) {
                        String encrypt = Aes128Util.encrypt(DEFAULT_KEY, connVo.getConnPass());
                        config.setSavePass(1);
                        config.setConnPass(encrypt);
                    } else {
                        config.setSavePass(0);
                        config.setConnPass(null);
                    }
                    // 保存数据源连接信息
                    if (StringUtils.isEmpty(config.getConnNick())) {
                        config.setConnNick(StringUtil.joinString("default-", connVo.getConnPort()));
                    }
                    connConfigService.save(config);
                }
                // TODO 连接成功，返回数据库相关信息
                List<String> databases = dbProvider.getSchemas(connection);
                // 过滤不需要的数据库
                databases = databases.stream().filter(database -> !FILTER_DATA_BASES.contains(database)).collect(Collectors.toList());
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("databases", databases);
                return Result.success(resultMap);
            } catch (Exception e) {
                // 连接失败
                return Result.fail(ResponseCode.CONN_FAIL);
            }
        }
        return Result.fail(ResponseCode.FAIL);
    }

    /**
     * 获取数据源表
     *
     * @param databaseName 数据库名称
     * @param userId 用户 ID
     * @return 结果
     */
    @Override
    public Result<?> fetchTables(String databaseName, String userId) {
        // 获取数据源连接信息
        DataSource dataSource = databaseComponent.getDataSource(userId);
        // 判断当前数据源连接是否存在
        if (Objects.isNull(dataSource)) {
            return Result.fail(ResponseCode.CONN_NOT_EXIST);
        }
        // 创建数据源连接
        Connection connection = null;
        try {
            // 连接数据源
            connection = dataSource.getConnection();
            // 获取数据库表信息
            List<String> tables = dbProvider.getTables(connection, databaseName);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("databases", tables);
            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.fail(ResponseCode.CONN_NOT_EXIST);
        }
    }
}
