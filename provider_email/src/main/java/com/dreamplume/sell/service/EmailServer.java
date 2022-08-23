package com.dreamplume.sell.service;


import com.dreamplume.sell.exception.SellException;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @Classname EmailServer
 * @Description TODO
 * @Date 2022/5/8 12:11
 * @Created by 翊
 */
public interface EmailServer {

    /**
     * 发送邮件给用户
     * @param recipientEmail 收件人邮箱
     */
    void sendEmail(String recipientEmail);

    /**
     * 推送消息给用户
     * @param allUserEmailList 所有用户 Email 集合
     * @param pictureUrl 图片 url
     */
    void sendMessage(List<String> allUserEmailList, String pictureUrl);

    /**
     * 邮箱验证
     * @param recipientEmail 收件人邮箱
     * @param code 验证码
     */
    void verificationCode(String recipientEmail, String code);
}
