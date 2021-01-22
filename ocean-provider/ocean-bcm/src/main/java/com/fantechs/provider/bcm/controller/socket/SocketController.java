package com.fantechs.provider.bcm.controller.socket;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.bcm.service.socket.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@RestController
@Api(tags = "打印接口")
@RequestMapping("/print")
public class SocketController {
    @Resource
    private SocketService socketService;


    @ApiOperation("打印")
    @PostMapping("/send")
    public ResponseEntity send(@RequestBody String sss){
        String ss = socketService.sender(ControllerUtil.dynamicConditionByEntity(sss));
        return ControllerUtil.returnSuccess(ss);
    }
}
