package com.hang.reiji.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 专门发邮件的工具类
 */
public class UtilEmail {

    //发送方
    @Value("${spring.mail.from}")
    private static String from;

    private static final JavaMailSender javaMailSender = new JavaMailSenderImpl();

    /**
     * 发邮件,纯文本
     * @param receiver 接收方的邮箱
     * @param title 标题
     * @param text 正文
     */
    public static void sendMail(String receiver,String title,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(receiver);
        message.setSubject(title);
        message.setText(text);
        javaMailSender.send(message);
    }

    /**
     * 发邮件，加文件
     * @param receiver 接收方的邮箱
     * @param title 标题
     * @param text 正文
     * @param file 文件
     */
    public static void sendFile(String receiver, String title, String text, File file){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(receiver);
            helper.setSubject(title);
            helper.setText(text);
            helper.addAttachment(file.getName(),file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
