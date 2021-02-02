package com.fantechs.provider.api.electronic;

import com.fantechs.common.base.electronic.dto.*;
import com.fantechs.common.base.electronic.entity.*;
import com.fantechs.common.base.electronic.entity.search.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
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
    ResponseEntity<List<SmtElectronicTagStorageDto>> findElectronicTagStorageList(@ApiParam(value = "查询对象") @RequestBody SearchSmtElectronicTagStorage searchSmtElectronicTagStorage);

    @ApiOperation("根据客户端id查询电子标签信息")
    @PostMapping("/smtEquipment/findList")
    ResponseEntity<List<SmtEquipmentDto>> findEquipmentList(@ApiParam(value = "查询对象") @RequestBody SearchSmtEquipment searchSmtEquipment);

    @PostMapping("/smtClientManage/update")
    @ApiOperation(value = "更新客户端信息", notes = "更新客户端信息")
    ResponseEntity updateClientManage(@ApiParam(value = "对象，Id必传") @RequestBody SmtClientManage smtClientManage);

    @PostMapping("/smtClientManage/findList")
    @ApiOperation(value = "获取客户端信息", notes = "获取客户端信息")
    ResponseEntity<List<SmtClientManageDto>> findClientManageList(@ApiParam(value = "查询对象") @RequestBody SearchSmtClientManage searchSmtClientManage);

    @PostMapping("/smtSorting/findList")
    @ApiOperation(value = "获取分拣单", notes = "获取分拣单")
    ResponseEntity<List<SmtSortingDto>> findSortingList(@ApiParam(value = "查询对象") @RequestBody SearchSmtSorting searchSmtSorting);

    @PostMapping("/smtSorting/update")
    @ApiOperation(value = "更新分拣单信息", notes = "更新分拣单信息")
    ResponseEntity updateSmtSorting(@ApiParam(value = "对象，Id必传") @RequestBody SmtSorting smtSorting);

    @PostMapping("/smtSorting/batchSave")
    @ApiOperation("批量新增分拣单")
    ResponseEntity batchInsertSmtSorting(@ApiParam(value = "对象，Id必传", required = true) @RequestBody List<SmtSorting> SmtSortings);

    @PostMapping("/smtSorting/batchDelete")
    @ApiOperation("批量删除分拣单")
    ResponseEntity batchDeleteSorting(@ApiParam(value = "sortingCodes", required = true) @RequestBody List<String> sortingCodes);

    @PostMapping("/smtSorting/batchUpdate")
    @ApiOperation("批量修改分拣单")
    ResponseEntity batchUpdateSorting(@ApiParam(value = "对象，Id必传",required = true)@RequestBody  List<SmtSorting> SmtSortings);

    @ApiOperation(value = "新增上料", notes = "新增上料")
    @PostMapping("/smtPaddingMaterial/add")
    ResponseEntity addPaddingMaterial(@ApiParam(value = "必传：paddingMaterialCode、materialCode、quantity") @RequestBody SmtPaddingMaterial smtPaddingMaterial);

    @ApiOperation(value = "新增上料单",notes = "新增上料单")
    @PostMapping("/smtLoading/add")
    ResponseEntity<SmtLoading> addSmtLoading(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtLoading smtLoading);

    @ApiOperation(value = "修改上料单", notes = "修改上料单")
    @PostMapping("/smtLoading/update")
    ResponseEntity updateLoading(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtLoading.update.class) SmtLoading smtLoading);

    @ApiOperation(value = "新增上料单明细",notes = "新增上料单明细")
    @PostMapping("/smtLoadingDet/add")
    ResponseEntity addSmtLoadingDet(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtLoadingDet smtLoadingDet);

    @ApiOperation("修改上料单明细")
    @PostMapping("/smtLoadingDet/update")
    ResponseEntity updateLoadingDet(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= SmtLoadingDet.update.class) SmtLoadingDet smtLoadingDet);

    @ApiOperation("获取详情")
    @PostMapping("/smtLoading/detail")
    ResponseEntity<SmtLoading> detailSmtLoading(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

    @PostMapping("/smtLoading/findList")
    @ApiOperation(value = "获取上料单",notes = "获取上料单")
    ResponseEntity<List<SmtLoading>> findLoadingList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoading searchSmtLoading);

    @ApiOperation("获取详情")
    @PostMapping("/smtLoadingDet/detail")
    ResponseEntity<SmtLoadingDet> detailSmtLoadingDet(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id);

    @ApiOperation("获取上料单明细")
    @PostMapping("/smtLoadingDet/findList")
    ResponseEntity<List<SmtLoadingDetDto>> findLoadingDetList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoadingDet searchSmtLoadingDet);

}
