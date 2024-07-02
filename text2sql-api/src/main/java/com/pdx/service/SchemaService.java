package com.pdx.service;

import com.pdx.model.dto.SchemaDetailResponse;
import com.pdx.model.entity.Schema;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.model.vo.AutoImportSchemaVo;
import com.pdx.model.vo.InsertSchemaVo;
import com.pdx.model.vo.MoveDatabaseVo;
import com.pdx.model.vo.SchemaDetailVo;
import com.pdx.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
public interface SchemaService extends IService<Schema> {

    /**
     * 获取 schema 详情
     * @param schemaDetailVo
     * @return
     */
    Result<?> schemaDetail(SchemaDetailVo schemaDetailVo);


    SchemaDetailResponse schemaDetail(String databaseId, String schemaId);

    /**
     * 新增 schema
     * @param insertSchemaVo 新增 schema
     * @return 操作结果
     */
    Result<?> insertSchema(InsertSchemaVo insertSchemaVo);

    /**
     * 删除 Schema
     * @param id 主键 ID
     * @return 操作结果
     */
    Result<?> deleteSchemaById(String id);

    /**
     * 自动导入表结构
     * @param importSchemaVo 导入表结构
     * @return 导入结果
     */
    Result<?> autoImport(AutoImportSchemaVo importSchemaVo);

    /**
     * 移动数据库
     *
     * @param moveDatabaseVo 移动数据库
     * @return 操作结果
     */
    Result<?> moveDatabase(MoveDatabaseVo moveDatabaseVo);
}
