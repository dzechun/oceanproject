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

import java.math.BigDecimal;
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
    @PostMapping("/wmsInnerInventory/insertList")
    ResponseEntity insertList(@RequestBody List<WmsInnerInventory> wmsInnerInventories);

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/wmsInnerJobOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInPutawayOrder);

    @ApiOperation(value = "成品检验单复检更新移位单",notes = "成品检验单复检更新移位单")
    @PostMapping("/wmsInnerJobOrder/updateShit")
    ResponseEntity updateShit(@RequestParam Long jobOrderId, @RequestParam BigDecimal ngQty);

    @ApiOperation("列表")
    @PostMapping("/wmsInnerJobOrderDet/findList")
     ResponseEntity<List<WmsInnerJobOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrderDet searchWmsInPutawayOrderDet);

    @ApiOperation("列表")
    @PostMapping("/wmsInnerJobOrder/findList")
    ResponseEntity<List<WmsInnerJobOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrder searchWmsInPutawayOrder);

    @PostMapping("/pickingOrder/retrographyStatus")
    ResponseEntity retrographyStatus(@RequestBody WmsInnerJobOrderDet wmsInnerJobOrderDet);

    @ApiOperation("装车拣货单列表")
    @PostMapping("/pickingOrder/findList")
    ResponseEntity<List<WmsInnerJobOrderDto>> findPickingOrderList(@RequestBody SearchWmsInnerJobOrder searchWmsInnerJobOrder);

    @ApiOperation("按条件查询明细")
    @PostMapping("/wmsInnerInventoryDet/findByDet")
    ResponseEntity<WmsInnerInventoryDet> findByDet(@RequestParam String barCode);

    @ApiOperation("加库存明细")
    @PostMapping("/wmsInnerInventoryDet/add")
    ResponseEntity add(@RequestBody List<WmsInnerInventoryDet> wmsInnerInventoryDets);

    @ApiOperation("减库存明细")
    @PostMapping("/wmsInnerInventoryDet/subtract")
    ResponseEntity subtract(@RequestBody WmsInnerInventoryDet wmsInnerInventoryDet);

    @ApiOperation("列表")
    @PostMapping("/wmsInnerInventoryDet/findList")
    ResponseEntity<List<WmsInnerInventoryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryDet searchWmsInnerInventoryDet);

    @ApiOperation("修改")
    @PostMapping("/wmsInnerInventoryDet/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerInventoryDet.update.class) WmsInnerInventoryDet wmsInnerInventoryDet);

    @ApiOperation("来料打印列表")
    @PostMapping("/wmsInnerMaterialBarcode/findList")
    ResponseEntity<List<WmsInnerMaterialBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode);

    @ApiOperation(value = "新增库存日志",notes = "新增库存日志")
    @PostMapping("/wmsInnerInventoryLog/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInventoryLog wmsInnerInventoryLog);

    @PostMapping("/wmsInnerJobOrder/addList")
    ResponseEntity addList(@RequestBody List<WmsInnerJobOrder> list);

    @ApiOperation("列表")
    @PostMapping("/wmsInventoryVerification/findList")
    ResponseEntity<List<WmsInnerStockOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrder searchWmsInnerStockOrder);

    @ApiOperation(value = "领料单发运校验",notes = "领料单发运校验")
    @PostMapping("/wmsInnerJobOrderDet/pickDisQty")
    ResponseEntity pickDisQty(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WmsInnerJobOrderDet> wmsInPutawayOrderDet);

    @ApiOperation(value = "重新生成质检移位单",notes = "重新生成质检移位单")
    @PostMapping("/wmsInnerJobOrder/reCreateInnerJobShift")
    ResponseEntity reCreateInnerJobShift(@RequestParam Long jobOrderId, @RequestParam BigDecimal qty);

}
