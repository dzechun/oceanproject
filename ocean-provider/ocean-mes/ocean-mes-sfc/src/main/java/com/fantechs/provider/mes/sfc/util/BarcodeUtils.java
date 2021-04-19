package com.fantechs.provider.mes.sfc.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.UpdateProcessDto;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessRecordService;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 条码工具类
 *
 * @author hyc
 * @version 1.0
 * @date 2021/04/09 17:23
 **/
public class BarcodeUtils {

    @Resource
    private static MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private static MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private static MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;
    @Resource
    private static PMFeignApi pmFeignApi;
    @Resource
    private static BasicFeignApi basicFeignApi;

    /**
     * @param barCode   产品条码
     * @param processId 工序ID
     * @param processId 工单ID
     *                  1．传【产品条码、工序ID、工单ID】参数；
     *                  2．系统检查产品条码是否正确，不正确返回错误信息结束；
     *                  3．系统检查条码流程是否正确，不正确返回错误信息结束；
     *                  4．系统检查条码状态是否正确，如是否挂起、维修、完工等状态返回错误信息结束；
     *                  5．系统检查条码工单状态是否正确，如是否挂起、完工、投产数量>=工单数等状态返回错误信息结束；
     *                  6．系统条码检查OK；
     * @return
     */
    public static Boolean checkSN(String barCode, Long processId, Long orderId) throws Exception{

        // 1、判断条码是否正确（是否存在）
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService.findList((SearchMesSfcWorkOrderBarcode) ControllerUtil.dynamicCondition(
                "barcode",barCode
        ));
        if(mesSfcWorkOrderBarcodeDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        if(mesSfcWorkOrderBarcodeDtos.size() > 1){
            throw new BizErrorException(ErrorCodeEnum.PDA40012001);
        }
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        if(mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 3){
            throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus());
        }

        // 2、判断条码流程是否正确（流程表）
        if(processId != null){
            MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .workOrderBarcodeId(mesSfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                    .build());
            if(mesSfcBarcodeProcess != null){
                throw new BizErrorException(ErrorCodeEnum.PDA40012002, barCode);
            }
            if(!processId.equals(mesSfcBarcodeProcess.getProcessId())){
                throw new BizErrorException(ErrorCodeEnum.PDA40012003, mesSfcBarcodeProcess.getBarcode(), mesSfcBarcodeProcess.getNextProcessCode());
            }
        }

        // 3、系统检查条码工单状态是否正确（工单表）
        if(orderId != null){
            ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = pmFeignApi.workOrderDetail(orderId);
            if(pmWorkOrderResponseEntity.getCode() != 0){
                throw new BizErrorException(ErrorCodeEnum.PDA40012005, mesSfcWorkOrderBarcodeDto.getWorkOrderCode());
            }
            MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
            if("4".equals(mesPmWorkOrder.getWorkOrderStatus()) || "5".equals(mesPmWorkOrder.getWorkOrderStatus())){
                throw new BizErrorException(ErrorCodeEnum.PDA40012006);
            }
            if(mesPmWorkOrder.getProductionQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) > -1){
                throw new BizErrorException(ErrorCodeEnum.PDA40012007, mesPmWorkOrder.getWorkOrderCode());
            }
        }
        return true;
    }

    /**
     * 1．传【产品条码、下一工序ID、不良现象代码、作业人员ID、线别ID、设备ID、工艺路线ID】参数，检验结果是OK时不良现象代码写N/A,设备ID为空值时写N/A；
     * 2．更新产品条码当前工序、下一工序；
     * 3．写过站记录；
     * @return
     * @throws Exception
     */
    public static int updateProcess(UpdateProcessDto dto) throws Exception{
        // 获取条码
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderCode(dto.getBarCode())
                .build());
        ResponseEntity<List<BaseRouteProcess>> responseEntity = basicFeignApi.findConfigureRout(dto.getRouteId());
        if(responseEntity.getCode() != 0){
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // 若入参下一工序ID跟过站表下一工序ID不一致
        // 则判断过站表下一工序是否必过工序
        if(dto.getNextProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())){
            Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                    .filter(i -> mesSfcBarcodeProcess.getNextProcessId().equals(i.getProcessId()))
                    .findFirst();
            if(!routeProcessOptional.isPresent()){
                throw new BizErrorException(ErrorCodeEnum.PDA40012009, mesSfcBarcodeProcess.getNextProcessId());
            }
            BaseRouteProcess routeProcess = routeProcessOptional.get();
            if(routeProcess.getIsMustPass() == 1){
                throw new BizErrorException(ErrorCodeEnum.PDA40012010, mesSfcBarcodeProcess.getNextProcessId());
            }
        }
        // 更新当前工序
        mesSfcBarcodeProcess.setProcessId(mesSfcBarcodeProcess.getNextProcessId());
        mesSfcBarcodeProcess.setProcessCode(mesSfcBarcodeProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(mesSfcBarcodeProcess.getProcessName());
        Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                .filter(i -> dto.getNextProcessId().equals(i.getProcessId()))
                .findFirst();
        if(!routeProcessOptional.isPresent()){
            throw new BizErrorException(ErrorCodeEnum.PDA40012011, mesSfcBarcodeProcess.getNextProcessId());
        }
        BaseRouteProcess routeProcess = routeProcessOptional.get();
        ResponseEntity<BaseProcess> processResponseEntity = basicFeignApi.processDetail(routeProcess.getNextProcessId());
        if(processResponseEntity.getCode() != 0){
            throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
        }
        BaseProcess baseProcess = processResponseEntity.getData();
        // 设置下一工序
        mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
        mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcess.getPassStationCount() + 1);
        int update = mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);
        if(update < 1){
            throw new RuntimeException("更新过站表下一工序失败！");
        }
        // 增加过站记录
        MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(mesSfcBarcodeProcessRecord, mesSfcBarcodeProcess);
        mesSfcBarcodeProcessRecord.setOperatorUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(dto.getOperatorUserId());
        return mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);
    }

}
