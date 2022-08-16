package com.fantechs.provider.api.fileserver.service;


import com.fantechs.common.base.response.ResponseEntity;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lfz on 2018/8/22.
 */
@FeignClient(value = "ocean-fileserver")
public interface FileFeignApi {

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity fileUpload(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/file/download")
    Response download(@RequestParam(value = "fileUrl", required = true) String fileUrl);
}
