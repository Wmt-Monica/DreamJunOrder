package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.User;
import com.dreamplume.sell.form.*;
import com.dreamplume.sell.service.UserService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2022/4/21 11:25
 * @Created by 翊
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    /**
     * 查找商家用户
     * @return
     */
    @GetMapping("/get/business")
    public ResultVO<Object> findBusiness() {
        User business = userService.findBusiness();
        return ResultVOUtil.success(JSONObject.toJSON(business));
    }

    /**
     * 用户登录
     * @param logonForm 登录信息表
     * @return 返回登录结果
     */
    @PostMapping("/logon")
    public ResultVO<Object> logon(@RequestBody @Valid LogonForm logonForm) {
        User user = userService.logon(logonForm);
        return ResultVOUtil.success(JSONObject.toJSON(user));
    }

    /**
     * 忘记密码
     * @param from 表单
     */
    @PostMapping("/forget/password")
    public ResultVO<Object> forgetPassword(@RequestBody @Valid ForgetPasswordForm from) {
        userService.forgetPassword(from);
        return ResultVOUtil.success();
    }

    /**
     * 忘记支付密码
     * @param form 表单
     */
    @PostMapping("/forget/pay/password")
    public ResultVO<Object> forgetPayPassword(@RequestBody @Valid ForgetPasswordForm form) {
        userService.forgetPayPassword(form);
        return ResultVOUtil.success();
    }

    /**
     * 更改用户登录密码
     * @param updateUserPasswordForm 表单
     */
    @PutMapping("/update/password")
    public ResultVO<Object> updatePassword(@RequestBody @Valid UpdateUserPasswordForm updateUserPasswordForm) {
        userService.updatePassword(updateUserPasswordForm);
        return ResultVOUtil.success();
    }

    /**
     * 用户注册
     * @param registerForm 注册信息表
     */
    @PostMapping("/register")
    public ResultVO<Object> register(@RequestBody @Valid RegisterForm registerForm) {
        log.info("registerForm = {}", registerForm);
        userService.register(registerForm);
        return ResultVOUtil.success();
    }

    /**
     * 用户注销
     * @param userId 用户 ID
     */
    @PutMapping("/logoff")
    public ResultVO<Object> logoff(@RequestParam("userId") String userId) {
        userService.logoff(userId);
        return ResultVOUtil.success();
    }

    /**
     * 用户退出登录
     * @param userId 用户 ID
     */
    @PutMapping("/logout")
    public ResultVO<Object> logout(@RequestParam("userId") String userId) {
        userService.logout(userId);
        return ResultVOUtil.success();
    }

    /**
     * 更新用户名
     * @param updateUserForm 表单
     */
    @PutMapping("/update/username")
    public ResultVO<Object> updateUserName(@RequestBody @Valid UpdateUserForm updateUserForm) {
        userService.updateUserName(updateUserForm);
        return ResultVOUtil.success();
    }

    /**
     * 更新用户头像
     * @param userId 用户 id
     * @param newPictureFile 新用户头像文件
     */
    @PostMapping("/update/head/portrait")
    public ResultVO<Object> updateUserHeadPortrait(@RequestParam("userId") String userId,
                                                   @RequestParam("newPictureFile") MultipartFile newPictureFile) {
        log.info("userId = {}", userId);
        log.info("newPictureFile = {}", newPictureFile);
        userService.updateUserHeadPortrait(userId, newPictureFile);
        return ResultVOUtil.success();
    }

    /**
     * 获取所有用户的邮箱
     * @return 邮箱集合
     */
    @GetMapping("/get/all/user/email")
    public ResultVO<Object> findAllUserEmail() {
        List<String> allUserEmail = userService.findAllUserEmail();
        return ResultVOUtil.success(JSONObject.toJSON(allUserEmail));
    }
}
