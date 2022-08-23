package com.dreamplume.sell.service;

import com.dreamplume.sell.entity.Wallet;
import com.dreamplume.sell.form.InitWalletPasswordFrom;
import com.dreamplume.sell.form.UpdateWalletBalanceFrom;
import com.dreamplume.sell.form.UpdateWalletPasswordForm;

import java.math.BigDecimal;

/**
 * @Classname WalletService
 * @Description TODO
 * @Date 2022/4/20 9:43
 * @Created by 翊
 */
public interface WalletService {

    /**
     * 创建用户钱包
     * @param userId 用户 ID
     */
    void create(String userId);

    /**
     * 查看用户余额
     * @param userId 用户 id
     * @return 返回用于余额
     */
    BigDecimal getBalanceByUserId(String userId);

    /**
     * 判断用户是否设置了支付密码
     * @param userId 用户 ID
     * @return 返回是否设置了密码
     */
    Boolean judgeSetPassword(String userId);

    /**
     * 设置支付密码
     * @param from 初始化用户支付密码表单
     */
    void setPayPassword(InitWalletPasswordFrom from);

    /**
     * 更新用户支付密码
     * @param updateWalletPasswordForm 更新用户支付密码表对象
     */
    void updatePayPassword(UpdateWalletPasswordForm updateWalletPasswordForm);

    /**
     * 用户钱包充值
     * @param from 更新用户钱包表单
     * @return 充值结果
     */
    BigDecimal recharge(UpdateWalletBalanceFrom from);

    /**
     * 用户钱包提现
     * @param from 更新用户钱包表单
     * @return 提现结果
     */
    BigDecimal withdrawal(UpdateWalletBalanceFrom from);

    /**
     * 根据用户 ID 返回 Wallet 钱包对象
     * @param userId 用户 Id
     * @return Wallet 对象
     */
    Wallet getWalletByUserId(String userId);

    /**
     * 支付金额
     * @param backAmount 支付金额
     */
    void pay(BigDecimal backAmount, String userId, String password);

    /**
     * 返还支付金额
     * @param backAmount 返回金额数目
     */
    void back(BigDecimal backAmount, String userId);

    void businessBack(BigDecimal backAmount, String businessId);

    /**
     * 更新密码
     * @param userId 用户 ID
     * @param newPassword 新密码
     */
    void updatePayPassword(String userId, String newPassword);

    /**
     * 更新密码
     * @param user 用户
     * @param newPassword 新密码
     */
//    void updatePayPassword(User user, String newPassword);
}
