package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.form.*;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022/4/19 23:57
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-user", configuration = MultipartSupportConfig.class)
public interface UserService {

    /**
     * 查找商家用户
     * @return
     */
    @GetMapping("/user/get/business")
    ResultVO<Object> findBusiness();

    /**
     * 用户登录
     * @param logonForm 登录信息表
     * @return 返回登录结果
     */
    @PostMapping("/user/logon")
    ResultVO<Object> logon (@RequestBody @Valid LogonForm logonForm);

    /**
     * 忘记密码
     * @param from 表单
     */
    @PostMapping("/user/forget/password")
    ResultVO<Object> forgetPassword(@RequestBody @Valid ForgetPasswordForm from);

    /**
     * 忘记支付密码
     * @param form 表单
     */
    @PostMapping("/user/forget/pay/password")
    ResultVO<Object> forgetPayPassword(@RequestBody @Valid ForgetPasswordForm form);

    /**
     * 更改用户登录密码
     * @param updateUserPasswordForm 表单
     */
    @PutMapping("/user/update/password")
    ResultVO<Object> updatePassword(@RequestBody @Valid UpdateUserPasswordForm updateUserPasswordForm);

    /**
     * 用户注册
     * @param registerForm 注册信息表
     */
    @PostMapping("/user/register")
    ResultVO<Object> register(@RequestBody @Valid RegisterForm registerForm);

    /**
     * 用户注销
     * @param userId 用户 ID
     */
    @PutMapping("/user/logoff")
    ResultVO<Object> logoff(@RequestParam("userId") String userId);

    /**
     * 用户退出登录
     * @param userId 用户 ID
     */
    @PutMapping("/user/logout")
    ResultVO<Object> logout(@RequestParam("userId") String userId);

    /**
     * 更新用户名
     * @param updateUserForm 表单
     */
    @PutMapping("/user/update/username")
    ResultVO<Object> updateUserName(@RequestBody @Valid UpdateUserForm updateUserForm);

    /**
     * 更新用户头像
     * @param userId 用户 id
     * @param newPictureFile 新用户头像文件
     */
    @PostMapping("/user/update/head/portrait")
    ResultVO<Object> updateUserHeadPortrait(@RequestParam("userId") String userId,
                                            @RequestParam("newPictureFile") MultipartFile newPictureFile);

    /**
     * 获取所有用户的邮箱
     * @return 邮箱集合
     */
    @GetMapping("/user/get/all/user/email")
    ResultVO<Object> findAllUserEmail();
}
