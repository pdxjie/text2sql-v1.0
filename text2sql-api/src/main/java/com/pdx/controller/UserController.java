package com.pdx.controller;


import com.pdx.annotation.CheckRoles;
import com.pdx.model.constants.RoleType;
import com.pdx.model.entity.User;
import com.pdx.model.vo.ModifyUserVo;
import com.pdx.model.vo.UpdatePasswordVo;
import com.pdx.model.vo.UserVo;
import com.pdx.response.Result;
import com.pdx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2023-07-24
 */
@Api(tags = "用户接口管理")
@RestController
@RequestMapping("/pdx/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/search")
    @CheckRoles(RoleType.ADMIN)
    @ApiOperation(value = "用户条件检索列表")
    public Result<?> searchPages (@RequestBody UserVo userVo) {
        return userService.queryByCondition(userVo);
    }

    @PostMapping("/insert")
    @CheckRoles(RoleType.ADMIN)
    @ApiOperation(value = "添加用户")
    public Result<?> insertUser (@RequestBody User user) {
        return userService.insertUser(user);
    }

    @GetMapping("/{id}")
    @CheckRoles(RoleType.ADMIN)
    @ApiOperation(value = "获取用户信息")
    public Result<?> getOne (@PathVariable("id") String id) {
        return userService.getOneById(id);
    }

    @PutMapping("/update")
    @CheckRoles(RoleType.ADMIN)
    @ApiOperation(value = "更新用户基础信息")
    public Result<?> update (@RequestBody ModifyUserVo user) {
        return userService.updateInfo(user);
    }

    @ApiOperation(value = "修改用户头像")
    @PostMapping("/avatar/{id}")
    public Result<?> updateAvatar (@PathVariable("id") String id, @RequestBody MultipartFile file) {
        return userService.updateAvatar(id, file);
    }

    @ApiOperation(value = "修改用户密码")
    @PostMapping("/password")
    public Result<?> updatePassword (@RequestBody @Valid UpdatePasswordVo vo) {
        return userService.updatePassword(vo);
    }

    @ApiOperation(value = "删除用户信息")
    @CheckRoles(RoleType.ADMIN)
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteById (@PathVariable("id") String id) {
        return userService.removeUserById(id);
    }

    @ApiOperation(value = "封禁用户账号")
    @CheckRoles(RoleType.ADMIN)
    @GetMapping("/forbidden/{id}")
    public Result<?> forbiddenUser (@PathVariable("id")String id) {
        return userService.forbiddeUser(id);
    }

    @PutMapping("/set/role")
    @ApiOperation(value = "设置角色")
    @CheckRoles(value = RoleType.ADMIN)
    public Result<?> setRole (@PathParam("userId") String userId, @PathParam("roleId") String roleId) {
        return userService.setRole(userId, roleId);
    }

}

