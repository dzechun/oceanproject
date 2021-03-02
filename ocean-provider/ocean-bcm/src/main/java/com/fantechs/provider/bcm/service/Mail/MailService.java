package com.fantechs.provider.bcm.service.Mail;

import org.springframework.web.multipart.MultipartFile;


public interface MailService {
    void sendSimpleMail(String to, String subject, String contnet);
    void sendHtmlMail(String to, String subject, String contnet);
    void sendAttachmentsMail(String to, String subject, String contnet,
                             MultipartFile multipartFile);
    void sendInlinkResourceMail(String to, String subject, String contnet,
                                String rscPath, String rscId);
}
