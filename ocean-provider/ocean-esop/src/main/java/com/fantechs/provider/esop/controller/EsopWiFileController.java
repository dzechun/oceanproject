package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.esop.service.EsopWiFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@RestController
@Api(tags = "电子WI附件上传")
@RequestMapping("/esopHtWiFile")
@Validated
public class EsopWiFileController {

    @Resource
    private EsopWiFileService esopWiFileService;

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopWiFileService.batchDelete(ids));
    }

    @ApiOperation("文件上传")
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@ApiParam(value = "文件必传",required = true) @RequestPart(value = "file") MultipartFile file) {
        String path = esopWiFileService.uploadFile(file);
        return  ControllerUtil.returnDataSuccess(path,1);
    }
}
