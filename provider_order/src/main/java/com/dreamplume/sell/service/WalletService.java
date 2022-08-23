package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @Classname WalletService
 * @Description TODO
 * @Date 2022/7/30 3:52
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-wallet", configuration = MultipartSupportConfig.class)
public interface WalletService {

    /**
     * 返还支付金额
     * @param backAmount 返回金额数目
     */
    @PutMapping("/wallet/back")
    ResultVO<Object> back(@RequestParam("backAmount") BigDecimal backAmount,
                          @RequestParam("userId") String userId);

    /**
     * 返还支付金额
     * @param backAmount 返回金额数目
     */
    @PutMapping("/wallet/business/back")
    ResultVO<Object> businessBack(@RequestParam("backAmount") BigDecimal backAmount,
                                  @RequestParam("businessId") String businessId);



    /**
     * 支付金额
     * @param backAmount 支付金额
     */
    @PutMapping("/wallet/pay")
    ResultVO<Object> pay(@RequestParam("backAmount") BigDecimal backAmount,
                         @RequestParam("userId") String userId,
                         @RequestParam("password") String password);

    /**
     * 根据用户 ID 返回 Wallet 钱包对象
     * @param userId 用户 Id
     * @return Wallet 对象
     */
    @GetMapping("/wallet/get/by/userId")
    ResultVO<Object> getWalletByUserId(@RequestParam("userId") String userId);
}
