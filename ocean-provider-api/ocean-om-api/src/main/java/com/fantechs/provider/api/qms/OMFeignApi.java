package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/9 16:27
 * @Description:
 * @Version: 1.0
 */
@FeignClient(name = "ocean-om")
public interface OMFeignApi {

//    @ApiOperation("订单列表")
//    @PostMapping("/smtOrder/findList")
//    ResponseEntity<List<SmtOrderDto>> findOrderList(@ApiParam(value = "查询对象") @RequestBody SearchSmtOrder searchSmtOrder);

    @ApiOperation("修改订单")
    @PostMapping("/smtOrder/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody SmtOrder smtOrder);

    @ApiOperation("获取订单详情")
    @PostMapping("/smtOrder/detail")
    ResponseEntity<SmtOrder> detailSmtOrder(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

    @ApiOperation("修改订单明细")
    @PostMapping("/omSalesOrderDet/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= OmSalesOrderDet.update.class) OmSalesOrderDet omSalesOrderDet);

    @ApiOperation(value = "新增或更新采购订单表信息",notes = "新增或更新采购订单表信息")
    @PostMapping("/omPurchaseOrder/addOrUpdate")
    ResponseEntity<OmPurchaseOrder> addOrUpdate(@ApiParam(value = "必传:",required = true)@RequestBody OmPurchaseOrder omPurchaseOrder);

    @ApiOperation(value = "新增或更新采购订单详情表信息",notes = "新增或更新采购订单详情表信息")
    @PostMapping("/omPurchaseOrderDet/addOrUpdate")
    ResponseEntity<OmPurchaseOrderDet> addOrUpdate(@ApiParam(value = "必传:",required = true)@RequestBody OmPurchaseOrderDet omPurchaseOrderDet);

    @ApiOperation("修改单据状态")
    @PostMapping("/omTransferOrder/updateStatus")
    ResponseEntity updateStatus(@RequestBody OmTransferOrder omTransferOrder);

    @ApiOperation("反写销退入库订单收货数量")
    @PostMapping("/omSalesReturnOrder/writeQty")
    ResponseEntity writeQty(@RequestBody OmSalesReturnOrderDet omSalesReturnOrderDet);
}
