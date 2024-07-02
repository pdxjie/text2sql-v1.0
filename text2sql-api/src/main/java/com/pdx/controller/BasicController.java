package com.pdx.controller;

import com.pdx.model.vo.LoginVo;
import com.pdx.model.vo.RegisterVo;
import com.pdx.response.Result;
import com.pdx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/*
 * @Author 派同学
 * @Description 基础前置接口
 * @Date 2023/7/26
 **/
@Api(tags = "基础前置操作接口")
@RestController
@CrossOrigin
@RequestMapping("/basic")
public class BasicController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登录操作")
    public Result<?> login (@RequestBody  @Valid LoginVo loginVo) {
        return userService.login(loginVo);
    }

    @ApiOperation(value = "发送验证码")
    @GetMapping("/captcha/{email}")
    public Result<?> sendCaptcha (@PathVariable("email")String email) {
        return userService.sendCode(email);
    }

    @ApiOperation(value = "注册用户")
    @PostMapping("/register")
    public Result<?> register (@RequestBody @Valid RegisterVo registerVo) {
        return userService.registerUser(registerVo);
    }
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public Result<?> getUserInfo () {
        return userService.getUserInfo();
    }

    @ApiOperation(value = "退出登陆")
    @GetMapping("/logout/{id}")
    public Result<?> logout (@PathVariable("id") String id) {
        return userService.logout(id);
    }

    @ApiOperation(value = "批量上传文件")
    @PostMapping("/batch/upload")
    public Result<?> batchUpload (@RequestParam("file") MultipartFile file) {
        return userService.batchUpload(file);
    }
}
