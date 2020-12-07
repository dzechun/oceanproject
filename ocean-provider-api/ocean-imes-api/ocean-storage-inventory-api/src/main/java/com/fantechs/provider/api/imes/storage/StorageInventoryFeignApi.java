package com.fantechs.provider.api.imes.storage;

import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @Date 2020/12/7 17:39
 */
@FeignClient(name = "ocean-imes-basic")
public interface StorageInventoryFeignApi {

    @PostMapping("/smtStorageMaterial/detail")
    ResponseEntity<SmtStorageMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) ;

}
