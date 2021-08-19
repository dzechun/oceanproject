package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupParam;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroupParam;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentDataGroupParamService;
import com.fantechs.provider.daq.service.DaqHtEquipmentDataGroupParamService;
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
 * Created by leifengzhi on 2021/08/02.
 */
@RestController
@Api(tags = "设备组别参数")
@RequestMapping("/daqEquipmentDataGroupParam")
@Validated
public class DaqEquipmentDataGroupParamController {

    @Resource
    private DaqEquipmentDataGroupParamService daqEquipmentDataGroupParamService;
    @Resource
    private DaqHtEquipmentDataGroupParamService daqHtEquipmentDataGroupParamService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentDataGroupParam eamEquipmentDataGroupParam) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupParamService.save(eamEquipmentDataGroupParam));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupParamService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentDataGroupParam.update.class) EamEquipmentDataGroupParam eamEquipmentDataGroupParam) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupParamService.update(eamEquipmentDataGroupParam));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentDataGroupParam> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentDataGroupParam  eamEquipmentDataGroupParam = daqEquipmentDataGroupParamService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentDataGroupParam,StringUtils.isEmpty(eamEquipmentDataGroupParam)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentDataGroupParamDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentDataGroupParam searchEamEquipmentDataGroupParam) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentDataGroupParam.getStartPage(),searchEamEquipmentDataGroupParam.getPageSize());
        List<EamEquipmentDataGroupParamDto> list = daqEquipmentDataGroupParamService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroupParam));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentDataGroupParamDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentDataGroupParam searchEamEquipmentDataGroupParam) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentDataGroupParam.getStartPage(),searchEamEquipmentDataGroupParam.getPageSize());
        List<EamHtEquipmentDataGroupParamDto> list = daqHtEquipmentDataGroupParamService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroupParam));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
