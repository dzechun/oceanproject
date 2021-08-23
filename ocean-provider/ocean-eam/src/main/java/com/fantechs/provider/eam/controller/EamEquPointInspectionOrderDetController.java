package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrderDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquPointInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderDetService;
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
 * Created by leifengzhi on 2021/08/20.
 */
@RestController
@Api(tags = "设备点检单明细")
@RequestMapping("/eamEquPointInspectionOrderDet")
@Validated
public class EamEquPointInspectionOrderDetController {

    @Resource
    private EamEquPointInspectionOrderDetService eamEquPointInspectionOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquPointInspectionOrderDet eamEquPointInspectionOrderDet) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderDetService.save(eamEquPointInspectionOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquPointInspectionOrderDet.update.class) EamEquPointInspectionOrderDet eamEquPointInspectionOrderDet) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderDetService.update(eamEquPointInspectionOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquPointInspectionOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquPointInspectionOrderDet  eamEquPointInspectionOrderDet = eamEquPointInspectionOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquPointInspectionOrderDet,StringUtils.isEmpty(eamEquPointInspectionOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquPointInspectionOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionOrderDet searchEamEquPointInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionOrderDet.getStartPage(),searchEamEquPointInspectionOrderDet.getPageSize());
        List<EamEquPointInspectionOrderDetDto> list = eamEquPointInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquPointInspectionOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionOrderDet searchEamEquPointInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionOrderDet.getStartPage(),searchEamEquPointInspectionOrderDet.getPageSize());
        List<EamEquPointInspectionOrderDetDto> list = eamEquPointInspectionOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquPointInspectionOrderDet searchEamEquPointInspectionOrderDet){
    List<EamEquPointInspectionOrderDetDto> list = eamEquPointInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备点检单明细", EamEquPointInspectionOrderDetDto.class, "设备点检单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
