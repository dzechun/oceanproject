package com.fantechs.provider.api.mes.sfc;

import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.response.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ocean-mes-sfc")
public interface SFCFeignApi {
    @PostMapping("/rabbit/print")
    ResponseEntity print(@RequestBody PrintDto printDto);
}
