package com.fantechs.provider.bcm.controller.mail;

import com.fantechs.provider.bcm.service.Mail.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author Mr.Lei
 * @create 2021/3/1
 */
@RestController
@Api(tags = "邮件推送")
@RequestMapping("/mail")
public class MailController {
    @Resource
    private MailService mailService;
    @Resource
    private TemplateEngine templateEngine;

    @ApiOperation("简单文本邮件")
    @GetMapping("/sendSimpleMail")
    public void sendSimpleMail(@ApiParam(value = "接收者邮箱",required = true)@RequestParam @NotNull(message = "接收邮件不能为空") String to,
                               @ApiParam(value = "标题",required = true)@RequestParam String subject,
                               @ApiParam(value = "内容",required = true)@RequestParam String contnet){
        mailService.sendSimpleMail(to,subject,contnet);
    }

    @ApiOperation("HTML 文本邮件")
    @GetMapping("/sendHtmlMail")
    public void sendHtmlMail(@ApiParam(value = "接收者邮箱",required = true)@RequestParam @NotNull(message = "接收邮件不能为空") String to,
                             @ApiParam(value = "标题",required = true)@RequestParam String subject,
                             @ApiParam(value = "内容",required = true)@RequestParam String contnet){
        mailService.sendHtmlMail(to,subject,contnet);
    }
}
