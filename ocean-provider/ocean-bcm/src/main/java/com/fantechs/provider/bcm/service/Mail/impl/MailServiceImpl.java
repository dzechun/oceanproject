package com.fantechs.provider.bcm.service.Mail.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.provider.bcm.service.Mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mr.Lei
 * @create 2021/3/1
 */
@Service
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 简单文本邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content){
        try {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p;
            Matcher m;
            p = Pattern.compile(regEx1);
            m = p.matcher(to);
            if(!m.matches()){
                throw new BizErrorException("邮箱账号错误");
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom(from);

            mailSender.send(message);
        }catch (Exception e){
            logger.error("邮件发送异常:"+e.getMessage());
            throw new BizErrorException(e.getMessage());
        }
    }

    /**
     * HTML 文本邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @throws MessagingException
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content){
        try {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p;
            Matcher m;
            p = Pattern.compile(regEx1);
            m = p.matcher(to);
            if(!m.matches()){
                throw new BizErrorException("邮箱账号错误");
            }
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(from);

            mailSender.send(message);
        }catch (Exception e){
            logger.error("邮件发送异常:"+e.getMessage());
            throw new BizErrorException("邮件推送失败");
        }

    }

    /**
     * 附件邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @param multipartFile 附件路径
     * @throws MessagingException
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content,
                                    MultipartFile multipartFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(from);

            FileSystemResource file = new FileSystemResource(multipartFileToFile(multipartFile));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            mailSender.send(message);
        }catch (Exception e){
            logger.error("邮件发送异常:"+e.getMessage());
            throw new BizErrorException("邮件推送失败");
        }
    }

    /**
     * 图片邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @param rscPath 图片路径
     * @param rscId 图片ID
     * @throws MessagingException
     */
    @Override
    public void sendInlinkResourceMail(String to, String subject, String content,
                                       String rscPath, String rscId) {
        logger.info("发送静态邮件开始: {},{},{},{},{}", to, subject, content, rscPath, rscId);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {

            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(from);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);
            mailSender.send(message);
            logger.info("发送静态邮件成功!");

        } catch (MessagingException e) {
            logger.info("发送静态邮件失败: ", e);
            throw new BizErrorException("邮件推送失败");
        }

    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
