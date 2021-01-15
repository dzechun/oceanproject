package com.fantechs.provider.api.mes.pm;

import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ocean-mes-pm")
public interface ApplyFeignApi {

    @ApiOperation("订单列表")
    @PostMapping("/smtOrder/findList")
    ResponseEntity<List<SmtOrderDto>> findOrderList(@ApiParam(value = "查询对象")@RequestBody SearchSmtOrder searchSmtOrder);

    @ApiOperation("工单列表")
    @PostMapping("/smtWorkOrder/findList")
    ResponseEntity<List<SmtWorkOrderDto>> findWorkOrderList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder);

    @ApiOperation("更新工单状态")
    @PostMapping("/smtWorkOrder/updateStatus")
    ResponseEntity updateStatus(
            @ApiParam(value = "工单ID",required = true) @RequestParam Long workOrderID,
            @ApiParam(value = "工单状态",required = true) @RequestParam Integer status);

    @ApiOperation(value = "新增工单",notes = "新增工单")
    @PostMapping("/smtWorkOrder/add")
    ResponseEntity addWorkOrder(@ApiParam(value = "必传：workOrderCode、materialId、workOrderQuantity、routeId、proLineId",required = true)@RequestBody SmtWorkOrder smtWorkOrder);

    @ApiOperation(value = "产生工单流转卡",notes = "产生工单流转卡")
    @PostMapping("/smtWorkOrderCardCollocation/generateWorkOrderCardCollocation")
    ResponseEntity generateWorkOrderCardCollocation(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderCardCollocation smtWorkOrderCardCollocation);

    @ApiOperation("生成条码")
    @PostMapping("/smtBarcodeRule/generateCode")
    ResponseEntity<String> generateCode(
            @ApiParam(value = "条码规则集合")@RequestBody List<SmtBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数")@RequestParam String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号")@RequestParam (required = false)String code);

    @ApiOperation("工单记录完工数量")
    @GetMapping("/smtWorkOrder/finishedProduct")
    ResponseEntity<Integer> finishedProduct(
            @ApiParam(value = "工单ID")@RequestParam Long workOrderId,
            @ApiParam(value = "完工数量")@RequestParam Double count
    );
}
