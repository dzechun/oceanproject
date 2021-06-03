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

    @PostMapping("/smtSorting/findList")
    @ApiOperation(value = "获取分拣单", notes = "获取分拣单")
    ResponseEntity<List<PtlSortingDto>> findSortingList(@ApiParam(value = "查询对象") @RequestBody SearchPtlSorting searchPtlSorting);

    @PostMapping("/smtSorting/update")
    @ApiOperation(value = "更新分拣单信息", notes = "更新分拣单信息")
    ResponseEntity updateSmtSorting(@ApiParam(value = "对象，Id必传") @RequestBody PtlSorting ptlSorting);

    @PostMapping("/smtSorting/batchSave")
    @ApiOperation("批量新增分拣单")
    ResponseEntity batchInsertSmtSorting(@ApiParam(value = "对象，Id必传", required = true) @RequestBody List<PtlSorting> ptlSortings);

    @PostMapping("/smtSorting/batchDelete")
    @ApiOperation("批量删除分拣单")
    ResponseEntity batchDeleteSorting(@ApiParam(value = "sortingCodes", required = true) @RequestBody List<String> sortingCodes);

    @PostMapping("/smtSorting/batchUpdate")
    @ApiOperation("批量修改分拣单")
    ResponseEntity batchUpdateSorting(@ApiParam(value = "对象，Id必传",required = true)@RequestBody  List<PtlSorting> ptlSortings);

    @ApiOperation("修改状态")
    @GetMapping("/smtSorting/updateStatus")
    ResponseEntity updateStatus(
            @ApiParam(value = "任务单",required = true)@RequestParam String sortingCode,
            @ApiParam(value = "操作状态（1-激活 2-完成 3-异常）",required = true)@RequestParam Byte status);

    @ApiOperation(value = "新增上料单",notes = "新增上料单")
    @PostMapping("/smtLoading/add")
    ResponseEntity<PtlLoading> addSmtLoading(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlLoading ptlLoading);

    @ApiOperation(value = "修改上料单", notes = "修改上料单")
    @PostMapping("/smtLoading/update")
    ResponseEntity updateLoading(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlLoading.update.class) PtlLoading ptlLoading);

    @ApiOperation(value = "新增上料单明细",notes = "新增上料单明细")
    @PostMapping("/smtLoadingDet/add")
    ResponseEntity addSmtLoadingDet(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlLoadingDet ptlLoadingDet);

    @ApiOperation("修改上料单明细")
    @PostMapping("/smtLoadingDet/update")
    ResponseEntity updateLoadingDet(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlLoadingDet.update.class) PtlLoadingDet ptlLoadingDet);

    @ApiOperation("获取详情")
    @PostMapping("/smtLoading/detail")
    ResponseEntity<PtlLoading> detailSmtLoading(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

    @PostMapping("/smtLoading/findList")
    @ApiOperation(value = "获取上料单",notes = "获取上料单")
    ResponseEntity<List<PtlLoading>> findLoadingList(@ApiParam(value = "查询对象")@RequestBody SearchPtlLoading searchPtlLoading);

    @ApiOperation("获取详情")
    @PostMapping("/smtLoadingDet/detail")
    ResponseEntity<PtlLoadingDet> detailSmtLoadingDet(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id);

    @ApiOperation("获取上料单明细")
    @PostMapping("/smtLoadingDet/findList")
    ResponseEntity<List<PtlLoadingDetDto>> findLoadingDetList(@ApiParam(value = "查询对象")@RequestBody SearchPtlLoadingDet searchPtlLoadingDet);

}
