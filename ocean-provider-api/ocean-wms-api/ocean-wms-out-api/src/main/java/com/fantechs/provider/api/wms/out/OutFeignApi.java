package com.fantechs.provider.api.wms.out;


import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutProductionMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Data 2021-01-19 10:31
 */
@FeignClient(name = "ocean-wms-out")
public interface OutFeignApi {

    @ApiOperation(value = "发料计划新增", notes = "发料计划新增")
    @PostMapping("/wmsOutProductionMaterial/add")
    ResponseEntity<WmsOutProductionMaterial> outProductionMaterialAdd(@ApiParam(value = "必传：", required = true) @RequestBody @Validated WmsOutProductionMaterial wmsOutProductionMaterial);

    @ApiOperation(value = "发料计划明细新增",notes = "发料计划明细新增")
    @PostMapping("/wmsOutProductionMaterialdDet/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet);

    @ApiOperation("根据工单和物料反写发料数量")
    @PostMapping("/wmsOutProductionMaterial/updateByWorkOrderId")
    ResponseEntity updateByWorkOrderId(@ApiParam(value = "对象，Id必传",required = true)@RequestBody WmsOutProductionMaterial wmsOutProductionMaterial);

    @ApiOperation("发料计划列表")
    @PostMapping("/wmsOutProductionMaterial/findList")
    ResponseEntity<List<WmsOutProductionMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutProductionMaterial searchWmsOutProductionMaterial);

}
