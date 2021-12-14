package com.fantechs.provider.api.srm;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmInAsnOrder;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmInAsnOrderDetBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-srm")
public interface SrmFeignApi {

    @ApiOperation("预收货通知单列表")
    @PostMapping("/srmInAsnOrder/findList")
    ResponseEntity<List<SrmInAsnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrder searchSrmInAsnOrder);

    @ApiOperation(value = "新增预收货通知单",notes = "新增预收货通知单")
    @PostMapping("/srmInAsnOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmInAsnOrderDto srmInAsnOrderDto);

    @ApiOperation("预收货通知单条码列表")
    @PostMapping("/srmInAsnOrderDetBarcode/findList")
    ResponseEntity<List<SrmInAsnOrderDetBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode);

}
