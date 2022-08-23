package com.dreamplume.sell.service;

import com.dreamplume.sell.entity.User;
import com.dreamplume.sell.form.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022/4/19 23:57
 * @Created by 翊
 */
public interface UserService {

    /**
     * 查找商家用户
     * @return
     */
    User findBusiness();

    /**
     * 用户登录
     * @param logonForm 登录信息表
     * @return 返回登录结果
     */
    User logon (LogonForm logonForm);

    /**
     * 忘记密码
     * @param from 表单
     */
    void forgetPassword(ForgetPasswordForm from);

    /**
     * 忘记支付密码
     * @param form 表单
     */
    void forgetPayPassword(ForgetPasswordForm form);

    /**
     * 更改用户登录密码
     * @param updateUserPasswordForm 表单
     */
    void updatePassword(UpdateUserPasswordForm updateUserPasswordForm);

    /**
     * 用户注册
     * @param registerForm 注册信息表
     */
    void register(RegisterForm registerForm);

    /**
     * 用户注销
     * @param userId 用户 ID
     */
    void logoff(String userId);

    /**
     * 用户退出登录
     * @param userId 用户 ID
     */
    void logout(String userId);

    /**
     * 更新用户名
     * @param updateUserForm 表单
     */
    void updateUserName(UpdateUserForm updateUserForm);

    /**
     * 更新用户头像
     * @param userId 用户 id
     * @param newPictureFile 新用户头像文件
     */
    void updateUserHeadPortrait(String userId, MultipartFile newPictureFile);

    /**
     * 获取所有用户的邮箱
     * @return 邮箱集合
     */
    List<String> findAllUserEmail();
}
