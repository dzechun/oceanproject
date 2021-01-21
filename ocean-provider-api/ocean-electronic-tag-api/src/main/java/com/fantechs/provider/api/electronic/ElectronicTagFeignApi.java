package com.fantechs.provider.api.electronic;

import com.fantechs.common.base.electronic.dto.SmtClientManageDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtEquipment;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
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

    @ApiOperation(value = "新增上料", notes = "新增上料")
    @PostMapping("/smtPaddingMaterial/add")
    ResponseEntity addPaddingMaterial(@ApiParam(value = "必传：paddingMaterialCode、materialCode、quantity") @RequestBody SmtPaddingMaterial smtPaddingMaterial);

}
