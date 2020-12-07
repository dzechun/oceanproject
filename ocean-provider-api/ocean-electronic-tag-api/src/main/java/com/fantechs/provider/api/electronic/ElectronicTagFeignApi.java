package com.fantechs.provider.api.electronic;

import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtEquipment;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by lfz on 2020/11/24.
 */
@FeignClient(value = "ocean-electronic-tag")
public interface ElectronicTagFeignApi {

    @PostMapping(value="/smtElectronicTagController/findList",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取所有电子标签控制器",notes = "获取所有电子标签控制器")
    ResponseEntity<List<SmtElectronicTagControllerDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtElectronicTagController searchSmtElectronicTagController);

    @PostMapping(value="/smtElectronicTagController/findList")
    @ApiOperation(value = "获取电子标签控制器和储位信息",notes = "获取电子标签控制器和储位信息")
    ResponseEntity<List<SmtElectronicTagStorageDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtElectronicTagStorage searchSmtElectronicTagStorage);

    @ApiOperation("通过id查询电子标签信息")
    @GetMapping("/smtElectronicTagController/findById")
    ResponseEntity<SmtElectronicTagControllerDto> findById(@ApiParam(value = "电子标签控制器id")@RequestParam(value="electronicTagControllerId") String electronicTagControllerId);

    @ApiOperation("根据客户端id查询电子标签信息")
    @PostMapping("/smtEquipment/findList")
    ResponseEntity<List<SmtEquipmentDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtEquipment searchSmtEquipment);
}
