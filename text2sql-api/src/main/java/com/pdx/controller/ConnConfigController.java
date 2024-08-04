package com.pdx.controller;


import com.pdx.annotation.CheckRoles;
import com.pdx.model.constants.RoleType;
import com.pdx.model.enums.CheckType;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.Result;
import com.pdx.service.ConnConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2024-07-07
 */
@RestController
@RequestMapping("/pdx/conn")
public class ConnConfigController {

    @Autowired
    private ConnConfigService connConfigService;

    @PostMapping("/check")
    @CheckRoles(value = {RoleType.ADMIN, RoleType.USER}, checkType = CheckType.OR)
    @ApiOperation(value = "检查连接", notes = "检查连接")
    public Result<?> check(@RequestBody(required = false) ConnVo connVo) {
        return connConfigService.checkConn(connVo);
    }

    @GetMapping("/close")
    @CheckRoles(value = {RoleType.ADMIN, RoleType.USER}, checkType = CheckType.OR)
    @ApiOperation(value = "关闭连接", notes = "关闭连接")
    public Result<?> closeConn() {
        return connConfigService.closeConn();
    }

    @GetMapping("/databases")
    @CheckRoles(value = {RoleType.ADMIN, RoleType.USER}, checkType = CheckType.OR)
    @ApiOperation(value = "获取数据库列表", notes = "获取数据库列表")
    public Result<?> getDatabases() {
        return connConfigService.getDatabases();
    }

    @GetMapping("/{connId}")
    @CheckRoles(value = {RoleType.ADMIN, RoleType.USER}, checkType = CheckType.OR)
    @ApiOperation(value = "连接数据源", notes = "连接数据源")
    public Result<?> connDatasource(@PathVariable("connId") String connId) {
        return connConfigService.connDatasource(connId);
    }

    @GetMapping("/custom/group")
    @CheckRoles(value = {RoleType.ADMIN, RoleType.USER}, checkType = CheckType.OR)
    @ApiOperation(value = "获取自定义分组列表", notes = "获取自定义分组列表")
    public Result<?> getCustomGroupList() {
        return connConfigService.getCustomGroupList();
    }
}

