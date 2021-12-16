package com.fantechs.provider.restapi.mulinsen.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.restapi.mulinsen.service.NccBdStordocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "SCM仓库控制器")
@RequestMapping("/nccBdStordoc")
@Validated
public class NccBdStordocController {

    @Resource
    private NccBdStordocService nccBdStordocService;

    @ApiOperation("同步SCM仓库资料")
    @PostMapping("/synchronizeNccBdStordoc")
    public ResponseEntity synchronizeNccBdStordoc() throws Exception {
        return ControllerUtil.returnCRUD(nccBdStordocService.synchronizeNccBdStordoc());
    }

}
