package com.pdx.mapper;

import com.pdx.model.dto.SchemaDetailResponse;
import com.pdx.model.entity.Database;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
public interface DatabaseMapper extends BaseMapper<Database> {

    /**
     * 根据数据库 ID 和数据库表 ID 集合查询数据库表结构
     *
     * @param databaseId 数据库 ID
     * @param schemaIds 数据库表 ID 集合
     * @return 数据库表结构
     */
    List<SchemaDetailResponse> selectSchemaInfo(@Param("databaseId") String databaseId, @Param("schemaIds") List<String> schemaIds);
}
