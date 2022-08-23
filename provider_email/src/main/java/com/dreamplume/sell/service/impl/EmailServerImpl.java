package com.dreamplume.sell.service.impl;

import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.service.EmailServer;
import com.dreamplume.sell.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Classname EmailServerImpl
 * @Description TODO
 * @Date 2022/5/8 12:12
 * @Created by 翊
 */
@Service
@Slf4j
public class EmailServerImpl implements EmailServer {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    JavaMailSenderImpl mailSender;

    /**
     * 系统发送推送消息
     * @param pictureUrl 更新 APP 下载新二维码
     */
    public void sendMessage(List<String> allUserEmailList, String pictureUrl) {
        try {
            EmailUtil.sendMessage(mailSender, allUserEmailList, pictureUrl);
        } catch (MessagingException e) {
            log.error("推送消息失败");
            throw new SellException(SellErrorCode.EMAIL_SEND_FAIL);
        }
    }

    /**
     * 发送邮箱验证
     * @param recipientEmail 收件人邮箱
     */
    @Override
    public void sendEmail(String recipientEmail) {
        try {
            // 如果十分钟内，该 codeNum 未失效，则拒绝多次发送邮箱
            if (!ObjectUtils.isEmpty(stringRedisTemplate.opsForValue().get(recipientEmail))) {
                log.info("【邮箱发送】十分钟内已经发送过邮箱了，请勿重复操作");
                throw new SellException(SellErrorCode.EMAIL_REPLICATION_SEND);
            }
            String codeNum = EmailUtil.sendEmail(mailSender, recipientEmail);
            // 将邮箱写入 redis 中，保存时间为 5 分钟
            stringRedisTemplate.opsForValue().set(recipientEmail, codeNum,5, TimeUnit.MINUTES);
            log.info("【邮箱发送】邮箱发送成功，codeNum={}", codeNum);
        } catch (MessagingException e) {
            log.error("邮箱消息失败");
            throw new SellException(SellErrorCode.EMAIL_SEND_FAIL);
        }
    }

    /**
     * 验证邮箱验证码
     * @param recipientEmail 收件人邮箱
     * @param code 验证码
     */
    @Override
    public void verificationCode(String recipientEmail, String code) {
        String redisCode = stringRedisTemplate.opsForValue().get(recipientEmail);
        log.info("【邮箱验证】redisCode={}", redisCode);
        log.info("【邮箱验证】code={}", code);
        if (ObjectUtils.isEmpty(redisCode)) {
            log.info("【邮箱验证】验证码已失效，请重新发送");
            throw new SellException(SellErrorCode.VERIFICATION_CODE_EXPIRED);
        } else if (!redisCode.equals(code)) {
            log.info("【邮箱验证】验证码错误, recipientEmail={}", recipientEmail);
            throw new SellException(SellErrorCode.VERIFICATION_CODE_ERROR);
        }
        // 验证码使用后就从 Redis 删除缓存
        stringRedisTemplate.delete(recipientEmail);
    }
}
