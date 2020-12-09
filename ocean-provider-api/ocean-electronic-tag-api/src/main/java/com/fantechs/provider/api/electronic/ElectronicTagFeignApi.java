package com.fantechs.provider.api.electronic;

import com.fantechs.common.base.electronic.dto.SmtClientManageDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtEquipment;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by lfz on 2020/11/24.
 */
@FeignClient(value ="ocean-electronic-tag",contextId = "electronicTag")
public interface ElectronicTagFeignApi {
    @PostMapping(value="/smtElectronicTagStorage/findList")
    @ApiOperation(value = "获取电子标签控制器和储位信息",notes = "获取电子标签控制器和储位信息")
    ResponseEntity<List<SmtElectronicTagStorageDto>> findElectronicTagStorageList(@ApiParam(value = "查询对象") @RequestBody SearchSmtElectronicTagStorage searchSmtElectronicTagStorage);

    @ApiOperation("根据客户端id查询电子标签信息")
    @PostMapping("/smtEquipment/findList")
    ResponseEntity<List<SmtEquipmentDto>> findEquipmentList(@ApiParam(value = "查询对象") @RequestBody SearchSmtEquipment searchSmtEquipment);
    @PostMapping("/smtClientManage/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传")@RequestBody SmtClientManage smtClientManage);

    @PostMapping("/smtClientManage/findList")
    ResponseEntity<List<SmtClientManageDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtClientManage searchSmtClientManage);


    @PostMapping("/smtClientManage/update")
    ResponseEntity updateClientManage(@ApiParam(value = "对象，Id必传")@RequestBody SmtClientManage smtClientManage);

    @PostMapping("/smtClientManage/findList")
    ResponseEntity<List<SmtClientManageDto>> findClientManageList(@ApiParam(value = "查询对象")@RequestBody SearchSmtClientManage searchSmtClientManage);

}
