package com.pdx.strategy.database;

import com.pdx.model.vo.ConnVo;
import com.pdx.response.Result;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/7
 * @Description: Oracle 数据源连接策略
 */
public class OracleDatasourceStrategy implements DataSourceCheckStrategy {

    /**
     * 获取数据源连接策略
     * @param connVo 连接信息
     * @param userId 用户 ID
     * @return 结果
     */
    @Override
    public Result<?> checkDatasource(ConnVo connVo, String userId) {
        return null;
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
        return null;
    }
}
