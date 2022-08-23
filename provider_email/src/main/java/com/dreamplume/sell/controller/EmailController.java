package com.dreamplume.sell.controller;

import com.dreamplume.sell.service.EmailServer;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname EmailController
 * @Description TODO
 * @Date 2022/5/8 12:34
 * @Created by 翊
 */
@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {

    @Resource
    EmailServer emailServer;

    /**
     * 发送邮件给用户
     * @param recipientEmail 收件人邮箱
     */
    @PostMapping("/send/email")
    public ResultVO<Object> sendEmail(@RequestParam("recipientEmail") String recipientEmail) {
        emailServer.sendEmail(recipientEmail);
        return ResultVOUtil.success();
    }

    /**
     * 推送消息给用户
     * @param allUserEmailList 所有用户 Email 集合
     * @param pictureUrl 图片 url
     */
    @PostMapping("/send/message")
    public ResultVO<Object> sendMessage(@RequestParam("allUserEmailList") List<String> allUserEmailList,
                                        @RequestParam("pictureUrl") String pictureUrl) {
        emailServer.sendMessage(allUserEmailList, pictureUrl);
        return ResultVOUtil.success();
    }

    /**
     * 邮箱验证
     * @param recipientEmail 收件人邮箱
     * @param code 验证码
     */
    @PutMapping("/verification/code")
    public ResultVO<Object> verificationCode(@RequestParam("recipientEmail") String recipientEmail,
                                             @RequestParam("code") String code) {
        emailServer.verificationCode(recipientEmail, code);
        return ResultVOUtil.success();
    }
}
