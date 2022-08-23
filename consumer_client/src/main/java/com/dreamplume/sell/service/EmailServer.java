package com.dreamplume.sell.service;


import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Classname EmailServer
 * @Description TODO
 * @Date 2022/5/8 12:11
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-email", configuration = MultipartSupportConfig.class)
public interface EmailServer {

    /**
     * 发送邮件给用户
     * @param recipientEmail 收件人邮箱
     */
    @PostMapping("/email/send/email")
    ResultVO<Object> sendEmail(@RequestParam("recipientEmail") String recipientEmail);

    /**
     * 推送消息给用户
     * @param allUserEmailList 所有用户 Email 集合
     * @param pictureUrl 图片 url
     */
    @PostMapping("/email/send/message")
    ResultVO<Object> sendMessage(@RequestParam("allUserEmailList") List<String> allUserEmailList,
                                 @RequestParam("pictureUrl") String pictureUrl);

    /**
     * 邮箱验证
     * @param recipientEmail 收件人邮箱
     * @param code 验证码
     */
    @PutMapping("/email/verification/code")
    ResultVO<Object> verificationCode(@RequestParam("recipientEmail") String recipientEmail,
                                      @RequestParam("code") String code);
}

