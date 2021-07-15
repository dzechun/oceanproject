package com.fantechs.provider.eam.controller.socket;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.eam.service.socket.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@RestController
@Api(tags = "新宝ESOP设备管理")
@RequestMapping("/print")
public class SocketController {
    @Resource
    private SocketService socketService;

    @ApiOperation("发送指令")
    @GetMapping("/instructions")
    public ResponseEntity instructions(@RequestParam(required = true)String ip,
                                       @RequestParam(value = "控制设备命令，1203为关机，1204为重启",required = true) String code){
        int i = socketService.instructions(ip,code);
        return ControllerUtil.returnCRUD(i);
    }

    @GetMapping("/manyOpen")
    public ResponseEntity manyOpen() throws IOException {
        socketService.manyServer();
        return ControllerUtil.returnCRUD(1);
    }
}
