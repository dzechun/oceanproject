package com.fantechs.provider.api.electronic;

import com.fantechs.common.base.electronic.dto.*;
import com.fantechs.common.base.electronic.entity.*;
import com.fantechs.common.base.electronic.entity.search.*;
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

/**
 * Created by lfz on 2020/11/24.
 */
@FeignClient(value = "ocean-electronic-tag", contextId = "electronicTag")
public interface ElectronicTagFeignApi {

    @PostMapping(value = "/smtElectronicTagStorage/findList")
    @ApiOperation(value = "获取电子标签控制器和储位信息", notes = "获取电子标签控制器和储位信息")
    ResponseEntity<List<PtlElectronicTagStorageDto>> findElectronicTagStorageList(@ApiParam(value = "查询对象") @RequestBody SearchPtlElectronicTagStorage searchPtlElectronicTagStorage);

    @ApiOperation("根据客户端id查询电子标签信息")
    @PostMapping("/smtEquipment/findList")
    ResponseEntity<List<PtlEquipmentDto>> findEquipmentList(@ApiParam(value = "查询对象") @RequestBody SearchPtlEquipment searchPtlEquipment);

    @PostMapping("/smtClientManage/update")
    @ApiOperation(value = "更新客户端信息", notes = "更新客户端信息")
    ResponseEntity updateClientManage(@ApiParam(value = "对象，Id必传") @RequestBody PtlClientManage ptlClientManage);

    @PostMapping("/smtClientManage/findList")
    @ApiOperation(value = "获取客户端信息", notes = "获取客户端信息")
    ResponseEntity<List<PtlClientManageDto>> findClientManageList(@ApiParam(value = "查询对象") @RequestBody SearchPtlClientManage searchPtlClientManage);

    @PostMapping("/ptlJobOrder/findList")
    @ApiOperation(value = "获取电子标签作业任务单", notes = "获取电子标签作业任务单")
    ResponseEntity<List<PtlJobOrderDto>> findPtlJobOrderList(@ApiParam(value = "查询对象") @RequestBody SearchPtlJobOrder searchPtlJobOrder);

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/ptlJobOrder/add")
    ResponseEntity<PtlJobOrder> addPtlJobOrder(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlJobOrder ptlJobOrder);

    @ApiOperation("修改")
    @PostMapping("/ptlJobOrder/update")
    ResponseEntity updatePtlJobOrder(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=PtlJobOrder.update.class) PtlJobOrder ptlJobOrder);

    @ApiOperation("根据拣货单号修改")
    @PostMapping("/ptlJobOrder/updateByRelatedOrderCode")
    ResponseEntity updateByRelatedOrderCode(@ApiParam(value = "对象，Id必传",required = true)@RequestBody PtlJobOrder ptlJobOrder);

    @ApiOperation("获取详情")
    @PostMapping("/ptlJobOrder/detail")
    ResponseEntity<PtlJobOrder> ptlJobOrderDetail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id);

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/ptlJobOrderDet/add")
    ResponseEntity addPtlJobOrderDet(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlJobOrderDet ptlJobOrderDet);

    @ApiOperation("修改")
    @PostMapping("/ptlJobOrderDet/update")
    ResponseEntity updatePtlJobOrderDet(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=PtlJobOrderDet.update.class) PtlJobOrderDet ptlJobOrderDet);

    @ApiOperation("批量新增")
    @PostMapping("/ptlJobOrderDet/batchSave")
    ResponseEntity batchSavePtlJobOrderDet(@ApiParam(value = "对象列表",required = true)@RequestBody @Validated List<PtlJobOrderDet> ptlJobOrderDetList);

    @ApiOperation("批量修改")
    @PostMapping("/ptlJobOrderDet/batchUpdate")
    ResponseEntity batchUpdatePtlJobOrderDet(@ApiParam(value = "对象列表，Id必传",required = true)@RequestBody @Validated(value= PtlJobOrderDet.update.class) List<PtlJobOrderDet> ptlJobOrderDetList);

    @ApiOperation("根据作业单Id修改")
    @PostMapping("/ptlJobOrderDet/updateByJobOrderId")
    ResponseEntity updateByJobOrderId(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlJobOrderDet.update.class) PtlJobOrderDet ptlJobOrderDet);

    @ApiOperation("列表")
    @PostMapping("/ptlJobOrderDet/findList")
    ResponseEntity<List<PtlJobOrderDetDto>> findPtlJobOrderDetList(@ApiParam(value = "查询对象")@RequestBody SearchPtlJobOrderDet searchPtlJobOrderDet);
}
