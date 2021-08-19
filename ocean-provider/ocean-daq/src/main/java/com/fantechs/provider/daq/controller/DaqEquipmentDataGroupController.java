package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroup;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipmentDataGroup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentDataGroupService;
import com.fantechs.provider.daq.service.DaqHtEquipmentDataGroupService;
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
@Api(tags = "设备组别")
@RequestMapping("/daqEquipmentDataGroup")
@Validated
public class DaqEquipmentDataGroupController {

    @Resource
    private DaqEquipmentDataGroupService daqEquipmentDataGroupService;
    @Resource
    private DaqHtEquipmentDataGroupService daqHtEquipmentDataGroupService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated DaqEquipmentDataGroupDto daqEquipmentDataGroupDto) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupService.save(daqEquipmentDataGroupDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= DaqEquipmentDataGroup.update.class) DaqEquipmentDataGroupDto daqEquipmentDataGroupDto) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupService.update(daqEquipmentDataGroupDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<DaqEquipmentDataGroup> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        DaqEquipmentDataGroup  daqEquipmentDataGroup = daqEquipmentDataGroupService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(daqEquipmentDataGroup,StringUtils.isEmpty(daqEquipmentDataGroup)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<DaqEquipmentDataGroupDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchDaqEquipmentDataGroup searchDaqEquipmentDataGroup) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipmentDataGroup.getStartPage(),searchDaqEquipmentDataGroup.getPageSize());
        List<DaqEquipmentDataGroupDto> list = daqEquipmentDataGroupService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentDataGroup));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<DaqHtEquipmentDataGroupDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchDaqEquipmentDataGroup searchDaqEquipmentDataGroup) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipmentDataGroup.getStartPage(),searchDaqEquipmentDataGroup.getPageSize());
        List<DaqHtEquipmentDataGroupDto> list = daqHtEquipmentDataGroupService.findHtList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentDataGroup));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
