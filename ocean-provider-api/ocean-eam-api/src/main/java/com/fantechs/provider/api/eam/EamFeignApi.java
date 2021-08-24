package com.fantechs.provider.api.eam;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipment;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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


    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/eamIssue/batchAdd")
    ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody List<EamIssue> eamIssues);

    @ApiOperation("查询设备绑定产品列表")
    @PostMapping("/eamEquipmentMaterial/findList")
    ResponseEntity<List<EamEquipmentMaterialDto>> findEquipmentMaterialDtoList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaterial searchEamEquipmentMaterial);

    @ApiOperation("获取治具详情")
    @PostMapping("/eamJig/detail")
    ResponseEntity<EamJig> findEamJigDetail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id);

}
