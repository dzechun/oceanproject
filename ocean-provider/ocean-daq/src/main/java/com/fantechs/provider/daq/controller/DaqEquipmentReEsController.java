package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.general.dto.eam.EamEquipmentReEsDto;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentReEs;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentReEs;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentReEsService;
import com.fantechs.provider.daq.service.DaqHtEquipmentReEsService;
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
@Api(tags = "设备工作站关联信息")
@RequestMapping("/daqEquipmentReEs")
@Validated
public class DaqEquipmentReEsController {

    @Resource
    private DaqEquipmentReEsService daqEquipmentReEsService;
    @Resource
    private DaqHtEquipmentReEsService daqHtEquipmentReEsService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentReEs eamEquipmentReEs) {
        return ControllerUtil.returnCRUD(daqEquipmentReEsService.save(eamEquipmentReEs));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentReEsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentReEs.update.class) EamEquipmentReEs eamEquipmentReEs) {
        return ControllerUtil.returnCRUD(daqEquipmentReEsService.update(eamEquipmentReEs));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentReEs> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentReEs  eamEquipmentReEs = daqEquipmentReEsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentReEs,StringUtils.isEmpty(eamEquipmentReEs)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentReEsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentReEs searchEamEquipmentReEs) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentReEs.getStartPage(),searchEamEquipmentReEs.getPageSize());
        List<EamEquipmentReEsDto> list = daqEquipmentReEsService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentReEs));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentReEsDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentReEs searchEamEquipmentReEs) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentReEs.getStartPage(),searchEamEquipmentReEs.getPageSize());
        List<EamHtEquipmentReEsDto> list = daqHtEquipmentReEsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentReEs));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
