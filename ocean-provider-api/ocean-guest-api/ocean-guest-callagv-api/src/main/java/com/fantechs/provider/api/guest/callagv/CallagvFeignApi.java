package com.fantechs.provider.api.guest.callagv;

import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.agv.dto.WarnCallbackDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ocean-guest-callagv")
public interface CallagvFeignApi {

    @PostMapping("/RCSAPI/warnCallbackDTO")
    @ApiOperation(value = "接收RCS通知MES的AGV告警信息", notes = "接收RCS通知MES的AGV告警信息")
    RcsResponseDTO warnCallbackDTO(@ApiParam(value = "接收RCS通知MES的AGV告警信息", required = true) @RequestBody WarnCallbackDTO warnCallbackDTO);
}
