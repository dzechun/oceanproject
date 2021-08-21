package com.fantechs.provider.eam.controller;


import com.fantechs.provider.eam.service.EamEquipmentStandingBookAttachmentService;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@RestController
@Api(tags = "eamEquipmentStandingBookAttachment控制器")
@RequestMapping("/eamEquipmentStandingBookAttachment")
@Validated
public class EamEquipmentStandingBookAttachmentController {

    @Resource
    private EamEquipmentStandingBookAttachmentService eamEquipmentStandingBookAttachmentService;

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentStandingBookAttachmentService.batchDelete(ids));
    }

    @ApiOperation("文件上传")
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@ApiParam(value = "文件必传",required = true) @RequestPart(value = "file") MultipartFile file) {
        String path = eamEquipmentStandingBookAttachmentService.uploadFile(file);
        return  ControllerUtil.returnDataSuccess(path,1);
    }
}
