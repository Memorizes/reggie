package com.nuist.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    private static JavaMailSender mailSender;

    private static String postEmail;

    @Autowired
    private void getMailSender(JavaMailSender javaMailSender) {
        EmailUtils.mailSender =  javaMailSender;
    }

    @Value("${spring.mail.username}")
    private void getPostEmail(String postEmail1) {
        EmailUtils.postEmail = postEmail1;
    }


    public static void sendEMail(String receiveEmail, String code) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(postEmail); //设置发送邮件账号
            simpleMailMessage.setTo(receiveEmail); //设置接收邮件的人，可以多个
            simpleMailMessage.setSubject(""); //设置发送邮件的主题
            simpleMailMessage.setText("验证码为:" + code); //设置发送邮件的内容
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            System.out.println("邮件发送失败!");
        }
    }
}
