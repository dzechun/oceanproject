package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentRepairOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentRepairOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentRepairOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentRepairOrderService;
import com.fantechs.provider.eam.service.EamHtEquipmentRepairOrderService;
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
@Api(tags = "设备维修单")
@RequestMapping("/eamEquipmentRepairOrder")
@Validated
public class EamEquipmentRepairOrderController {

    @Resource
    private EamEquipmentRepairOrderService eamEquipmentRepairOrderService;
    @Resource
    private EamHtEquipmentRepairOrderService eamHtEquipmentRepairOrderService;

    @ApiOperation("新建维修单")
    @PostMapping("/pdaCreateOrder")
    public ResponseEntity<EamEquipmentRepairOrderDto> pdaCreateOrder(@ApiParam(value = "设备条码",required = true)@RequestParam  @NotBlank(message="设备条码不能为空") String equipmentBarcode) {
        EamEquipmentRepairOrderDto  eamEquipmentRepairOrderDto = eamEquipmentRepairOrderService.pdaCreateOrder(equipmentBarcode);
        return  ControllerUtil.returnDataSuccess(eamEquipmentRepairOrderDto,StringUtils.isEmpty(eamEquipmentRepairOrderDto)?0:1);
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentRepairOrderDto eamEquipmentRepairOrderDto) {
        return ControllerUtil.returnCRUD(eamEquipmentRepairOrderService.save(eamEquipmentRepairOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentRepairOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentRepairOrder.update.class) EamEquipmentRepairOrderDto eamEquipmentRepairOrderDto) {
        return ControllerUtil.returnCRUD(eamEquipmentRepairOrderService.update(eamEquipmentRepairOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentRepairOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentRepairOrder  eamEquipmentRepairOrder = eamEquipmentRepairOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentRepairOrder,StringUtils.isEmpty(eamEquipmentRepairOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentRepairOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentRepairOrder searchEamEquipmentRepairOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentRepairOrder.getStartPage(),searchEamEquipmentRepairOrder.getPageSize());
        List<EamEquipmentRepairOrderDto> list = eamEquipmentRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentRepairOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentRepairOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentRepairOrder searchEamEquipmentRepairOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentRepairOrder.getStartPage(),searchEamEquipmentRepairOrder.getPageSize());
        List<EamHtEquipmentRepairOrder> list = eamHtEquipmentRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentRepairOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentRepairOrder searchEamEquipmentRepairOrder){
    List<EamEquipmentRepairOrderDto> list = eamEquipmentRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentRepairOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备维修单", EamEquipmentRepairOrderDto.class, "设备维修单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
