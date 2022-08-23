package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname WalletService
 * @Description TODO
 * @Date 2022/7/30 0:27
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
     * 更新密码
     * @param userId 用户 ID
     * @param newPassword 新密码
     */
    @PutMapping("/wallet/update/pay/password/by/userId")
    ResultVO<Object> updatePayPasswordByUserId(@RequestParam("userId") String userId,
                                               @RequestParam("newPassword") String newPassword);
}
