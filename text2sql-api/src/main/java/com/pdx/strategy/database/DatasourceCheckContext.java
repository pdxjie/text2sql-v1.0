package com.pdx.strategy.database;

import com.pdx.model.enums.SQLType;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/7
 * @Description: DO AnyThing...
 */
public class DatasourceCheckContext {

    /**
     * 策略集合
     */
    private final Map<String, DataSourceCheckStrategy> strategies = new HashMap<>();

    /**
     * 构造方法
     */
    public DatasourceCheckContext() {
        strategies.put(SQLType.MYSQL.getType(), new MySQLDatasourceStrategy());
        strategies.put(SQLType.POSTGRESQL.getType(), new PostgreSQLDatasourceStrategy());
        strategies.put(SQLType.ORACLE.getType(), new OracleDatasourceStrategy());
        strategies.put(SQLType.SQLSERVER.getType(), new SqlServerDatasourceStrategy());
    }

    /**
     * 获取数据源连接
     *
     * @param connVo 连接参数
     * @param userId 用户 ID
     * @return 结果
     */
    public Result<?> checkDatasource(ConnVo connVo, String userId) {
        // 获取数据源类型值
        Integer connSource = connVo.getConnSource();
        // 获取数据源类型
        String type = SQLType.getType(connSource);
        DataSourceCheckStrategy strategy = strategies.get(type);
        // 判断是否生成策略为空
        if (Objects.isNull(strategy)) {
            return Result.fail(ResponseCode.CONN_TYPE_NOT_SUPPORT);
        }
        return strategy.checkDatasource(connVo, userId);
    }
}
