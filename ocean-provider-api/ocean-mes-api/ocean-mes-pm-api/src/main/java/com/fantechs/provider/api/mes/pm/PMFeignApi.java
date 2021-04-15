package com.fantechs.provider.api.mes.pm;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.pm.SmtStockDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "ocean-mes-pm")
public interface PMFeignApi {

    @ApiOperation("工单列表")
    @PostMapping("/smtWorkOrder/findList")
    ResponseEntity<List<MesPmWorkOrderDto>> findWorkOrderList(@ApiParam(value = "查询对象") @RequestBody SearchMesPmWorkOrder searchMesPmWorkOrder);

    @ApiOperation("修改工单")
    @PostMapping("/smtWorkOrder/update")
    ResponseEntity updateSmtWorkOrder(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation("更新工单状态")
    @PostMapping("/smtWorkOrder/updateStatus")
    ResponseEntity updateStatus(
            @ApiParam(value = "工单ID", required = true) @RequestParam Long workOrderID,
            @ApiParam(value = "工单状态", required = true) @RequestParam Integer status);

    @ApiOperation(value = "新增工单", notes = "新增工单")
    @PostMapping("/smtWorkOrder/add")
    ResponseEntity addWorkOrder(@ApiParam(value = "必传：workOrderCode、materialId、workOrderQuantity、routeId、proLineId", required = true) @RequestBody MesPmWorkOrder mesPmWorkOrder);

    @ApiOperation(value = "新增及更新工单及BOM",notes = "新增及更新工单及BOM")
    @PostMapping("/smtWorkOrder/save")
    ResponseEntity saveWorkOrder(@ApiParam(value = "保存工单及工单BOM",required = true)@RequestBody SaveWorkOrderAndBom saveWorkOrderAndBom);

    @ApiOperation(value = "产生工单流转卡", notes = "产生工单流转卡")
    @PostMapping("/smtWorkOrderCardCollocation/generateWorkOrderCardCollocation")
    ResponseEntity generateWorkOrderCardCollocation(@ApiParam(value = "必传：", required = true) @RequestBody @Validated SmtWorkOrderCardCollocation smtWorkOrderCardCollocation);

    @ApiOperation("工单记录完工数量")
    @GetMapping("/smtWorkOrder/finishedProduct")
    ResponseEntity<Integer> finishedProduct(
            @ApiParam(value = "工单ID") @RequestParam Long workOrderId,
            @ApiParam(value = "完工数量") @RequestParam Double count
    );

    @ApiOperation("修改条码流转卡")
    @PostMapping("/smtWorkOrderBarcodePool/update")
    ResponseEntity updateSmtWorkOrderBarcodePool(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= SmtWorkOrderBarcodePool.update.class) SmtWorkOrderBarcodePool smtWorkOrderBarcodePool);

    @ApiOperation("查询条码流转卡")
    @PostMapping("/smtWorkOrderBarcodePool/findList")
    ResponseEntity<List<SmtWorkOrderBarcodePoolDto>> findWorkOrderBarcodePoolList(@RequestBody SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool);

    @ApiOperation("修改工单流转卡任务池")
    @PostMapping("/smtWorkOrderCardPool/update")
    ResponseEntity updateSmtWorkOrderCardPool(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= SmtWorkOrderCardPool.update.class) SmtWorkOrderCardPool smtWorkOrderCardPool);

    @ApiOperation("查询工单流转卡任务池列表")
    @PostMapping("/smtWorkOrderCardPool/findList")
    ResponseEntity<List<SmtWorkOrderCardPoolDto>> findSmtWorkOrderCardPoolList(@ApiParam(value = "查询对象") @RequestBody SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);

    @ApiOperation("查询工单流转卡任务池详情")
    @PostMapping("/smtWorkOrderCardPool/detail")
    ResponseEntity<SmtWorkOrderCardPool> findSmtWorkOrderCardPoolDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("修改过站信息")
    @PostMapping("/smtProcessListProcess/update")
    ResponseEntity updateSmtProcessListProcess(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtProcessListProcess.update.class) SmtProcessListProcess smtProcessListProcess);

    @ApiOperation("查询过站信息列表")
    @PostMapping("/smtProcessListProcess/findList")
    ResponseEntity<List<SmtProcessListProcessDto>> findSmtProcessListProcessList(@ApiParam(value = "查询对象") @RequestBody SearchSmtProcessListProcess searchSmtProcessListProcess);

    @ApiOperation("获取工单的详情")
    @PostMapping("/smtWorkOrder/detail")
    ResponseEntity<MesPmWorkOrder> workOrderDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("修改")
    @PostMapping("/smtStock/update")
    ResponseEntity updateSmtStock(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= SmtStock.update.class) SmtStock smtStock);

    @ApiOperation("备料列表")
    @PostMapping("/smtStock/findList")
    ResponseEntity<List<SmtStockDto>> findSmtStockList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStock searchSmtStock);

    @ApiOperation("修改备料详情")
    @PostMapping("/smtStockDet/update")
    ResponseEntity updateSmtStockDet(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated SmtStockDet smtStockDet);

    @ApiOperation("备料详情列表")
    @PostMapping("/smtStockDet/findList")
    ResponseEntity<List<SmtStockDetDto>> findSmtStockDetList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStockDet searchSmtStockDet);

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/smtProcessListProcess/startJob")
    ResponseEntity startJob(@ApiParam(value = "必传：", required = true) @RequestBody SmtWorkOrderBarcodePool smtWorkOrderBarcodePool);

    @ApiOperation("通过流程单获取工单相关信息")
    @PostMapping("/smtWorkOrderCardPool/findWO")
    ResponseEntity<ProcessListWorkOrderDTO> selectWorkOrderDtoByWorkOrderCardId(
            @ApiParam(value = "流程单编码")@RequestParam String workOrderCardId
    );

    @ApiOperation("列表")
    @PostMapping("/smtWorkOrderCardPool/findList")
    ResponseEntity<List<SmtWorkOrderCardPoolDto>> findWorkOrderCardPoolList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);

    @ApiOperation("工单BOM信息列表")
    @PostMapping("/smtWorkOrderBom/findList")
    ResponseEntity<List<SmtWorkOrderBomDto>> findWordOrderBomList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderBom searchSmtWorkOrderBom);

    @ApiOperation("查询总计划表（月计划表）列表")
    @PostMapping("/mesPmMasterPlan/findList")
    ResponseEntity<List<MesPmMasterPlanDTO>> findPmMasterPlanlist(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmMasterPlanListDTO searchMesPmMasterPlanListDTO
    );

    @ApiOperation("获取最小齐套数")
    @PostMapping("/mesPmMatchingOrder/findMinMatchingQuantity")
    ResponseEntity<MesPmMatchingDto> findMinMatchingQuantity(@ApiParam(value = "工单流转卡号")@RequestParam String workOrderCardId, @ApiParam(value = "工段ID")@RequestParam long sectionId,@ApiParam(value = "数量")@RequestParam BigDecimal qualityQuantity,@ApiParam(value = "流转卡ID") @RequestParam(required = false) Long workOrderCardPoolId);

}
