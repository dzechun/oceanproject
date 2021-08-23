package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaintainOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderService;
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
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@RestController
@Api(tags = "设备保养单")
@RequestMapping("/eamEquipmentMaintainOrder")
@Validated
public class EamEquipmentMaintainOrderController {

    @Resource
    private EamEquipmentMaintainOrderService eamEquipmentMaintainOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainOrderService.save(eamEquipmentMaintainOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentMaintainOrder.update.class) EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainOrderService.update(eamEquipmentMaintainOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentMaintainOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentMaintainOrder  eamEquipmentMaintainOrder = eamEquipmentMaintainOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentMaintainOrder,StringUtils.isEmpty(eamEquipmentMaintainOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentMaintainOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainOrder searchEamEquipmentMaintainOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainOrder.getStartPage(),searchEamEquipmentMaintainOrder.getPageSize());
        List<EamEquipmentMaintainOrderDto> list = eamEquipmentMaintainOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("通过设备条码获取其他信息")
    @GetMapping("/findEamEquOrderDto")
    public ResponseEntity<EamEquMaintainOrderDto> findEamEquOrderDto(@ApiParam(value = "查询对象") @RequestParam String equipmentBarcode){
        HashMap<String, Object> map = new HashMap<>();
        map.put("equipmentBarcode", equipmentBarcode);
        return ControllerUtil.returnDataSuccess(eamEquipmentMaintainOrderService.findListForOrder(map), 1);
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquipmentMaintainOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainOrder searchEamEquipmentMaintainOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainOrder.getStartPage(),searchEamEquipmentMaintainOrder.getPageSize());
        List<EamEquipmentMaintainOrderDto> list = eamEquipmentMaintainOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentMaintainOrder searchEamEquipmentMaintainOrder){
    List<EamEquipmentMaintainOrderDto> list = eamEquipmentMaintainOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备保养单", EamEquipmentMaintainOrderDto.class, "设备保养单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
