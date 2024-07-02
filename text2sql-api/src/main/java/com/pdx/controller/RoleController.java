package com.pdx.controller;


import com.pdx.annotation.CheckRoles;
import com.pdx.model.constants.RoleType;
import com.pdx.model.entity.Role;
import com.pdx.model.vo.QueryRoleVo;
import com.pdx.model.vo.UpdateRoleVo;
import com.pdx.response.Result;
import com.pdx.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2023-07-24
 */
@Api(tags = "角色接口管理")
@RestController
@RequestMapping("/pdx/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/roles")
    @CheckRoles(value = RoleType.ADMIN)
    @ApiOperation(value = "角色列表")
    public Result<?> roles (@RequestBody QueryRoleVo vo) {
        return roleService.pages(vo);
    }

    @PostMapping("/insert")
    @CheckRoles(value = RoleType.ADMIN)
    @ApiOperation(value = "添加角色")
    public Result<?> insertRole (@RequestBody Role role) {
        return roleService.insertRole(role);
    }

    @GetMapping("/{id}")
    @CheckRoles(value = RoleType.ADMIN)
    @ApiOperation(value = "获取角色信息")
    public Result<?> roleInfo (@PathVariable("id") String id) {
        return roleService.getRoleInfo(id);
    }

    @PutMapping("/update")
    @CheckRoles(value = RoleType.ADMIN)
    @ApiOperation(value = "修改角色信息")
    public Result<?> updateRole (@RequestBody UpdateRoleVo vo) {
        return roleService.updateRole(vo);
    }

    @GetMapping("/status")
    @ApiOperation(value = "修改角色状态")
    @CheckRoles(value = RoleType.ADMIN)
    public Result<?> updateRoleStatus (@PathParam("roleId")String roleId) {
        return roleService.changeStatus(roleId);
    }


    @DeleteMapping("/{id}")
    @CheckRoles(value = RoleType.ADMIN)
    @ApiOperation(value = "删除角色信息")
    public Result<?> deleteById (@PathVariable("id") String id) {
        return Result.success(roleService.removeById(id));
    }

}

