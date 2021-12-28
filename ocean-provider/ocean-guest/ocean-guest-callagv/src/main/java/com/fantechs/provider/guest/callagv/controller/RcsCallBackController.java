package com.fantechs.provider.guest.callagv.controller;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.agv.dto.WarnCallbackDTO;
import com.fantechs.provider.api.guest.callagv.CallagvFeignApi;
import com.fantechs.provider.guest.callagv.service.RcsCallBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "接收RCS推送的接口")
@Slf4j
public class RcsCallBackController {

    @Resource
    RcsCallBackService rcsCallBackService;

    @Resource
    CallagvFeignApi callagvFeignApi;

    @PostMapping("/RCSAPI/agvCallback")
    @ApiOperation(value = "接收RCS通知MES的AGV信息", notes = "接收RCS通知MES的AGV信息")
    public RcsResponseDTO getAGVFromRCS(
            @ApiParam(value = "接收RCS通知信息数据", required = true) @RequestBody AgvCallBackDTO agvCallBackDTO) {

        log.info("接收RCS通知MES的AGV信息：" + JSONObject.toJSONString(agvCallBackDTO));
        RcsResponseDTO rcsResponseDTO = new RcsResponseDTO();
        rcsResponseDTO.setReqCode(agvCallBackDTO.getReqCode());
        rcsResponseDTO.setData("");
        try {
            rcsCallBackService.agvCallback(agvCallBackDTO);
            rcsResponseDTO.setCode("0");
            rcsResponseDTO.setMessage("成功");
        } catch (Exception e) {
            rcsResponseDTO.setCode("500");
            rcsResponseDTO.setMessage(e.getMessage());
        }
        log.info("MES返回RCS的信息：" + JSONObject.toJSONString(rcsResponseDTO));

        return rcsResponseDTO;
    }

    @PostMapping("/service/rest/agvCallbackService/warnCallback")
    @ApiOperation(value = "接收RCS通知MES的AGV告警信息", notes = "接收RCS通知MES的AGV告警信息")
    public RcsResponseDTO warnCallback(
            @ApiParam(value = "接收RCS通知MES的AGV告警信息", required = true) @RequestBody WarnCallbackDTO warnCallbackDTO) {

        return callagvFeignApi.warnCallbackDTO(warnCallbackDTO);
    }

    @PostMapping("/RCSAPI/warnCallbackDTO")
    @ApiOperation(value = "接收RCS通知MES的AGV告警信息", notes = "接收RCS通知MES的AGV告警信息")
    public RcsResponseDTO warnCallbackDTO(
            @ApiParam(value = "接收RCS通知MES的AGV告警信息", required = true) @RequestBody WarnCallbackDTO warnCallbackDTO) {

        log.info("接收RCS通知MES的AGV告警信息：" + JSONObject.toJSONString(warnCallbackDTO));
        RcsResponseDTO rcsResponseDTO = new RcsResponseDTO();
        rcsResponseDTO.setReqCode(warnCallbackDTO.getReqCode());
        try {
            rcsCallBackService.warnCallbackDTO(warnCallbackDTO);
            rcsResponseDTO.setCode("0");
            rcsResponseDTO.setMessage("成功");
        } catch (Exception e) {
            rcsResponseDTO.setCode("500");
            rcsResponseDTO.setMessage(e.getMessage());
        }
        log.info("MES返回RCS的信息：" + JSONObject.toJSONString(rcsResponseDTO));

        return rcsResponseDTO;
    }
}
