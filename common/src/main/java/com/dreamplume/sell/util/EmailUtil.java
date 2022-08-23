package com.dreamplume.sell.util;


import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Random;

/**
 * @Classname EmailUtil
 * @Description TODO
 * @Date 2022/5/8 9:15
 * @Created by 翊
 */
public class EmailUtil {

    public static String sendEmail(JavaMailSenderImpl mailSender, String recipientEmail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        StringBuilder codeNum = new StringBuilder();
        int[] code = new int[3];
        Random random = new Random();
        //自动生成验证码
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(10) + 48;
            int uppercase = random.nextInt(26) + 65;
            int lowercase = random.nextInt(26) + 97;
            code[0] = num;
            code[1] = uppercase;
            code[2] = lowercase;
            codeNum.append((char) code[random.nextInt(3)]);
        }
        //标题
        helper.setSubject("您的验证码为：" + codeNum);
        //内容
        helper.setText("您好！感谢支持梦君小店。您的验证码为：" + "<h2>" + codeNum + "</h2>" + "千万不能告诉别人哦！", true);
        //邮件接收者
        helper.setTo(recipientEmail);
        //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
        helper.setFrom("3040988158@qq.com");
        mailSender.send(mimeMessage);
        return codeNum.toString();
    }

    public static void sendMessage(JavaMailSenderImpl mailSender, List<String> recipientEmailList, String pictureUrl) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        //标题
        helper.setSubject("DreamJunOrder：感谢支持梦君小店");
        //内容
        helper.setText("<h1> 亲爱的用户，DreamJunOrder完成了新版本的更新：</h1>"+"<br />"+"<img src=\""+pictureUrl+"\" />"+"<br />"+"本邮件由系统自动发送，请勿直接回复", true);
        //邮件接收者
        for (String recipientEmail : recipientEmailList) {
            helper.setTo(recipientEmail);
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom("3040988158@qq.com");
            mailSender.send(mimeMessage);
        }
    }
}
