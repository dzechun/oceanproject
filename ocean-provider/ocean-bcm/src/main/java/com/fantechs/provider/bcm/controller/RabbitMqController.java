package com.fantechs.provider.bcm.controller;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.general.dto.bcm.PrintDto;
import com.fantechs.common.base.general.dto.bcm.PrintModel;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.bcm.util.RabbitProducer;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
@RestController
@Api(tags = "打印")
@RequestMapping("/rabbit")
@Validated
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


    @PostMapping("/print")
    public ResponseEntity print(@RequestBody PrintDto printDto) {
        rabbitProducer.sendPrint(printDto);
        return ControllerUtil.returnSuccess();
    }
}
