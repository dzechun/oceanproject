package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentScrapOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentScrapOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentScrapOrderService;
import com.fantechs.provider.eam.service.EamHtEquipmentScrapOrderService;
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
@Api(tags = "设备报废单")
@RequestMapping("/eamEquipmentScrapOrder")
@Validated
public class EamEquipmentScrapOrderController {

    @Resource
    private EamEquipmentScrapOrderService eamEquipmentScrapOrderService;
    @Resource
    private EamHtEquipmentScrapOrderService eamHtEquipmentScrapOrderService;

    @ApiOperation(value = "自动生成报废单",notes = "自动生成报废单")
    @PostMapping("/autoCreateOrder")
    public ResponseEntity autoCreateOrder() {
        return ControllerUtil.returnCRUD(eamEquipmentScrapOrderService.autoCreateOrder());
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentScrapOrderDto eamEquipmentScrapOrderDto) {
        return ControllerUtil.returnCRUD(eamEquipmentScrapOrderService.save(eamEquipmentScrapOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentScrapOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentScrapOrder.update.class) EamEquipmentScrapOrderDto eamEquipmentScrapOrderDto) {
        return ControllerUtil.returnCRUD(eamEquipmentScrapOrderService.update(eamEquipmentScrapOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentScrapOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentScrapOrder  eamEquipmentScrapOrder = eamEquipmentScrapOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentScrapOrder,StringUtils.isEmpty(eamEquipmentScrapOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentScrapOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentScrapOrder searchEamEquipmentScrapOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentScrapOrder.getStartPage(),searchEamEquipmentScrapOrder.getPageSize());
        List<EamEquipmentScrapOrderDto> list = eamEquipmentScrapOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentScrapOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentScrapOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentScrapOrder searchEamEquipmentScrapOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentScrapOrder.getStartPage(),searchEamEquipmentScrapOrder.getPageSize());
        List<EamHtEquipmentScrapOrder> list = eamHtEquipmentScrapOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentScrapOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentScrapOrder searchEamEquipmentScrapOrder){
    List<EamEquipmentScrapOrderDto> list = eamEquipmentScrapOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentScrapOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备报废单", EamEquipmentScrapOrderDto.class, "设备报废单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
