package com.pdx.service;

import com.pdx.model.entity.Database;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.model.vo.DataBaseSearchVo;
import com.pdx.model.vo.DatabaseInsertVo;
import com.pdx.model.vo.GenerateSQLStreamVo;
import com.pdx.model.vo.RemoveDatabaseVo;
import com.pdx.response.Result;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
public interface DatabaseService extends IService<Database> {

    /**
     * 根据条件查询数据库列表
     * @param searchVo 条件
     * @return 操作结果
     */
    Result<?> databasePages(DataBaseSearchVo searchVo);

    /**
     * 获取数据库树
     * @return 操作结果
     */
    Result<?> databaseTree();

    /**
     * 新增数据库
     *
     * @param insertVo 数据库信息
     * @return 操作结果
     */
    Result<?> insertDatabase(DatabaseInsertVo insertVo);

    /**
     * 删除数据库
     * @param removeDatabaseVo 数据库 id
     * @return 操作结果
     */
    Result<?> removeDatabase(RemoveDatabaseVo removeDatabaseVo);

    /**
     * 分析生成 SQL
     *
     * @param streamVo 数据库 id
     * @return 操作结果
     */
    SseEmitter generateSQLStream(GenerateSQLStreamVo streamVo);
}
