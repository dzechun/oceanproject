package com.fantechs.provider.ews.utils;

import com.fantechs.common.base.exception.BizErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Author mr.lei
 * @Date 2021/12/28
 */
@Slf4j
@Component
public class MailService {

    public String username = "";
    public String password = "";
    public String host = "";
    public String protocol = "";
    public int port = 587;


    /**
     * 简单文本邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String[] to, String subject, String content){
        try {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setUsername(username);
            javaMailSender.setPassword(password);
            javaMailSender.setHost(host);
            javaMailSender.setProtocol(protocol);
            javaMailSender.setPort(port);
            javaMailSender.setDefaultEncoding("UTF-8");
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth","true");
            properties.put("mail.smtp.ssl.enable", "true");
            javaMailSender.setJavaMailProperties(properties);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom(username);

            javaMailSender.send(message);
        }catch (Exception e){
            log.error("邮件发送异常:"+e.getMessage());
            throw new BizErrorException(e.getMessage());
        }
    }


}
