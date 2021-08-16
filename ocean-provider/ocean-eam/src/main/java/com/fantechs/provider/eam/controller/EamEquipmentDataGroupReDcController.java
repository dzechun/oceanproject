package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupReDc;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroupReDc;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentDataGroupReDcService;
import com.fantechs.provider.eam.service.EamHtEquipmentDataGroupReDcService;
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
@RequestMapping("/eamEquipmentDataGroupReDc")
@Validated
public class EamEquipmentDataGroupReDcController {

    @Resource
    private EamEquipmentDataGroupReDcService eamEquipmentDataGroupReDcService;
    @Resource
    private EamHtEquipmentDataGroupReDcService eamHtEquipmentDataGroupReDcService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentDataGroupReDc eamEquipmentDataGroupReDc) {
        return ControllerUtil.returnCRUD(eamEquipmentDataGroupReDcService.save(eamEquipmentDataGroupReDc));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentDataGroupReDcService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentDataGroupReDc.update.class) EamEquipmentDataGroupReDc eamEquipmentDataGroupReDc) {
        return ControllerUtil.returnCRUD(eamEquipmentDataGroupReDcService.update(eamEquipmentDataGroupReDc));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentDataGroupReDc> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentDataGroupReDc  eamEquipmentDataGroupReDc = eamEquipmentDataGroupReDcService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentDataGroupReDc,StringUtils.isEmpty(eamEquipmentDataGroupReDc)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentDataGroupReDcDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentDataGroupReDc searchEamEquipmentDataGroupReDc) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentDataGroupReDc.getStartPage(),searchEamEquipmentDataGroupReDc.getPageSize());
        List<EamEquipmentDataGroupReDcDto> list = eamEquipmentDataGroupReDcService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroupReDc));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentDataGroupReDcDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentDataGroupReDc searchEamEquipmentDataGroupReDc) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentDataGroupReDc.getStartPage(),searchEamEquipmentDataGroupReDc.getPageSize());
        List<EamHtEquipmentDataGroupReDcDto> list = eamHtEquipmentDataGroupReDcService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroupReDc));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentDataGroupReDc searchEamEquipmentDataGroupReDc){
    List<EamEquipmentDataGroupReDcDto> list = eamEquipmentDataGroupReDcService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroupReDc));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EamEquipmentDataGroupReDc信息", EamEquipmentDataGroupReDcDto.class, "EamEquipmentDataGroupReDc.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("批量添加")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "对象，equipmentDataGroupId必传",required = true)@RequestBody @Validated List<EamEquipmentDataGroupReDc> eamEquipmentDataGroupReDcs){
        return  ControllerUtil.returnCRUD(eamEquipmentDataGroupReDcService.batchAdd(eamEquipmentDataGroupReDcs));
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，equipmentDataGroupId必传",required = true)@RequestBody @Validated List<EamEquipmentDataGroupReDc> eamEquipmentDataGroupReDcs){
        return  ControllerUtil.returnCRUD(eamEquipmentDataGroupReDcService.batchUpdate(eamEquipmentDataGroupReDcs));
    }
}
