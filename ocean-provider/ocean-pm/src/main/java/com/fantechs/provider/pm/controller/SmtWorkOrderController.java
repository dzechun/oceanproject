package com.fantechs.provider.pm.controller;

import com.fantechs.common.base.dto.apply.SaveWorkOrderAndBom;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.pm.service.SmtHtWorkOrderService;
import com.fantechs.provider.pm.service.SmtWorkOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/13.
 */
@RestController
@Api(tags = "工单管理")
@RequestMapping("/smtWorkOrder")
@Validated
public class SmtWorkOrderController {

    @Autowired
    private SmtWorkOrderService smtWorkOrderService;
    @Autowired
    private SmtHtWorkOrderService smtHtWorkOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId、workOrderQuantity、proLineId、routeId",required = true)@RequestBody @Validated SmtWorkOrder smtWorkOrder) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.save(smtWorkOrder));
    }

    @ApiOperation(value = "新增及更新工单及BOM",notes = "新增及更新工单及BOM")
    @PostMapping("/save")
    public ResponseEntity save(@ApiParam(value = "保存工单及工单BOM",required = true)@RequestBody SaveWorkOrderAndBom saveWorkOrderAndBom) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.saveWorkOrderDTO(saveWorkOrderAndBom));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtWorkOrder smtWorkOrder) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.update(smtWorkOrder));
    }

    @ApiOperation("更新工单状态")
    @PostMapping("/updateStatus")
    public ResponseEntity updateStatus(
            @ApiParam(value = "工单ID",required = true) @RequestParam Long workOrderID,
            @ApiParam(value = "工单状态",required = true) @RequestParam Integer status
                                       ) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.updateWorkOrderStatus(workOrderID,status));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrder,StringUtils.isEmpty(smtWorkOrder)?0:1);
    }

    @ApiOperation("工单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrder.getStartPage(),searchSmtWorkOrder.getPageSize());
        List<SmtWorkOrderDto> list = smtWorkOrderService.findList(searchSmtWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工单历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtWorkOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrder.getStartPage(),searchSmtWorkOrder.getPageSize());
        List<SmtHtWorkOrder> list = smtHtWorkOrderService.findList(searchSmtWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工单记录完工数量")
    @GetMapping("finishedProduct")
    public ResponseEntity<Integer> finishedProduct(
            @ApiParam(value = "工单ID")@RequestParam Long workOrderId,
            @ApiParam(value = "完工数量")@RequestParam Double count
    ){
        return ControllerUtil.returnCRUD(smtWorkOrderService.finishedProduct(workOrderId, count));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtWorkOrder searchSmtWorkOrder){
    List<SmtWorkOrderDto> list = smtWorkOrderService.findList(searchSmtWorkOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工单信息", "工单信息", SmtWorkOrderDto.class, "工单信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
