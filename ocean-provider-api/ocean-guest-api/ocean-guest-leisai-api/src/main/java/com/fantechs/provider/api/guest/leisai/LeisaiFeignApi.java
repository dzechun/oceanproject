package com.fantechs.provider.api.guest.leisai;

import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCarton;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ocean-leisai-api")
public interface LeisaiFeignApi {

    @ApiOperation("同步箱号信息到WMS")
    @PostMapping("/leisaiSyncData/syncCartonData")
    ResponseEntity syncCartonData(@ApiParam(value = "必传：",required = true)@RequestBody @Validated LeisaiWmsCarton leisaiWmsCarton);
}
