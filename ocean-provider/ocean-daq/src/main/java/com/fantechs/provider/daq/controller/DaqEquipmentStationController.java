package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStationDto;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentStationDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStation;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentStation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentStationService;
import com.fantechs.provider.daq.service.DaqHtEquipmentStationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */
@RestController
@Api(tags = "设备工作站")
@RequestMapping("/daqEquipmentStation")
@Validated
public class DaqEquipmentStationController {

    @Resource
    private DaqEquipmentStationService daqEquipmentStationService;
    @Resource
    private DaqHtEquipmentStationService daqHtEquipmentStationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentStationDto eamEquipmentStationDto) {
        return ControllerUtil.returnCRUD(daqEquipmentStationService.save(eamEquipmentStationDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentStationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentStationDto.update.class) EamEquipmentStationDto eamEquipmentStationDto) {
        return ControllerUtil.returnCRUD(daqEquipmentStationService.update(eamEquipmentStationDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentStation> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentStation  eamEquipmentStation = daqEquipmentStationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentStation,StringUtils.isEmpty(eamEquipmentStation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentStationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentStation searchEamEquipmentStation) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentStation.getStartPage(),searchEamEquipmentStation.getPageSize());
        List<EamEquipmentStationDto> list = daqEquipmentStationService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentStation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentStationDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentStation searchEamEquipmentStation) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentStation.getStartPage(),searchEamEquipmentStation.getPageSize());
        List<EamHtEquipmentStationDto> list = daqHtEquipmentStationService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentStation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
