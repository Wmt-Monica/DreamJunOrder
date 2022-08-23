package com.dreamplume.sell.controller;

import com.dreamplume.sell.form.InitWalletPasswordFrom;
import com.dreamplume.sell.form.UpdateWalletBalanceFrom;
import com.dreamplume.sell.form.UpdateWalletPasswordForm;
import com.dreamplume.sell.service.WalletService;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    // 查找指定 userID 的钱包余额
    @GetMapping("/find")
    public ResultVO<Object> find(@RequestParam("userId") String userId) {
        return walletService.getBalanceByUserId(userId);
    }

    // 判断用户是否设置了密码
    @GetMapping("/password/judge")
    public ResultVO<Object> judgeSetPassword(@RequestParam("userId") String userId) {
        return  walletService.judgeSetPassword(userId);
    }

    // 设置用户钱包密码
    @PutMapping("/password/set")
    public ResultVO<Object> setPayPassword(@RequestBody InitWalletPasswordFrom from) {
        return walletService.setPayPassword(from);
    }

    // 更新用户钱包支付密码
    @PutMapping("/update/pay/password")
    public ResultVO<Object> updatePayPassword(@RequestBody UpdateWalletPasswordForm from) {
        log.info("【form】");
        log.info("【更新用户支付密码】form={}", from);
        return walletService.updatePayPasswordByForm(from);
    }

    // 用户充值
    @PutMapping("/recharge")
    public ResultVO<Object> recharge(@RequestBody UpdateWalletBalanceFrom from) {
        return walletService.recharge(from);
    }

    // 用户钱包提现
    @PutMapping("/withdrawal")
    public ResultVO<Object> withdrawal(@RequestBody UpdateWalletBalanceFrom from) {
        return walletService.withdrawal(from);
    }

}
