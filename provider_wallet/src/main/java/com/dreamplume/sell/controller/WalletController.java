package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.Wallet;
import com.dreamplume.sell.form.InitWalletPasswordFrom;
import com.dreamplume.sell.form.UpdateWalletBalanceFrom;
import com.dreamplume.sell.form.UpdateWalletPasswordForm;
import com.dreamplume.sell.service.WalletService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Classname WalletController
 * @Description TODO
 * @Date 2022/4/21 20:18
 * @Created by 翊
 */
@RestController
@RequestMapping("/wallet")
@Slf4j
public class WalletController {

    @Resource
    WalletService walletService;

    /**
     * 创建用户钱包
     * @param userId 用户 ID
     */
    @PutMapping("/create")
    public ResultVO<Object> create(@RequestParam("userId") String userId) {
        walletService.create(userId);
        return ResultVOUtil.success();
    }

    /**
     * 查看用户余额
     * @param userId 用户 id
     * @return 返回用于余额
     */
    @GetMapping("/get/balance/by/userId")
    public ResultVO<Object> getBalanceByUserId(@RequestParam("userId") String userId) {
        BigDecimal balance = walletService.getBalanceByUserId(userId);
        return ResultVOUtil.success(JSONObject.toJSON(balance));
    }

    /**
     * 判断用户是否设置了支付密码
     * @param userId 用户 ID
     * @return 返回是否设置了密码
     */
    @GetMapping("/judge/set/password")
    public ResultVO<Object> judgeSetPassword(@RequestParam("userId") String userId) {
        Boolean setPassword = walletService.judgeSetPassword(userId);
        return ResultVOUtil.success(JSONObject.toJSON(setPassword));
    }

    /**
     * 设置支付密码
     * @param from 初始化用户支付密码表单
     */
    @PostMapping("/set/password")
    public ResultVO<Object> setPayPassword(@RequestBody InitWalletPasswordFrom from) {
        walletService.setPayPassword(from);
        return ResultVOUtil.success();
    }

    /**
     * 更新用户支付密码
     * @param updateWalletPasswordForm 更新用户支付密码表对象
     */
    @PutMapping("/update/pay/password/by/form")
    public ResultVO<Object> updatePayPasswordByForm(@RequestBody UpdateWalletPasswordForm updateWalletPasswordForm) {
        walletService.updatePayPassword(updateWalletPasswordForm);
        return ResultVOUtil.success();
    }

    /**
     * 用户钱包充值
     * @param from 更新用户钱包表单
     * @return 充值结果
     */
    @PutMapping("/recharge")
    public ResultVO<Object> recharge(@RequestBody UpdateWalletBalanceFrom from) {
        BigDecimal rechargeBigDecimal = walletService.recharge(from);
        return ResultVOUtil.success(JSONObject.toJSON(rechargeBigDecimal));
    }

    /**
     * 用户钱包提现
     * @param from 更新用户钱包表单
     * @return 提现结果
     */
    @PutMapping("/withdrawal")
    public ResultVO<Object> withdrawal(@RequestBody UpdateWalletBalanceFrom from) {
        BigDecimal withdrawalBigDecimal = walletService.withdrawal(from);
        return ResultVOUtil.success(JSONObject.toJSON(withdrawalBigDecimal));
    }

    /**
     * 根据用户 ID 返回 Wallet 钱包对象
     * @param userId 用户 Id
     * @return Wallet 对象
     */
    @GetMapping("/get/by/userId")
    public ResultVO<Object> getWalletByUserId(@RequestParam("userId") String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResultVOUtil.success(JSONObject.toJSON(wallet));
    }

    /**
     * 支付金额
     * @param backAmount 支付金额
     */
    @PutMapping("/pay")
    public ResultVO<Object> pay(@RequestParam("backAmount") BigDecimal backAmount,
                                @RequestParam("userId") String userId,
                                @RequestParam("password") String password) {
        walletService.pay(backAmount, userId, password);
        return ResultVOUtil.success();
    }

    /**
     * 退还支付金额
     * @param backAmount 返回金额数目
     */
    @PutMapping("/back")
    public ResultVO<Object> back(@RequestParam("backAmount") BigDecimal backAmount,
                                 @RequestParam("userId") String userId) {
        walletService.back(backAmount, userId);
        return ResultVOUtil.success();
    }

    /**
     * 返还支付金额
     * @param backAmount 返回金额数目
     */
    @PutMapping("/business/back")
    public ResultVO<Object> businessBack(@RequestParam("backAmount") BigDecimal backAmount,
                                         @RequestParam("businessId") String businessId) {
        walletService.businessBack(backAmount, businessId);
        return ResultVOUtil.success();
    }

    /**
     * 更新密码
     * @param userId 用户 ID
     * @param newPassword 新密码
     */
    @PutMapping("/update/pay/password/by/userId")
    public ResultVO<Object> updatePayPasswordByUserId(@RequestParam("userId") String userId,
                                                      @RequestParam("newPassword") String newPassword) {
        walletService.updatePayPassword(userId, newPassword);
        return ResultVOUtil.success();
    }

}
