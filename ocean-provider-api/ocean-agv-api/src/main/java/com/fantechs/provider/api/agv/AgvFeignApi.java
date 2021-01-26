package com.fantechs.provider.api.agv;

import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ocean-mes-agv")
public interface AgvFeignApi {

    @PostMapping("/dispatch/genAgvSchedulingTask")
    @ApiOperation(value = "生成任务单接口",notes = "生成任务单接口")
    ResponseEntity<String> genAgvSchedulingTask(
            @ApiParam(value = "请求参数键值对", required = true) @RequestBody Map<String,Object> map);

    @PostMapping("/dispatch/continueTask")
    @ApiOperation(value = "继续执行任务",notes = "继续执行任务")
    ResponseEntity<String> continueTask(@ApiParam(value = "请求参数键值对", required = true) @RequestBody Map<String,Object> map);
}
