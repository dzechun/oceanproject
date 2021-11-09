package com.fantechs.provider.esop.controller.socket;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.esop.service.socket.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/esopEquipmentStatus")
@Validated
public class SocketController {
    @Resource
    private SocketService socketService;

    @ApiOperation("发送指令")
    @PostMapping("/instructions")
    public ResponseEntity instructions(@ApiParam(value = "ip",required = true)@RequestParam String ip,
                                       @ApiParam(value = "code命令 1203为重启 1204为关机",required = true)@RequestParam String code,
                                       @ApiParam(value = "url")@RequestParam String url){
        int i = socketService.instructions(ip,code,url);
        return ControllerUtil.returnCRUD(i);
    }

    @GetMapping("/manyOpen")
    public ResponseEntity openService() throws IOException {
        socketService.openService();
        return ControllerUtil.returnCRUD(1);
    }

    @GetMapping("/closeThird")
    public ResponseEntity closeThird()  {
        socketService.closeThird();
        return ControllerUtil.returnCRUD(1);
    }

}
