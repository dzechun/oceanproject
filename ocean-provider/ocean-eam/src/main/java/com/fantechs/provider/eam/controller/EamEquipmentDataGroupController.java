package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentDataGroupService;
import com.fantechs.provider.eam.service.EamHtEquipmentDataGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@RestController
@Api(tags = "设备分组")
@RequestMapping("/eamEquipmentDataGroup")
@Validated
public class EamEquipmentDataGroupController {

    @Resource
    private EamEquipmentDataGroupService eamEquipmentDataGroupService;
    @Resource
    private EamHtEquipmentDataGroupService eamHtEquipmentDataGroupService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentDataGroupDto eamEquipmentDataGroupDto) {
        return ControllerUtil.returnCRUD(eamEquipmentDataGroupService.save(eamEquipmentDataGroupDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentDataGroupService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentDataGroup.update.class) EamEquipmentDataGroupDto eamEquipmentDataGroupDto) {
        return ControllerUtil.returnCRUD(eamEquipmentDataGroupService.update(eamEquipmentDataGroupDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentDataGroup> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentDataGroup  eamEquipmentDataGroup = eamEquipmentDataGroupService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentDataGroup,StringUtils.isEmpty(eamEquipmentDataGroup)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentDataGroupDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentDataGroup searchEamEquipmentDataGroup) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentDataGroup.getStartPage(),searchEamEquipmentDataGroup.getPageSize());
        List<EamEquipmentDataGroupDto> list = eamEquipmentDataGroupService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroup));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentDataGroupDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentDataGroup searchEamEquipmentDataGroup) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentDataGroup.getStartPage(),searchEamEquipmentDataGroup.getPageSize());
        List<EamHtEquipmentDataGroupDto> list = eamHtEquipmentDataGroupService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroup));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
