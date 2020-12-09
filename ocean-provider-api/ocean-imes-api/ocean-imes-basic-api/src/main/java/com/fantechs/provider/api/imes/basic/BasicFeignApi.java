package com.fantechs.provider.api.imes.basic;

import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ocean-imes-basic")
public interface BasicFeignApi {

    @PostMapping("/smtStorage/detail")
    @ApiOperation(value = "获取储位信息",notes = "获取电子标签控制器和储位信息")
    ResponseEntity<SmtStorage> detail(@ApiParam(value = "id",required = true)@RequestParam(value="id")  Long id) ;

    @PostMapping(value="/smtMaterial/findList")
    @ApiOperation(value = "获取物料信息",notes = "获取物料信息")
    ResponseEntity<List<SmtMaterial>> findSmtMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchSmtMaterial searchSmtMaterial);

}
