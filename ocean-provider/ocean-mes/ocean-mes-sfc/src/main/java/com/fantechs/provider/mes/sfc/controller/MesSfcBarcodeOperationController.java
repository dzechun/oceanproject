package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.UpdateProcessDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 生产管理-条码作业控制器
 * @author hyc
 * @date 2021-04-09 15:29:27
 */
@RestController
@Api(tags = "生产管理-条码作业控制器")
@RequestMapping("/mesSfcBarcodeOperation")
@Validated
public class MesSfcBarcodeOperationController {

    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private OMFeignApi omFeignApi;

    @ApiOperation("PDA投产作业")
    @PostMapping("/pdaPutIntoProduction")
    public ResponseEntity pdaPutIntoProduction(@ApiParam(value = "条码",required = true) @RequestParam @NotNull(message="barCode不能为空") String barCode,
                                     @ApiParam(value = "工序ID",required = true) @RequestParam @NotNull(message="processId不能为空") Long processId,
                                     @ApiParam(value = "工单ID",required = true) @RequestParam @NotNull(message="orderId不能为空") Long orderId,
                                     @ApiParam(value = "设备ID",required = true) @RequestParam @NotNull(message="equipmentId不能为空") String equipmentId,
                                     @ApiParam(value = "操作人员ID",required = true) @RequestParam @NotNull(message="operatorUserId不能为空") Long operatorUserId){
        try {
            // 1、检查条码、工单
            BarcodeUtils.checkSN(barCode, processId, orderId);
            // TODO：2021-04-14
            //  2、产前装备项判断，若ok则更新条码状态

            ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = pmFeignApi.workOrderDetail(orderId);
            MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
            UpdateProcessDto updateProcessDto = UpdateProcessDto.builder()
                    .badnessPhenotypeCode("N/A")
                    .barCode(barCode)
                    .equipmentId(equipmentId)
                    .operatorUserId(operatorUserId)
                    .proLineId(mesPmWorkOrder.getProLineId())
                    .routeId(mesPmWorkOrder.getRouteId())
                    .build();
            // 3、判断首条码，若是则更新工单状态
            if(mesPmWorkOrder.getWorkOrderStatus().equals("0") || mesPmWorkOrder.getWorkOrderStatus().equals("1")){
                mesPmWorkOrder.setWorkOrderStatus(Byte.valueOf("2"));
                pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
                updateProcessDto.setNextProcessId(processId);
            }else {
                MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                        .workOrderCode(barCode)
                        .build());
                updateProcessDto.setNextProcessId(mesSfcBarcodeProcess.getNextProcessId());
            }
            // 4、更新下一工序，增加工序记录
            return ControllerUtil.returnCRUD(BarcodeUtils.updateProcess(updateProcessDto));
        }catch (Exception ex){
            throw new BizErrorException(ex);
        }
    }

    @ApiOperation("PDA包箱作业前检测")
    @PostMapping("/pdaPackingOperation")
    public ResponseEntity pdaCheck(@ApiParam(value = "条码",required = true) @RequestParam @NotNull(message="barCode不能为空") String barCode,
                                   @ApiParam(value = "工单ID",required = true) @RequestParam @NotNull(message="orderId不能为空") Long orderId) throws Exception {
        try {
            // 1、检查条码、工单状态
            BarcodeUtils.checkSN(barCode, null, orderId);
            // 2、检查排产单
            // 查询排产单详情
            // 查询排产单

            return null;
        }catch (Exception ex){
            throw new BizErrorException();
        }
    }

}
