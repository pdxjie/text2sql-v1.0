package com.pdx.controller;


import com.pdx.annotation.CheckRoles;
import com.pdx.model.constants.RoleType;
import com.pdx.model.enums.CheckType;
import com.pdx.model.vo.DataBaseSearchVo;
import com.pdx.model.vo.DatabaseInsertVo;
import com.pdx.model.vo.GenerateSQLStreamVo;
import com.pdx.model.vo.RemoveDatabaseVo;
import com.pdx.response.Result;
import com.pdx.service.DatabaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
@RestController
@RequestMapping("database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/pages")
    @CheckRoles(RoleType.ADMIN)
    @ApiOperation(value = "分页查询数据库列表")
    public Result<?> databasePages(@RequestBody DataBaseSearchVo searchVo) {
        return databaseService.databasePages(searchVo);
    }

    @PostMapping("/insert")
    @ApiOperation(value = "新增数据库")
    @CheckRoles(value = {RoleType.ADMIN, RoleType.USER}, checkType = CheckType.OR)
    public Result<?> insertDatabase(@RequestBody DatabaseInsertVo insertVo) {
        return databaseService.insertDatabase(insertVo);
    }

    @ApiOperation(value = "数据库树形节点")
    @GetMapping("/tree")
    public Result<?> databaseTree() {
        return databaseService.databaseTree();
    }

    @ApiOperation(value = "删除数据库")
    @PostMapping("/delete")
    public Result<?> removeDatabase(@RequestBody RemoveDatabaseVo removeDatabaseVo) {
        return databaseService.removeDatabase(removeDatabaseVo);
    }

    @ApiOperation(value = "分析生成 SQL")
    @PostMapping(value = "/generateSQLStream", produces = "text/event-stream")
    public SseEmitter generateSQLStream(@RequestBody GenerateSQLStreamVo streamVo) {
        return databaseService.generateSQLStream(streamVo);
    }
}

