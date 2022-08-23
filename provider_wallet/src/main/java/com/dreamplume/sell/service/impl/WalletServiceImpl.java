package com.dreamplume.sell.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dreamplume.sell.entity.Wallet;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.form.InitWalletPasswordFrom;
import com.dreamplume.sell.form.UpdateWalletBalanceFrom;
import com.dreamplume.sell.form.UpdateWalletPasswordForm;
import com.dreamplume.sell.repository.WalletRepository;
import com.dreamplume.sell.service.EmailServer;
import com.dreamplume.sell.service.WalletService;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Classname WalletServiceImpl
 * @Description TODO
 * @Date 2022/4/21 20:20
 * @Created by 翊
 */
@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Resource
    WalletRepository walletRepository;

    @Resource
    EmailServer emailServer;

    /**
     * 创建用户钱包
     * @param userId 用户 ID
     */
    @Override
    public void create(String userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(new BigDecimal("0.0"));
        int insert = walletRepository.insert(wallet);
        if (insert <= 0) {
            log.error(SellErrorCode.WALLET_CREATE_FAIL.getMessage());
        }
    }

    /**
     * 根据用户 id 返回钱包余额
     * @param userId 用户 id
     * @return 余额
     */
    @Override
    public BigDecimal getBalanceByUserId(String userId) {
        return getWalletByUserId(userId).getBalance();
    }

    /**
     * 判断用户是否设置了支付密码
     * @param userId 用户 ID
     * @return 判断结果
     */
    @Override
    public Boolean judgeSetPassword(String userId) {
        Wallet wallet = getWalletByUserId(userId);
        return !(ObjectUtils.isEmpty(wallet.getPassword()) || wallet.getPassword().equals(""));
    }

    /**
     * 初始化用户的支付密码
     * @param from 初始化用户支付密码表单
     */
    @Override
    public void setPayPassword(InitWalletPasswordFrom from) {
        // 验证邮箱验证码
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(from.getEmail(), from.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }
        // 初始化用户钱包支付密码
        Wallet wallet = getWalletByUserId(from.getUserId());
        wallet.setPassword(DigestUtils.md5DigestAsHex(from.getPassword().getBytes()));
        if (update(wallet, from.getUserId()) <= 0) {
            log.error("【钱包设置密码】密码设置失败");
            throw new SellException(SellErrorCode.WALLET_SET_PASSWORD_FAIL);
        }
    }

    /**
     * 更新用户支付密码
     * @param updateWalletPasswordForm 更新用户支付密码表对象
     */
    @Override
    public void updatePayPassword(UpdateWalletPasswordForm updateWalletPasswordForm) {
        // 验证验证码
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(updateWalletPasswordForm.getEmail(), updateWalletPasswordForm.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }

        Wallet wallet = getWalletByUserId(updateWalletPasswordForm.getUserId());
        // 验证是否存在对应用户的钱包
        if (ObjectUtils.isEmpty(wallet)) {
            log.error("【更新用户支付密码】未查询到指定的用户钱包, userId={}", updateWalletPasswordForm.getUserId());
            throw new SellException(SellErrorCode.WALLET_NOT_FIND);

            //验证支付原密码是否正确
        } else if (!wallet.getPassword().equals(DigestUtils.md5DigestAsHex(updateWalletPasswordForm.getOldPayPassword().getBytes()))) {
            log.info("【更新用户支付密码】用户原密码错误");
            throw new SellException(SellErrorCode.WALLET_OLD_PASSWORD_ERROR);
        }

        // 更新用户的钱包支付密码
        wallet.setPassword(DigestUtils.md5DigestAsHex(updateWalletPasswordForm.getNewPayPassword().getBytes()));
        if (update(wallet, updateWalletPasswordForm.getUserId()) <= 0) {
            log.error("【更新用户支付密码】更新失败, userId={}", updateWalletPasswordForm.getUserId());
            throw new SellException(SellErrorCode.WALLET_UPDATE_PAY_PASSWORD_FAIL);
        }
    }

    /**
     * 用户充值
     * @param form 更新用户钱包表单
     * @return 返回充值结果
     */
    @Override
    public BigDecimal recharge(UpdateWalletBalanceFrom form) {

        // 验证码验证
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(form.getEmail(), form.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }
        // 进行用户充值
        Wallet wallet = getWalletByUserId(form.getUserId());
        BigDecimal balance = wallet.getBalance();
        wallet.setBalance(balance.add(form.getUpdateBigDecimal()));
        if (update(wallet, form.getUserId()) <= 0) {
            log.error("【钱包充值】用户充值失败，userId={}, money={}", form.getUserId(), form.getUpdateBigDecimal());
            throw new SellException(SellErrorCode.WALLET_RECHARGE_FAIL);
        }
        return wallet.getBalance();
    }

    /**
     * 用户提现
     * @param from 更新用户钱包表单
     * @return 提现结果
     */
    @Override
    public BigDecimal withdrawal(UpdateWalletBalanceFrom from) {

        // 验证码验证
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(from.getEmail(), from.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }
        Wallet wallet = getWalletByUserId(from.getUserId());
        BigDecimal balance = wallet.getBalance();

        // 判断钱包余额是否充足
        if (balance.compareTo(from.getUpdateBigDecimal()) < 0) {
            log.error("【钱包提现】用户提现余额不足，userId={}, money={}", from.getUserId(), from.getUpdateBigDecimal());
            throw new SellException(SellErrorCode.WALLET_NOT_ENOUGH);
        }

        // 用户钱包提现
        wallet.setBalance(balance.subtract(from.getUpdateBigDecimal()));
        if (update(wallet, from.getUserId()) <= 0) {
            log.error("【钱包提现】用户提现失败，userId={}, money={}", from.getUserId(), from.getUpdateBigDecimal());
            throw new SellException(SellErrorCode.WALLET_WITHDRAWAL_FAIL);
        }
        return wallet.getBalance();
    }

    /**
     * 根据用户 Id 返回 Wallet 钱包对象
     * @param userId 用户 Id
     * @return Wallet 钱包对象
     */
    @Override
    public Wallet getWalletByUserId(String userId) {
        Wallet wallet = walletRepository.selectOne(new QueryWrapper<Wallet>().eq("user_id", userId));
        if (wallet == null) {
            log.error("【钱包查询】未查询到用户钱包，userId={}", userId);
            throw new SellException(SellErrorCode.WALLET_NOT_FIND);
        }
        return wallet;
    }

    /**
     * 支付金额
     * @param payAmount 支付金额
     */
    @Override
    public void pay(BigDecimal payAmount, String userId, String password) {
        Wallet wallet = getWalletByUserId(userId);
        if (wallet.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            log.error("【支付订单】买家支付密码错误");
            throw new SellException(SellErrorCode.WALLET_PAY_PASSWORD_ERROR);
        }
        BigDecimal balance = wallet.getBalance();
        if (balance.compareTo(payAmount) < 0) {
            log.error("【支付订单】buyer 钱包余额不足");
            throw new SellException(SellErrorCode.WALLET_NOT_ENOUGH);
        } else {
            wallet.setBalance(balance.subtract(payAmount));
            if (update(wallet, userId) <= 0) {
                log.error("【钱包支付】支付失败，userId={}, payAmount={}", userId, payAmount);
                throw new SellException(SellErrorCode.WALLET_PAY_FAIL);
            }
        }
    }

    /**
     * 返还支付金额
     * @param backAmount 返回金额数目
     */
    @Override
    public void back(BigDecimal backAmount, String userId) {

        // 将退还金额返回到买家钱包中
        Wallet wallet = getWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance().add(backAmount));

        // 将退还的金额从商家余额中扣除
        if (update(wallet, userId) <= 0) {
            log.error("【订单入账】订单金额入账失败");
            throw new SellException(SellErrorCode.WALLET_BACK_FAIL);
        }
    }

    @Override
    public void businessBack(BigDecimal backAmount, String businessId) {
        // 将退还金额返回到买家钱包中
        Wallet wallet = getWalletByUserId(businessId);
        wallet.setBalance(wallet.getBalance().subtract(backAmount));

        // 将退还的金额从商家余额中扣除
        if (update(wallet, businessId) <= 0) {
            log.error("【订单入账】订单金额入账失败");
            throw new SellException(SellErrorCode.WALLET_BACK_FAIL);
        }
    }

    /**
     * 更新支付密码
     * @param userId 用户 ID
     * @param newPassword 新密码
     */
    @Override
    public void updatePayPassword(String userId, String newPassword) {
        QueryWrapper<Wallet> wrapper = new QueryWrapper<Wallet>().eq("user_id", userId);
        Wallet wallet = walletRepository.selectOne(wrapper);
        log.info("【更新支付密码】wallet={}", wallet);
        if (ObjectUtils.isEmpty(wallet)) {
            log.error("【更新钱包支付密码】未查询到指定的用户的钱包, userId={}", userId);
            throw new SellException(SellErrorCode.WALLET_NOT_FIND);
        }
        wallet.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        log.info("newPayPassword = {}", wallet.getPassword());
        if (walletRepository.update(wallet, wrapper) <= 0) {
            log.error("【更新钱包支付密码】更新失败");
            throw new SellException(SellErrorCode.WALLET_UPDATE_PAY_PASSWORD_FAIL);
        }
    }

    /**
     * 更新用户钱包 Balance
     * @param wallet 用户钱包
     * @param userId 用户 ID
     * @return 更新结果
     */
    public int update (Wallet wallet, String userId) {
        return walletRepository.update(wallet, new QueryWrapper<Wallet>().eq("user_id", userId));
    }
}
