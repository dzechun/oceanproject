package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.dto.om.*;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.om.search.SearchOmTransferOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

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

    @ApiOperation("新增或更新采购订单表信息")
    @PostMapping("/omPurchaseOrder/saveByApi")
    ResponseEntity<OmPurchaseOrder> saveByApi(@ApiParam(value = "必传:",required = true)@RequestBody OmPurchaseOrder omPurchaseOrder);

    @ApiOperation("新增或更新采购订单详情表信息")
    @PostMapping("/omPurchaseOrderDet/saveByApi")
    ResponseEntity saveByApi(@ApiParam(value = "必传:",required = true)@RequestBody List<OmPurchaseOrderDet> omPurchaseOrderDets);

    @ApiOperation("采购订单列表")
    @PostMapping("/omPurchaseOrder/findList")
    ResponseEntity<List<OmPurchaseOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrder searchOmPurchaseOrder);

    @ApiOperation("获取采购单明细物料ID")
    @PostMapping("/omPurchaseOrder/findPurchaseMaterial")
    ResponseEntity<String> findPurchaseMaterial(@ApiParam(value = "purchaseOrderCode",required = true)@RequestParam String purchaseOrderCode);

    @ApiOperation("采购订单明细列表")
    @PostMapping("/omPurchaseOrderDet/findList")
    ResponseEntity<List<OmPurchaseOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrderDet searchOmPurchaseOrderDet);

    @ApiOperation("修改")
    @PostMapping("/omPurchaseOrderDet/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmPurchaseOrderDet.update.class) OmPurchaseOrderDet omPurchaseOrderDet);

    @ApiOperation("修改单据状态")
    @PostMapping("/omTransferOrder/updateStatus")
    ResponseEntity updateStatus(@RequestBody OmTransferOrder omTransferOrder);

    @ApiOperation("调拨订单明细列表")
    @PostMapping("/omTransferOrderDet/findList")
    ResponseEntity<List<OmTransferOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmTransferOrderDet searchOmTransferOrderDet);

    @ApiOperation("调拨订单明细修改")
    @PostMapping("/omTransferOrderDet/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= OmTransferOrderDet.update.class) OmTransferOrderDet omTransferOrderDet);

    @ApiOperation("反写销退入库订单收货数量")
    @PostMapping("/omSalesReturnOrder/writeQty")
    ResponseEntity writeQty(@RequestBody OmSalesReturnOrderDet omSalesReturnOrderDet);

    @ApiOperation("销售列表")
    @PostMapping("/omSalesOrder/findList")
    ResponseEntity<List<OmSalesOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDto searchOmSalesOrderDto);

    @ApiOperation(value = "销售新增",notes = "新增")
    @PostMapping("/omSalesOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmSalesOrderDto omSalesOrderDto);

    @ApiOperation("销售列表")
    @PostMapping("/omSalesOrder/findAll")
    ResponseEntity<List<OmSalesOrderDto>> findSalesOrderAll();

    @ApiOperation(value = "批量新增销售订单",notes = "批量新增销售订单")
    @PostMapping("/omSalesOrder/addList")
    ResponseEntity addList(@ApiParam(value = "销售订单信息集合") @RequestBody List<OmSalesOrder> salesOrders);

    @ApiOperation("批量修改")
    @PostMapping("/omSalesOrder/batchUpdate")
    ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesOrder.update.class) List<OmSalesOrder> salesOrders);

    @ApiOperation("修改")
    @PostMapping("/omSalesOrder/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesOrder.update.class) OmSalesOrderDto omSalesOrderDto);

    @ApiOperation("其他出库订单数量反写")
    @PostMapping("/omOtherOutOrder/writeQty")
    ResponseEntity writeQtyToOut(@RequestBody OmOtherOutOrderDet omOtherOutOrderDet);

    @ApiOperation("其他入库订单数量反写")
    @PostMapping("/omOtherInOrder/writeQty")
    ResponseEntity writeQtyToIn(@RequestBody OmOtherInOrderDet omOtherInOrderDet);

    @ApiOperation("查询销售编码关联PO列表(不分页)")
    @PostMapping("/omSalesCodeReSpc/findAll")
    ResponseEntity<List<OmSalesCodeReSpcDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchOmSalesCodeReSpc searchOmSalesCodeReSpc);

    @ApiOperation("销售编码关联PO修改")
    @PostMapping("/omSalesCodeReSpc/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesCodeReSpc.update.class) OmSalesCodeReSpc omSalesCodeReSpc);

    @ApiOperation("销售订单明细列表")
    @PostMapping("/omSalesOrderDet/findList")
    ResponseEntity<List<OmSalesOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDetDto searchOmSalesOrderDetDto);

    @ApiOperation(value = "更新采购订单上架数量",notes = "更新采购订单上架数量")
    @PostMapping("/omPurchaseOrder/updatePutawayQty")
    ResponseEntity updatePutawayQty(@ApiParam(value = "必传操作类型",required = true)@RequestParam Byte opType,@ApiParam(value = "必传明细ID",required = true)@RequestParam Long purchaseOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);

    @ApiOperation(value = "更新采购订单下推数量",notes = "更新采购订单下推数量")
    @PostMapping("/omPurchaseOrder/updatePutDownQty")
    ResponseEntity updatePutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long purchaseOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);

    @ApiOperation(value = "更新其他入库订单下推数量",notes = "更新其他入库订单下推数量")
    @PostMapping("/omOtherInOrder/updateOtherInPutDownQty")
    ResponseEntity updateOtherInPutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long otherInOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);

    @ApiOperation(value = "更新其他入库订单实收数量",notes = "更新其他入库订单实收数量")
    @PostMapping("/omOtherInOrder/updateOtherInPutQty")
    ResponseEntity updateOtherInPutQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long otherInOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);


    @ApiOperation(value = "更新销退订单下推数量",notes = "更新销退订单下推数量")
    @PostMapping("/omSalesReturnOrder/updateSalesReturnPutDownQty")
    ResponseEntity updateSalesReturnPutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long salesReturnOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);

    @ApiOperation(value = "更新销退订单下推数量",notes = "更新销退订单下推数量")
    @PostMapping("/omSalesReturnOrder/updateSalesReturnPutQty")
    ResponseEntity updateSalesReturnPutQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long salesReturnOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);

    @ApiOperation(value = "更新调拨订单下推数量",notes = "更新调拨订单下推数量")
    @PostMapping("/omTransferOrderDet/updatePutDownQty")
    ResponseEntity updateTransferOrderPutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long detId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);

    @ApiOperation(value = "更新采退订单下推数量",notes = "更新采退订单下推数量")
    @PostMapping("/omPurchaseReturnOrderDet/updatePutDownQty")
    ResponseEntity updatePurchaseReturnOrderPutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long detId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) ;

    @ApiOperation(value = "更新其他出库订单下推数量",notes = "更新其他出库订单下推数量")
    @PostMapping("/omOtherOutOrderDet/updatePutDownQty")
    ResponseEntity updateOtherOutOrderPutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long detId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) ;

    @ApiOperation(value = "更新销售订单下推数量",notes = "更新销售订单下推数量")
    @PostMapping("/omSalesOrderDet/updatePutDownQty")
    ResponseEntity updateSalesOrderPutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long detId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) ;

    @ApiOperation("其他出库订单拣货数量反写")
    @PostMapping("/omOtherOutOrder/otherUpdatePickingQty")
    ResponseEntity otherUpdatePickingQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long otherOutOrderDetId,
                                    @ApiParam(value = "必传拣货架数量",required = true)@RequestParam BigDecimal actualQty);

    @ApiOperation("采退订单拣货数量反写")
    @PostMapping("/omPurchaseReturnOrder/purchaseUpdatePickingQty")
    ResponseEntity purchaseUpdatePickingQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long purchaseReturnOrderDetId,
                                    @ApiParam(value = "必传拣货架数量",required = true)@RequestParam BigDecimal actualQty);
}
