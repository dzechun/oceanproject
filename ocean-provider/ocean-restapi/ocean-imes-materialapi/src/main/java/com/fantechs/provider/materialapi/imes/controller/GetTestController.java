package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.GetTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "WebService接口调用测试")
@RequestMapping("/test")
@Validated
public class GetTestController {

    @Resource
    private GetTestService getTestService;

   @ApiOperation(value = "调用",notes = "新增")
    @PostMapping("/http")
    public ResponseEntity http(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
       try {
           getTestService.getHttp();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return ControllerUtil.returnCRUD(1);
    }

    @ApiOperation(value = "调用",notes = "新增")
    @PostMapping("/apach")
    public ResponseEntity apach(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
            getTestService.getApache();
        return ControllerUtil.returnCRUD(1);
    }

    @ApiOperation(value = "调用",notes = "新增")
    @PostMapping("/axis")
    public ResponseEntity axis(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) throws ServiceException, RemoteException {
       // getTestService.getAxis2();
        return ControllerUtil.returnCRUD(1);
    }

    @ApiOperation(value = "apachMes",notes = "apachMes")
    @PostMapping("/apachMes")
    public ResponseEntity apachMes(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        getTestService.getApacheMes();
        return ControllerUtil.returnCRUD(1);
    }

    @ApiOperation(value = "leisai",notes = "leisai")
    @PostMapping("/leisai")
    public ResponseEntity leisai(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        getTestService.getLeisai();
        return ControllerUtil.returnCRUD(1);
    }
}
