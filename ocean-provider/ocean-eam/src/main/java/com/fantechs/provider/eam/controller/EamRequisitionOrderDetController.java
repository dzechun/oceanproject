package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrderDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamRequisitionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtRequisitionOrderDetService;
import com.fantechs.provider.eam.service.EamRequisitionOrderDetService;
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
 * Created by leifengzhi on 2021/06/29.
 */
@RestController
@Api(tags = "设备领用单明细")
@RequestMapping("/eamRequisitionOrderDet")
@Validated
public class EamRequisitionOrderDetController {

    @Resource
    private EamRequisitionOrderDetService eamRequisitionOrderDetService;
    @Resource
    private EamHtRequisitionOrderDetService eamHtRequisitionOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamRequisitionOrderDet eamRequisitionOrderDet) {
        return ControllerUtil.returnCRUD(eamRequisitionOrderDetService.save(eamRequisitionOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamRequisitionOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamRequisitionOrderDet.update.class) EamRequisitionOrderDet eamRequisitionOrderDet) {
        return ControllerUtil.returnCRUD(eamRequisitionOrderDetService.update(eamRequisitionOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamRequisitionOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamRequisitionOrderDet  eamRequisitionOrderDet = eamRequisitionOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamRequisitionOrderDet,StringUtils.isEmpty(eamRequisitionOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamRequisitionOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamRequisitionOrderDet searchEamRequisitionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamRequisitionOrderDet.getStartPage(),searchEamRequisitionOrderDet.getPageSize());
        List<EamRequisitionOrderDetDto> list = eamRequisitionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamRequisitionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtRequisitionOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamRequisitionOrderDet searchEamRequisitionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamRequisitionOrderDet.getStartPage(),searchEamRequisitionOrderDet.getPageSize());
        List<EamHtRequisitionOrderDet> list = eamHtRequisitionOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamRequisitionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamRequisitionOrderDet searchEamRequisitionOrderDet){
    List<EamRequisitionOrderDetDto> list = eamRequisitionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamRequisitionOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备领用单明细", EamRequisitionOrderDetDto.class, "设备领用单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
