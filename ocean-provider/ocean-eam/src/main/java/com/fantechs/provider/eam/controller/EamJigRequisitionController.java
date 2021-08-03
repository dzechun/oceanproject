package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigRequisitionService;
import com.fantechs.provider.eam.service.EamJigRequisitionService;
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
 * Created by leifengzhi on 2021/07/30.
 */
@RestController
@Api(tags = "治具领用")
@RequestMapping("/eamJigRequisition")
@Validated
public class EamJigRequisitionController {

    @Resource
    private EamJigRequisitionService eamJigRequisitionService;
    @Resource
    private EamHtJigRequisitionService eamHtJigRequisitionService;

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<EamJigRequisition> list) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.batchSave(list));
    }

    @ApiOperation(value = "查询工单信息",notes = "查询工单信息")
    @PostMapping("/findWorkOrder")
    public ResponseEntity findWorkOrder(@ApiParam(value = "工单号",required = true) @RequestParam @NotBlank(message="工单号不能为空") String workOrderCode) {
        EamJigRequisitionWorkOrderDto workOrderDto = eamJigRequisitionService.findWorkOrder(workOrderCode);
        return ControllerUtil.returnDataSuccess(workOrderDto,StringUtils.isEmpty(workOrderDto)?0:1);
    }

    @ApiOperation(value = "检查治具条码",notes = "检查治具条码")
    @PostMapping("/checkJigBarcode")
    public ResponseEntity checkJigBarcode(@ApiParam(value = "治具条码",required = true) @RequestParam @NotBlank(message="治具条码不能为空") String jigBarcode,
                                          @ApiParam(value = "治具ID",required = true) @RequestParam @NotNull(message="治具ID不能为空") Long jigId,
                                          @ApiParam(value = "所需数量",required = true) @RequestParam @NotNull(message="所需数量不能为空") Integer usageQty,
                                          @ApiParam(value = "已扫描数量",required = true) @RequestParam @NotNull(message="已扫描数量不能为空") Integer count) {
        EamJigBarcode eamJigBarcode = eamJigRequisitionService.checkJigBarcode(jigBarcode, jigId, usageQty, count);
        return ControllerUtil.returnDataSuccess(eamJigBarcode,StringUtils.isEmpty(eamJigBarcode)?0:1);
    }

    @ApiOperation(value = "转换工单-获取旧工单已领用数量",notes = "转换工单-获取旧工单已领用数量")
    @PostMapping("/getRecordQty")
    public ResponseEntity getRecordQty(@ApiParam(value = "新工单号",required = true) @RequestParam @NotBlank(message="新工单号不能为空") String newWorkOrderCode,
                                                   @ApiParam(value = "旧工单号",required = true) @RequestParam @NotBlank(message="旧工单号不能为空") String oldWorkOrderCode) {
        List<EamJigReMaterialDto> list = eamJigRequisitionService.getRecordQty(newWorkOrderCode,oldWorkOrderCode);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigRequisition eamJigRequisition) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.save(eamJigRequisition));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigRequisition.update.class) EamJigRequisition eamJigRequisition) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.update(eamJigRequisition));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigRequisition> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigRequisition  eamJigRequisition = eamJigRequisitionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigRequisition,StringUtils.isEmpty(eamJigRequisition)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigRequisitionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigRequisition searchEamJigRequisition) {
        Page<Object> page = PageHelper.startPage(searchEamJigRequisition.getStartPage(),searchEamJigRequisition.getPageSize());
        List<EamJigRequisitionDto> list = eamJigRequisitionService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigRequisition>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigRequisition searchEamJigRequisition) {
        Page<Object> page = PageHelper.startPage(searchEamJigRequisition.getStartPage(),searchEamJigRequisition.getPageSize());
        List<EamHtJigRequisition> list = eamHtJigRequisitionService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigRequisition searchEamJigRequisition){
    List<EamJigRequisitionDto> list = eamJigRequisitionService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具领用", EamJigRequisitionDto.class, "治具领用.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
