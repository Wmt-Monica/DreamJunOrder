package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.form.InitWalletPasswordFrom;
import com.dreamplume.sell.form.UpdateWalletBalanceFrom;
import com.dreamplume.sell.form.UpdateWalletPasswordForm;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @Classname WalletService
 * @Description TODO
 * @Date 2022/4/20 9:43
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-wallet", configuration = MultipartSupportConfig.class)
public interface WalletService {

    /**
     * 创建用户钱包
     * @param userId 用户 ID
     */
    @PutMapping("/wallet/create")
    ResultVO<Object> create(@RequestParam("userId") String userId);

    /**
     * 查看用户余额
     * @param userId 用户 id
     * @return 返回用于余额
     */
    @GetMapping("/wallet/get/balance/by/userId")
    ResultVO<Object> getBalanceByUserId(@RequestParam("userId") String userId);

    /**
     * 判断用户是否设置了支付密码
     * @param userId 用户 ID
     * @return 返回是否设置了密码
     */
    @GetMapping("/wallet/judge/set/password")
    ResultVO<Object> judgeSetPassword(@RequestParam("userId") String userId);

    /**
     * 设置支付密码
     * @param from 初始化用户支付密码表单
     */
    @PostMapping("/wallet/set/password")
    ResultVO<Object> setPayPassword(@RequestBody InitWalletPasswordFrom from);

    /**
     * 更新用户支付密码
     * @param updateWalletPasswordForm 更新用户支付密码表对象
     */
    @PutMapping("/wallet/update/pay/password/by/form")
    ResultVO<Object> updatePayPasswordByForm(@RequestBody UpdateWalletPasswordForm updateWalletPasswordForm);

    /**
     * 用户钱包充值
     * @param from 更新用户钱包表单
     * @return 充值结果
     */
    @PutMapping("/wallet/recharge")
    ResultVO<Object> recharge(@RequestBody UpdateWalletBalanceFrom from);

    /**
     * 用户钱包提现
     * @param from 更新用户钱包表单
     * @return 提现结果
     */
    @PutMapping("/wallet/withdrawal")
    ResultVO<Object> withdrawal(@RequestBody UpdateWalletBalanceFrom from);

    /**
     * 根据用户 ID 返回 Wallet 钱包对象
     * @param userId 用户 Id
     * @return Wallet 对象
     */
    @GetMapping("/wallet/get/by/userId")
    ResultVO<Object> getWalletByUserId(@RequestParam("userId") String userId);

    /**
     * 支付金额
     * @param backAmount 支付金额
     */
    @PutMapping("/wallet/pay")
    ResultVO<Object> pay(@RequestParam("backAmount") BigDecimal backAmount,
                         @RequestParam("userId") String userId);

    /**
     * 返还支付金额
     * @param backAmount 返回金额数目
     */
    @PutMapping("/wallet/back")
    ResultVO<Object> back(@RequestParam("backAmount") BigDecimal backAmount,
                          @RequestParam("userId") String userId);

    /**
     * 更新密码
     * @param userId 用户 ID
     * @param newPassword 新密码
     */
    @PutMapping("/wallet/update/pay/password/by/userId")
    ResultVO<Object> updatePayPasswordByUserId(@RequestParam("userId") String userId,
                                               @RequestParam("newPassword") String newPassword);

}
