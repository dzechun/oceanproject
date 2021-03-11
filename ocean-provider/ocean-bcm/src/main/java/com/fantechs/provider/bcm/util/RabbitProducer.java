package com.fantechs.provider.bcm.util;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
@Component
public class RabbitProducer {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendDemoQueue(byte[] bytes) throws IOException {
//        Date date = new Date();
//        String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
//        System.out.println("[demoQueue] send msg: " + dateString);
        //byte[] imgBytes = multipartFile.getBytes();
        // 第一个参数为刚刚定义的队列名称
        this.rabbitTemplate.convertAndSend("demoQueue", bytes);
    }
}
