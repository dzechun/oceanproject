package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产管理-条码作业控制器
 *
 * @author 邓新堃
 * @date 2021-04-15
 */
@RestController
@Api(tags = "生产管理-条码作业控制器")
@RequestMapping("/mesSfcBarcodeOperation")
@Validated
public class MesSfcBarcodeOperationController {

    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    private PMFeignApi pmFeignApi;
//    @Resource
//    private BaseFeignApi baseFeignApi;
//    @Resource
//    private RedisUtil redisUtil;

    @ApiOperation("PDA投产作业")
    @PostMapping("/pdaPutIntoProduction")
    public ResponseEntity pdaPutIntoProduction(@ApiParam(value = "PDA作业对象") @RequestBody PdaPutIntoProductionVo vo) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        try {
            // 1、检查条码、工单
            BarcodeUtils.checkSN(CheckProductionVo.builder()
                    .barCode(vo.getBarCode())
                    .processId(vo.getProcessId())
                    .stationId(vo.getStationId())
                    .checkOrNot(vo.getCheckOrNot())
                    .build());
            // TODO：2021-04-14
            //  2、产前装备项判断，若ok则更新条码状态

            ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = pmFeignApi.workOrderDetail(vo.getOrderId());
            MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
            UpdateProcessDto updateProcessDto = UpdateProcessDto.builder()
                    .badnessPhenotypeCode("N/A")
                    .barCode(vo.getBarCode())
                    .equipmentId(vo.getEquipmentId())
                    .operatorUserId(user.getUserId())
                    .proLineId(mesPmWorkOrder.getProLineId())
                    .routeId(mesPmWorkOrder.getRouteId())
                    .build();
            // 3、判断首条码，若是则更新工单状态
            if (mesPmWorkOrder.getWorkOrderStatus().equals("0") || mesPmWorkOrder.getWorkOrderStatus().equals("1")) {
                mesPmWorkOrder.setWorkOrderStatus(Byte.valueOf("2"));
                pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
                updateProcessDto.setNextProcessId(vo.getProcessId());
            } else {
                MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                        .workOrderCode(vo.getBarCode())
                        .build());
                updateProcessDto.setNextProcessId(mesSfcBarcodeProcess.getNextProcessId());
            }
            // 4、更新下一工序，增加工序记录
            return ControllerUtil.returnCRUD(BarcodeUtils.updateProcess(updateProcessDto));
        } catch (Exception ex) {
            throw new BizErrorException(ex);
        }
    }

    @ApiOperation("PDA包箱作业-条码提交")
    @PostMapping("/pdaCheckPacking")
    public ResponseEntity pdaCheckPacking(PdaPackingVo vo) {

        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        try {
            // 1、检查条码、工单状态、排程
            BarcodeUtils.checkSN(CheckProductionVo.builder()
                    .barCode(vo.getBarCode())
                    .stationId(vo.getStationId())
                    .checkOrNot(vo.getCheckOrNot())
                    .build());

            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                    .findList(SearchMesSfcWorkOrderBarcode.builder()
                            .barcode(vo.getBarCode())
                            .build());
            MesSfcWorkOrderBarcodeDto sfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
            ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityByBarCode = pmFeignApi.workOrderDetail(sfcWorkOrderBarcodeDto.getWorkOrderId());
            // 条码所属工单
            MesPmWorkOrder mesPmWorkOrderByBarCode = pmWorkOrderResponseEntityByBarCode.getData();

            // 2、判断是否已有工单，若有则校验配置
            if (StringUtils.isNotEmpty(vo.getWorkOrderId())) {
                // 2.1、判断是否同一工单
                if (vo.getPackType().equals(1) && !sfcWorkOrderBarcodeDto.getWorkOrderId().equals(vo.getWorkOrderId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012018, sfcWorkOrderBarcodeDto.getWorkOrderId(), vo.getWorkOrderId());
                }
                // 2.2、判断是否同一料号
                ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityById = pmFeignApi.workOrderDetail(vo.getWorkOrderId());
                // PDA当前作业工单
                MesPmWorkOrder mesPmWorkOrderById = pmWorkOrderResponseEntityById.getData();
                if (vo.getPackType().equals(1) && !mesPmWorkOrderByBarCode.getMaterialId().equals(mesPmWorkOrderById.getMaterialId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012019, mesPmWorkOrderByBarCode.getMaterialId(), mesPmWorkOrderById.getMaterialId());
                }
            }



            // 条码对应工序
            MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .workOrderCode(vo.getBarCode())
                    .build());
            // 3、判断是否已有箱码，生成箱码
            if (StringUtils.isEmpty(vo.getCartonCode())) {
                String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrderByBarCode.getMaterialId().toString(),
                        mesPmWorkOrderByBarCode.getBarcodeRuleSetId(),
                        mesSfcBarcodeProcess.getProcessId().toString(),
                        mesSfcBarcodeProcess.getMaterialCode(),
                        mesPmWorkOrderByBarCode.getWorkOrderId().toString(),
                        "09");
                vo.setCartonCode(cartonCode);
            } else {
                // 4、包箱是否已满，包箱是否已关闭
                MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(vo.getProductCartonId());
//                if(mesSfcProductCarton)
                SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
                searchMesSfcBarcodeProcess.setCartonCode(vo.getCartonCode());
                List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(searchMesSfcBarcodeProcess);
                if(mesSfcBarcodeProcessList.size() >= vo.getCartonNum()){
                    String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrderByBarCode.getMaterialId().toString(),
                            mesPmWorkOrderByBarCode.getBarcodeRuleSetId(),
                            mesSfcBarcodeProcess.getProcessId().toString(),
                            mesSfcBarcodeProcess.getMaterialCode(),
                            mesPmWorkOrderByBarCode.getWorkOrderId().toString(),
                            "09");
                    vo.setCartonCode(cartonCode);
                }
            }

            // 5、判断工单数跟已包箱数

            // 6、更新包箱号至过站表
            mesSfcBarcodeProcess.setCartonCode(vo.getCartonCode());
            mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);

            // 7、是否扫附件码，若不则检查ok直接过站
            if (!vo.getAnnex()) {
                if (vo.getPrint()) {
                    // 打印条码
                    BarcodeUtils.printBarCode(sfcWorkOrderBarcodeDto);
                }
                UpdateProcessDto updateProcessDto = UpdateProcessDto.builder()
                        .badnessPhenotypeCode("N/A")
                        .barCode(vo.getBarCode())
                        .equipmentId("N/A")
                        .operatorUserId(user.getUserId())
                        .proLineId(mesPmWorkOrderByBarCode.getProLineId())
                        .routeId(mesPmWorkOrderByBarCode.getRouteId())
                        .build();
                // 判断首条码，若是则更新工单状态
                if (mesPmWorkOrderByBarCode.getWorkOrderStatus().equals("0") || mesPmWorkOrderByBarCode.getWorkOrderStatus().equals("1")) {
                    mesPmWorkOrderByBarCode.setWorkOrderStatus(Byte.valueOf("2"));
                    pmFeignApi.updateSmtWorkOrder(mesPmWorkOrderByBarCode);
                    updateProcessDto.setNextProcessId(mesSfcBarcodeProcess.getProcessId());
                } else {
                    updateProcessDto.setNextProcessId(mesSfcBarcodeProcess.getNextProcessId());
                }
                // 更新下一工序，增加工序记录
                return ControllerUtil.returnCRUD(BarcodeUtils.updateProcess(updateProcessDto));
            } else {
                // 构造返回值
                return ControllerUtil.returnSuccess();
            }
        } catch (Exception ex) {
            throw new BizErrorException(ex);
        }
    }

    @ApiOperation("PDA包箱作业-附件码提交")
    @PostMapping("/pdaCheckPackingAnnex")
    public ResponseEntity pdaCheckPackingAnnex(PdaPackingAnnexVo vo) {
        try {
            // 1、判断附件码是否存在
            BarcodeUtils.checkSN(CheckProductionVo.builder()
                    .barCode(vo.getBarCode())
                    .stationId(vo.getStationId())
                    .checkOrNot(false)
                    .build());
            return ControllerUtil.returnSuccess();
        } catch (Exception ex) {
            throw new BizErrorException(ex);
        }
    }



}
