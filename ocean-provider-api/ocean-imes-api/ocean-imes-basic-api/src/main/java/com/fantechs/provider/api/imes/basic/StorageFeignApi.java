package com.fantechs.provider.api.imes.basic;

import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ocean-imes-basic")
public interface StorageFeignApi {

    @PostMapping("/smtStorage/detail")
    ResponseEntity<SmtStorage> detail(@ApiParam(value = "id",required = true)@RequestParam(value="id")  Long id) ;

}
