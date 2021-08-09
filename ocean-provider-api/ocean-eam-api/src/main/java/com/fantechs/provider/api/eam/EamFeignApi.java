package com.fantechs.provider.api.eam;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipment;
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
}
