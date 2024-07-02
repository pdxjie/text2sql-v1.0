package com.pdx.controller;


import com.pdx.model.vo.AutoImportSchemaVo;
import com.pdx.model.vo.InsertSchemaVo;
import com.pdx.model.vo.MoveDatabaseVo;
import com.pdx.model.vo.SchemaDetailVo;
import com.pdx.response.Result;
import com.pdx.service.SchemaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
@RestController
@RequestMapping("/pdx/schema")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @ApiOperation(value = "获取 Schema 结构")
    @PostMapping("/detail")
    public Result<?> schemaDetail(@RequestBody SchemaDetailVo schemaDetailVo) {
        return schemaService.schemaDetail(schemaDetailVo);
    }

    @ApiOperation(value = "新增数据库表以及字段")
    @PostMapping("/insert")
    public Result<?> insertSchema(@RequestBody InsertSchemaVo insertSchemaVo) {
        return schemaService.insertSchema(insertSchemaVo);
    }

    @ApiOperation(value = "删除 Schema")
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteSchema(@PathVariable("id") String id) {
        return schemaService.deleteSchemaById(id);
    }

    @ApiOperation(value = "智能导入表结构")
    @PostMapping("/auto/import")
    public Result<?> autoImport(@RequestBody AutoImportSchemaVo importSchemaVo) {
        return schemaService.autoImport(importSchemaVo);
    }

    @ApiOperation(value = "移动数据库")
    @PostMapping("/move")
    public Result<?> moveDatabase(@RequestBody MoveDatabaseVo moveDatabaseVo) {
        return schemaService.moveDatabase(moveDatabaseVo);
    }
}

