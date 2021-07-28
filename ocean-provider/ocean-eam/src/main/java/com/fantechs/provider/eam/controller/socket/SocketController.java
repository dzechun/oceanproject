package com.fantechs.provider.eam.controller.socket;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.eam.service.socket.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@RestController
@Api(tags = "设备状态监控")
@RequestMapping("/equipmentStatus")
@Validated
public class SocketController {
    @Resource
    private SocketService socketService;

    @ApiOperation("发送指令")
    @PostMapping("/instructions")
    public ResponseEntity instructions(@RequestParam(required = true)String ip,
                                       @RequestParam(value = "控制设备命令，1203为关机，1204为重启",required = true) String code,
                                       @RequestParam(required = false) String url){
        int i = socketService.instructions(ip,code,url);
        return ControllerUtil.returnCRUD(i);
    }

    @GetMapping("/manyOpen")
    public ResponseEntity openService() throws IOException {
        socketService.openService();
        return ControllerUtil.returnCRUD(1);
    }


}
