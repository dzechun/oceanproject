package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderPrintParam;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcRepairOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrder;
import com.fantechs.common.base.general.entity.mes.sfc.history.MesSfcHtRepairOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcRepairOrderService;
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
 * Created by leifengzhi on 2021/09/10.
 */
@RestController
@Api(tags = "产品维修单")
@RequestMapping("/mesSfcRepairOrder")
@Validated
public class MesSfcRepairOrderController {

    @Resource
    private MesSfcRepairOrderService mesSfcRepairOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity<MesSfcRepairOrder> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcRepairOrder mesSfcRepairOrder) {
        MesSfcRepairOrder repairOrder = mesSfcRepairOrderService.add(mesSfcRepairOrder);
        return ControllerUtil.returnDataSuccess(repairOrder,StringUtils.isEmpty(repairOrder)?0:1);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcRepairOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcRepairOrder.update.class) MesSfcRepairOrder mesSfcRepairOrder) {
        return ControllerUtil.returnCRUD(mesSfcRepairOrderService.update(mesSfcRepairOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcRepairOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcRepairOrder  mesSfcRepairOrder = mesSfcRepairOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcRepairOrder,StringUtils.isEmpty(mesSfcRepairOrder)?0:1);
    }

    @ApiOperation(value = "打印",notes = "打印")
    @PostMapping("/print")
    public ResponseEntity print(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcRepairOrderPrintParam mesSfcRepairOrderPrintParam) {
        return ControllerUtil.returnCRUD(mesSfcRepairOrderService.print(mesSfcRepairOrderPrintParam));
    }

    @ApiOperation("获取工单")
    @PostMapping("/getWorkOrder")
    public ResponseEntity<MesSfcRepairOrderDto> getWorkOrder(@ApiParam(value = "序列号")@RequestParam String SNCode,
                                                          @ApiParam(value = "工单号")@RequestParam String workOrderCode,
                                                          @ApiParam(value = "序列号类别（1-成品序列号 2-半成品序列号）")@RequestParam Integer SNCodeType) {
        MesSfcRepairOrderDto repairOrderDto = mesSfcRepairOrderService.getWorkOrder(SNCode, workOrderCode,SNCodeType);
        return  ControllerUtil.returnDataSuccess(repairOrderDto,StringUtils.isEmpty(repairOrderDto)?0:1);
    }

    @ApiOperation("查询半成品bom列表")
    @PostMapping("/findSemiProductBom")
    public ResponseEntity<List<BaseProductBomDetDto>> findSemiProductBom(@ApiParam(value = "序列号")@RequestParam String semiProductBarcode) {
        List<BaseProductBomDetDto> semiProductBom = mesSfcRepairOrderService.findSemiProductBom(semiProductBarcode);
        return ControllerUtil.returnDataSuccess(semiProductBom,semiProductBom.size());
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcRepairOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcRepairOrder searchMesSfcRepairOrder) {
        Page<Object> page = PageHelper.startPage(searchMesSfcRepairOrder.getStartPage(),searchMesSfcRepairOrder.getPageSize());
        List<MesSfcRepairOrderDto> list = mesSfcRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcRepairOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesSfcHtRepairOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcRepairOrder searchMesSfcRepairOrder) {
        Page<Object> page = PageHelper.startPage(searchMesSfcRepairOrder.getStartPage(),searchMesSfcRepairOrder.getPageSize());
        List<MesSfcHtRepairOrder> list = mesSfcRepairOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesSfcRepairOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcRepairOrder searchMesSfcRepairOrder){
    List<MesSfcRepairOrderDto> list = mesSfcRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcRepairOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "产品维修单", MesSfcRepairOrderDto.class, "产品维修单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
