package com.fantechs.provider.api.mes.pm;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderProcessReWo;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "ocean-mes-pm")
public interface PMFeignApi {

    @ApiOperation("工单列表")
    @PostMapping("/mesPmWorkOrder/findList")
    ResponseEntity<List<MesPmWorkOrderDto>> findWorkOrderList(@ApiParam(value = "查询对象") @RequestBody SearchMesPmWorkOrder searchMesPmWorkOrder);

    @ApiOperation("工单列表")
    @PostMapping("/mesPmWorkOrder/findAll")
    ResponseEntity<List<MesPmWorkOrder>> findWorkOrderAll();

    @ApiOperation("工单列表")
    @PostMapping("/mesPmWorkOrder/getWorkOrderList")
    ResponseEntity<List<MesPmWorkOrder>> getWorkOrderList(@ApiParam(value = "查询对象") @RequestBody List<String> workOrderIds);

    @ApiOperation("修改工单")
    @PostMapping("/mesPmWorkOrder/update")
    ResponseEntity updateSmtWorkOrder(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation("过站修改工单")
    @PostMapping("/mesPmWorkOrder/updatePmWorkOrder")
    ResponseEntity updatePmWorkOrder(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation("批量修改完工数量")
    @PostMapping("/mesPmWorkOrder/batchUpdate")
    ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true) @RequestBody List<MesPmWorkOrder> mesPmWorkOrders);

    @ApiOperation("更新工单状态")
    @PostMapping("/mesPmWorkOrder/updateStatus")
    ResponseEntity updateStatus(
            @ApiParam(value = "工单ID", required = true) @RequestParam Long workOrderID,
            @ApiParam(value = "工单状态", required = true) @RequestParam Integer status);

    @ApiOperation(value = "新增工单", notes = "新增工单")
    @PostMapping("/mesPmWorkOrder/add")
    ResponseEntity addWorkOrder(@ApiParam(value = "必传：workOrderCode、materialId、workOrderQuantity、routeId、proLineId", required = true) @RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation(value = "批量新增工单",notes = "批量新增工单")
    @PostMapping("/mesPmWorkOrder/addList")
    ResponseEntity addList(@ApiParam(value = "工单信息集合") @RequestBody List<MesPmWorkOrder> pmWorkOrders);

    @ApiOperation("工单记录完工数量")
    @GetMapping("/mesPmWorkOrder/finishedProduct")
    ResponseEntity<Integer> finishedProduct(
            @ApiParam(value = "工单ID") @RequestParam Long workOrderId,
            @ApiParam(value = "完工数量") @RequestParam Double count
    );

    @PostMapping("/mesPmWorkOrder/detail")
    ResponseEntity<MesPmWorkOrder> workOrderDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("获取工单工序关系列表")
    @PostMapping("/mesPmWorkOrderProcessReWo/findList")
    ResponseEntity<List<MesPmWorkOrderProcessReWoDto>> findPmWorkOrderProcessReWoList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo);

    @ApiOperation("修改工单入库数量")
    @PostMapping("/mesPmWorkOrder/updateInventory")
    ResponseEntity updateInventory(@RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation("接口修改或保存工单")
    @PostMapping("/mesPmWorkOrder/saveByApi")
    ResponseEntity<MesPmWorkOrder> saveByApi(@RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation("修改工单Bom")
    @PostMapping("/mesPmWorkOrderBom/update")
    ResponseEntity updateMesPmWorkOrderBom(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesPmWorkOrderBom mesPmWorkOrderBom);

    @ApiOperation("添加工单Bom")
    @PostMapping("/mesPmWorkOrderBom/add")
    ResponseEntity addMesPmWorkOrderBom(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesPmWorkOrderBom mesPmWorkOrderBom);

    @ApiOperation("接口批量添加工单Bom")
    @PostMapping("/mesPmWorkOrderBom/batchAdd")
    ResponseEntity addMesPmWorkOrderBom(@ApiParam(value = "对象，Id必传",required = true)@RequestBody List<MesPmWorkOrderBom> mesPmWorkOrderBoms);

    @ApiOperation("工单bom列表")
    @PostMapping("/mesPmWorkOrderBom/findList")
    ResponseEntity<List<MesPmWorkOrderBomDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderBom searchMesPmWorkOrderBom);

    @ApiOperation("产前关键事项确认列表")
    @PostMapping("/mesPmProductionKeyIssuesOrder/findList")
    ResponseEntity<List<MesPmProductionKeyIssuesOrder>> findPmPKIOList(@ApiParam(value = "查询对象") @RequestBody SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder);

}
