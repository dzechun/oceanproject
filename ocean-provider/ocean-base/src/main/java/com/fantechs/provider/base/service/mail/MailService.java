package com.fantechs.provider.base.service.mail;

import org.springframework.web.multipart.MultipartFile;


public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
    void sendHtmlMail(String to, String subject, String content);
    void sendAttachmentsMail(String to, String subject, String content,
                             MultipartFile multipartFile);
    void sendInlinkResourceMail(String to, String subject, String content,
                                String rscPath, String rscId);
}
