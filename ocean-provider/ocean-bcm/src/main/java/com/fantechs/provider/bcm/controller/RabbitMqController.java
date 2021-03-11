package com.fantechs.provider.bcm.controller;

import com.fantechs.provider.bcm.util.RabbitProducer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
@RestController
public class RabbitMqController {
    @Autowired
    private RabbitProducer rabbitProducer;

    @PostMapping("/sendDemoQueue")
    public Object sendDemoQueue(@RequestParam(required = true)String version, MultipartFile multipartFile) throws IOException {
        //String fileName = multipartFile.getOriginalFilename();
        byte[] fileName = multipartFile.getOriginalFilename().getBytes();
        byte[] vs = version.getBytes();
        byte[] file = multipartFile.getBytes();
        byte[] ibytes = new byte[28+file.length];
        ibytes[0]=(byte)2;
        System.arraycopy(fileName,0,ibytes,1,fileName.length);
        System.arraycopy(vs,0,ibytes,17,vs.length);
        System.arraycopy(file,0,ibytes,28,file.length);
        rabbitProducer.sendDemoQueue(ibytes);
        return "success";
    }
}
