package com.fantechs.provider.api.eam;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.search.*;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "ocean-eam")
public interface EamFeignApi {

    @ApiOperation("查询设备列表")
    @PostMapping("/eamEquipment/findList")
    ResponseEntity<List<EamEquipmentDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchEamEquipment searchEamEquipment);

    @ApiOperation("设备修改")
    @PostMapping("/eamEquipment/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = EamEquipment.update.class) EamEquipment eamEquipment);

    @ApiOperation("通过IP获取详情")
    @GetMapping("/eamEquipment/detailByIp")
    ResponseEntity<EamEquipment> detailByIp(@ApiParam(value = "ip",required = true) @RequestParam @NotNull(message="id不能为空") String ip);

    @ApiOperation("查询设备列表")
    @PostMapping("/eamEquipment/findByMac")
    ResponseEntity<List<EamEquipmentDto>> findByMac(@RequestParam(value = "mac") Object mac, @RequestParam(value = "orgId") Long orgId);

    @ApiOperation("查询治具条码列表")
    @PostMapping("/eamJigBarcode/findList")
    ResponseEntity<List<EamJigBarcodeDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchEamJigBarcode searchEamJigBarcode);

    @ApiOperation("查询治具绑定产品列表")
    @PostMapping("/eamJigMaterial/findList")
    ResponseEntity<List<EamJigMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaterial searchEamJigMaterial);

    @ApiOperation("治具增加使用次数")
    @PostMapping("/eamJigBarcode/plusCurrentUsageTime")
    ResponseEntity plusCurrentUsageTime(@ApiParam(value = "治具条码id", required = true) @RequestParam @NotNull(message = "治具条码id") Long jigBarcodeId,
                                        @ApiParam(value = "治具使用次数", required = true) @RequestParam @NotNull(message = "治具使用次数不能为空") Integer num);

    @ApiOperation("查询设备绑定产品列表")
    @PostMapping("/eamEquipmentMaterial/findList")
    ResponseEntity<List<EamEquipmentMaterialDto>> findEquipmentMaterialDtoList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaterial searchEamEquipmentMaterial);

    @ApiOperation("获取治具详情")
    @PostMapping("/eamJig/detail")
    ResponseEntity<EamJig> findEamJigDetail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id);

    @ApiOperation("查询设备条码列表")
    @PostMapping("/eamEquipmentBarcode/findList")
    ResponseEntity<List<EamEquipmentBarcode>> findEamEquipmentBarCodeList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentBarcode searchEamEquipmentBarcode);

    @ApiOperation("设备增加使用次数")
    @PostMapping("/eamEquipmentBarcode/plusCurrentUsageTime")
    ResponseEntity plusEamEquiCurrentUsageTime(@ApiParam(value = "设备条码id", required = true) @RequestParam @NotNull(message = "设备条码id") Long equipmentBarCodeId,
                                        @ApiParam(value = "设备使用次数", required = true) @RequestParam @NotNull(message = "设备使用次数不能为空") Integer num);

}
