package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrderDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaintainOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderDetService;
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
 * Created by leifengzhi on 2021/08/21.
 */
@RestController
@Api(tags = "eamEquipmentMaintainOrderDet控制器")
@RequestMapping("/eamEquipmentMaintainOrderDet")
@Validated
public class EamEquipmentMaintainOrderDetController {

    @Resource
    private EamEquipmentMaintainOrderDetService eamEquipmentMaintainOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentMaintainOrderDet eamEquipmentMaintainOrderDet) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainOrderDetService.save(eamEquipmentMaintainOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentMaintainOrderDet.update.class) EamEquipmentMaintainOrderDet eamEquipmentMaintainOrderDet) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainOrderDetService.update(eamEquipmentMaintainOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentMaintainOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentMaintainOrderDet  eamEquipmentMaintainOrderDet = eamEquipmentMaintainOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentMaintainOrderDet,StringUtils.isEmpty(eamEquipmentMaintainOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentMaintainOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainOrderDet searchEamEquipmentMaintainOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainOrderDet.getStartPage(),searchEamEquipmentMaintainOrderDet.getPageSize());
        List<EamEquipmentMaintainOrderDetDto> list = eamEquipmentMaintainOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquipmentMaintainOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainOrderDet searchEamEquipmentMaintainOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainOrderDet.getStartPage(),searchEamEquipmentMaintainOrderDet.getPageSize());
        List<EamEquipmentMaintainOrderDetDto> list = eamEquipmentMaintainOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentMaintainOrderDet searchEamEquipmentMaintainOrderDet){
    List<EamEquipmentMaintainOrderDetDto> list = eamEquipmentMaintainOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EamEquipmentMaintainOrderDet信息", EamEquipmentMaintainOrderDetDto.class, "EamEquipmentMaintainOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
