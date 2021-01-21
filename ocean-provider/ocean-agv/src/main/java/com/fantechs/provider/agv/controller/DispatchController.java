package com.fantechs.provider.agv.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.agv.service.DispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/dispatch")
@Api(tags = "任务调度", description = "任务调度")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    @PostMapping("/genAgvSchedulingTask")
    @ApiOperation(value = "生成任务单接口",notes = "生成任务单接口")
    public ResponseEntity<String> genAgvSchedulingTask(
            @ApiParam(value = "请求参数键值对", required = true) @RequestBody Map<String,Object> map) {
        return ControllerUtil.returnDataSuccess(dispatchService.genAgvSchedulingTask(map), 1);
    }

    @PostMapping("/continueTask")
    @ApiOperation(value = "继续执行任务",notes = "继续执行任务")
    public ResponseEntity<String> continueTask(
            @ApiParam(value = "请求参数键值对", required = true) @RequestBody Map<String,Object> map) {
        return ControllerUtil.returnSuccess(dispatchService.continueTask(map));
    }
}
