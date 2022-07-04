package com.fantechs.provider.fileserver.controller;

import com.fantechs.provider.fileserver.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class MinioController {
    @Autowired
    private MinioUtil minioUtil;
    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public Object upload(MultipartFile file) {
        List<String> upload = minioUtil.upload(new MultipartFile[]{file});
        return address+"/"+bucketName+"/"+upload.get(0);
    }

    @GetMapping("/index")
    public String index() {
        return "123";
    }


}
