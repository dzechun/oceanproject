package com.fantechs.provider.api.wms.out;


import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Data 2021-01-19 10:31
 */
@FeignClient(name = "ocean-wms-out")
public interface OutFeignApi {

    @ApiOperation(value = "发料计划新增", notes = "发料计划新增")
    @PostMapping("/wmsOutProductionMaterial/add")
    ResponseEntity outProductionMaterialAdd(@ApiParam(value = "必传：", required = true) @RequestBody @Validated WmsOutProductionMaterial wmsOutProductionMaterial);

    @ApiOperation("发料计划修改")
    @PostMapping("/wmsOutProductionMaterial/update")
    ResponseEntity outProductionMaterialUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutProductionMaterial.update.class) WmsOutProductionMaterial wmsOutProductionMaterial);

}
