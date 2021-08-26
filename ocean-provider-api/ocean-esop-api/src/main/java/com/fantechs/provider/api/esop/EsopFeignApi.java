package com.fantechs.provider.api.esop;

import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.general.entity.esop.EsopIssue;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopEquipment;
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

@FeignClient(name = "ocean-esop")
public interface EsopFeignApi {

    @ApiOperation("查询设备列表")
    @PostMapping("/esopEquipment/findList")
    ResponseEntity<List<EsopEquipmentDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchEsopEquipment searchEsopEquipment);

    @ApiOperation("设备修改")
    @PostMapping("/esopEquipment/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = EsopEquipment.update.class) EsopEquipment esopEquipment);

    @ApiOperation("通过IP获取详情")
    @GetMapping("/esopEquipment/detailByIp")
    ResponseEntity<EsopEquipment> detailByIp(@ApiParam(value = "ip",required = true) @RequestParam @NotNull(message="id不能为空") String ip);

    @ApiOperation("查询设备列表")
    @PostMapping("/esopEquipment/findByMac")
    ResponseEntity<List<EsopEquipmentDto>> findByMac(@RequestParam(value = "mac") Object mac, @RequestParam(value = "orgId") Long orgId);


    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/esopIssue/batchAdd")
    ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody List<EsopIssue> esopIssues);


}
