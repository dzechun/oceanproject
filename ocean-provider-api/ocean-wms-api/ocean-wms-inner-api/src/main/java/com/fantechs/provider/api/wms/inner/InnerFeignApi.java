package com.fantechs.provider.api.wms.inner;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.*;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ocean-wms-inner")
public interface InnerFeignApi {
    @ApiOperation("库存查询")
    @PostMapping("/wmsInnerInventory/findList")
    ResponseEntity<List<WmsInnerInventoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventory searchWmsInnerInventory);

    @ApiOperation(value = "库存新增",notes = "新增")
    @PostMapping("/wmsInnerInventory/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInventory wmsInnerInventory);

    @ApiOperation("库存修改")
    @PostMapping("/wmsInnerInventory/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerInventory.update.class) WmsInnerInventory wmsInnerInventory);


    @PostMapping("/wmsInnerInventory/selectOneByExample")
    ResponseEntity<WmsInnerInventory> selectOneByExample(@RequestBody Map<String,Object> map);
    @PostMapping("/wmsInnerInventory/updateByPrimaryKeySelective")
    ResponseEntity updateByPrimaryKeySelective(@RequestBody WmsInnerInventory wmsInnerInventory);
    @PostMapping("/wmsInnerInventory/updateByExampleSelective")
    ResponseEntity updateByExampleSelective(@RequestBody WmsInnerInventory wmsInnerInventory,@RequestParam Map<String,Object> map);
    @PostMapping("/wmsInnerInventory/insertSelective")
    ResponseEntity insertSelective(@RequestBody WmsInnerInventory wmsInnerInventory);

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/wmsInnerJobOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInPutawayOrder);

    @ApiOperation("列表")
    @PostMapping("/wmsInnerJobOrderDet/findList")
     ResponseEntity<List<WmsInnerJobOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrderDet searchWmsInPutawayOrderDet);

    @ApiOperation("列表")
    @PostMapping("/wmsInnerJobOrder/findList")
    ResponseEntity<List<WmsInnerJobOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrder searchWmsInPutawayOrder);

    @PostMapping("/pickingOrder/retrographyStatus")
    ResponseEntity retrographyStatus(@RequestBody WmsInnerJobOrderDet wmsInnerJobOrderDet);
}