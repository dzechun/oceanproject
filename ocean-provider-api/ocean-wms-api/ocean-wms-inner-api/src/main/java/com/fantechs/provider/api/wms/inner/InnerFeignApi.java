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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ocean-wms-inner")
public interface InnerFeignApi {

    @ApiOperation("储位库存查询")
    @PostMapping("/wmsInnerStorageInventory/findList")
    ResponseEntity<List<WmsInnerStorageInventoryDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerStorageInventory searchSmtStorageInventory);

    @ApiOperation("储位库存新增")
    @PostMapping("/wmsInnerStorageInventory/add")
    ResponseEntity<WmsInnerStorageInventory> add(@ApiParam(value = "必传：", required = true) @RequestBody WmsInnerStorageInventory wmsInnerStorageInventory);

    @ApiOperation("储位库存删除")
    @PostMapping("/wmsInnerStorageInventory/delete")
    ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids);

    @ApiOperation("储位库存更新")
    @PostMapping("/wmsInnerStorageInventory/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody WmsInnerStorageInventory wmsInnerStorageInventory);

    @ApiOperation("扣除储位库存")
    @PostMapping("/wmsInnerStorageInventory/out")
    ResponseEntity<WmsInnerStorageInventory> out(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStorageInventory wmsInnerStorageInventory);

    @ApiOperation("储位库存明细新增")
    @PostMapping("/wmsInnerStorageInventoryDet/add")
    ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody WmsInnerStorageInventoryDet wmsInnerStorageInventoryDet);

    @ApiOperation("储位库存明细列表")
    @PostMapping("/wmsInnerStorageInventoryDet/findList")
    ResponseEntity<List<WmsInnerStorageInventoryDetDto>> findStorageInventoryDetList(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerStorageInventoryDet searchSmtStorageInventoryDet);

    @ApiOperation("储位库存明细修改")
    @PostMapping("/wmsInnerStorageInventoryDet/update")
    ResponseEntity updateStorageInventoryDet(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = WmsInnerStorageInventoryDet.update.class) WmsInnerStorageInventoryDet wmsInnerStorageInventoryDet);

    @ApiOperation("储位库存明细删除")
    @PostMapping("/wmsInnerStorageInventoryDet/delete")
    ResponseEntity deleteStorageInventoryDet(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids);

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


    @ApiOperation(value = "栈板新增上架作业",notes = "栈板新增上架作业")
    @PostMapping("/wmsInnerJobOrder/packageAutoAdd")
    ResponseEntity<WmsInnerJobOrder> packageAutoAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInnerJobOrder);

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
