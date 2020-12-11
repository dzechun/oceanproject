package com.fantechs.provider.restapi.imes.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.restapi.imes.service.GetDataFromU9Service;
import com.fantechs.provider.restapi.imes.service.impl.GetDataFromQisServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/QISAPI")
@Api(tags = "同步Qis数据", basePath = "/QISAPI")
public class GetDataFromQisController {


    @Resource
    private GetDataFromQisServiceImpl getDataFromQisService;

    @GetMapping("/updateStorageFromQis")
    @ApiOperation(value = "同步QIS仓库储位数据")
    public ResponseEntity updateStorageFromQis() throws Exception {


        int i = getDataFromQisService.updateStorageFromQis();
        if (i == 500) {
            return ControllerUtil.returnFail("同步QIS仓库储位数据，请勿重复操作", 500);
        }
        if (i == 1) {
            return ControllerUtil.returnSuccess("同步完成");
        } else {
            return ControllerUtil.returnFail("同步失败", -1);
        }
    }
}
