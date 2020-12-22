package com.fantechs.provider.api.imes.apply;

import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-imes-apply")
public interface ApplyFeignApi {

    @ApiOperation("订单列表")
    @PostMapping("/smtOrder/findList")
    ResponseEntity<List<SmtOrderDto>> findOrderList(@ApiParam(value = "查询对象")@RequestBody SearchSmtOrder searchSmtOrder);

    @ApiOperation("工单列表")
    @PostMapping("/smtWorkOrder/findList")
    ResponseEntity<List<SmtWorkOrderDto>> findWorkOrderList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder);

    @ApiOperation(value = "新增工单",notes = "新增工单")
    @PostMapping("/smtWorkOrder/add")
    ResponseEntity addWorkOrder(@ApiParam(value = "必传：workOrderCode、materialId、workOrderQuantity、routeId、proLineId",required = true)@RequestBody SmtWorkOrder smtWorkOrder);

    @ApiOperation(value = "产生工单流转卡",notes = "产生工单流转卡")
    @PostMapping("/smtWorkOrderCardCollocation/generateWorkOrderCardCollocation")
    ResponseEntity generateWorkOrderCardCollocation(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderCardCollocation smtWorkOrderCardCollocation);
}
