package com.pdx.utils;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.google.common.collect.Lists;
import com.pdx.model.dto.DatabaseTreeDto;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: IT 派同学
 * @Date: 2024/7/8
 * @Description: DO AnyThing...
 */
@Component
public class DBProvider {

    public static Map<String,Object> customMap;

    // 初始化自定义类型映射
    static {
        customMap = new HashMap<>();
        customMap.put("VARCHAR","String");
        customMap.put("BIGINT","Long");
        customMap.put("INTEGER","Integer");
        customMap.put("INT","Integer");
        customMap.put("DATE","java.util.Date");
        customMap.put("DATETIME","java.util.Date");
        customMap.put("TIMESTAMP","java.util.Date");
        customMap.put("DOUBLE","Double");
        customMap.put("TEXT","String");
        customMap.put("VARCHAR2","String");
        customMap.put("NVARCHAR2","String");
        customMap.put("CHAR","String");
        customMap.put("MEDIUMTEXT","String");
        customMap.put("TINYINT","Integer");
        customMap.put("LONGTEXT","String");
        customMap.put("BIT","Integer");
        customMap.put("MEDIUMINT","Integer");
        customMap.put("SMALLINT","Integer");
        customMap.put("ENUM","String");
        customMap.put("DECIMAL","Integer");
        customMap.put("tableRemovePrefixes","");
    }

    /**
     * 获取数据库列表
     */
    public List<String> getSchemas(Connection connection) throws Exception {
        //获取元数据
        DatabaseMetaData metaData = connection.getMetaData();
        //获取所有数据库列表
        ResultSet rs = metaData.getCatalogs();
        // 存放数据库列表
        List<String> list = new ArrayList<>();
        // 遍历数据库列表
        while (rs.next()) {
            list.add(rs.getString(1));
        }
        rs.close();
        return list;
    }

    /**
     * 获取数据库表列表
     *
     * @param connection 数据库连接
     * @param databaseName 数据库名称
     * @return 数据库表列表
     */
    public List<String> getTables(Connection connection, String databaseName) {
        // TODO 获取数据库获取该数据库下的所有表
        return Lists.newArrayList();
    }
}
