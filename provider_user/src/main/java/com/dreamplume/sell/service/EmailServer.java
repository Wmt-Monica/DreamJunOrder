package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname EmailServer
 * @Description TODO
 * @Date 2022/7/30 0:27
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-email", configuration = MultipartSupportConfig.class)
public interface EmailServer {

    /**
     * 邮箱验证
     * @param recipientEmail 收件人邮箱
     * @param code 验证码
     */
    @PutMapping("/email/verification/code")
    ResultVO<Object> verificationCode(@RequestParam("recipientEmail") String recipientEmail,
                                      @RequestParam("code") String code);
}
