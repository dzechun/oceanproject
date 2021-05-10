package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcKeyPartRelevance;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Transactional
public class MesSfcBarcodeOperationController {

    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @ApiOperation("PDA投产作业")
    @PostMapping("/pdaPutIntoProduction")
    public ResponseEntity pdaPutIntoProduction(@ApiParam(value = "PDA作业对象") @RequestBody PdaPutIntoProductionDto vo) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        try {
            // 1、检查条码、工单
            BarcodeUtils.checkSN(CheckProductionDto.builder()
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


    @ApiOperation("PDA包箱作业-查询上次作业数据")
    @PostMapping("/pdaCheckPacking")
    public ResponseEntity findCarton(@ApiParam(value = "processId",required = true)@RequestParam  @NotNull(message="processId不能为空") Long processId,
                                     @ApiParam(value = "stationId",required = true)@RequestParam  @NotNull(message="stationId不能为空") Long stationId){
        Example example = new Example(MesSfcProductCarton.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationId", stationId);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductCarton> sfcProductCartonList = mesSfcProductCartonService.selectByExample(example);
        if(sfcProductCartonList.isEmpty()){
//            throw
        }
        MesSfcProductCarton mesSfcProductCarton = sfcProductCartonList.get(0);
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityByBarCode = pmFeignApi.workOrderDetail(mesSfcProductCarton.getWorkOrderId());
        // 条码所属工单
        MesPmWorkOrder mesPmWorkOrderByBarCode = pmWorkOrderResponseEntityByBarCode.getData();
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId())
                .stationId(stationId)
                .build());
        if(!mesSfcBarcodeProcess.getProcessId().equals(processId)){
//            throw
        }
        return null;
    }

    @ApiOperation("PDA包箱作业-条码提交")
    @PostMapping("/pdaCheckPacking")
    public ResponseEntity pdaCheckPacking(PdaCartonDto vo) {

        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        try {

            // 未满箱提交，关箱
            if(vo.getCloseOrNot()){
                if(StringUtils.isEmpty(vo.getProductCartonId())){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012025);
                }
                MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(vo.getProductCartonId());
                mesSfcProductCarton.setCloseStatus((byte) 1);
                mesSfcProductCarton.setCloseCartonUserId(user.getUserId());
                mesSfcProductCarton.setCloseCartonTime(new Date());
                mesSfcProductCarton.setModifiedUserId(user.getUserId());
                mesSfcProductCarton.setModifiedTime(new Date());
                return ControllerUtil.returnCRUD(mesSfcProductCartonService.update(mesSfcProductCarton));
            }

            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                    .findList(SearchMesSfcWorkOrderBarcode.builder()
                            .barcode(vo.getBarCode())
                            .build());
            MesSfcWorkOrderBarcodeDto sfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
            ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityByBarCode = pmFeignApi.workOrderDetail(sfcWorkOrderBarcodeDto.getWorkOrderId());
            // 条码所属工单
            MesPmWorkOrder mesPmWorkOrderByBarCode = pmWorkOrderResponseEntityByBarCode.getData();

            // 箱码对应状态表
            MesSfcProductCarton mesSfcProductCarton = null;
            Example example = new Example(MesSfcProductCarton.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("stationId", vo.getStationId());
            List<MesSfcProductCarton> sfcProductCartonList = mesSfcProductCartonService.selectByExample(example);
            List<MesSfcProductCarton> sfcProductCartons = sfcProductCartonList.stream().filter(item -> item.getCloseStatus().equals(1)).collect(Collectors.toList());
            if(sfcProductCartons.size() > 0){
                // 取出未关闭包箱数据
                mesSfcProductCarton = sfcProductCartons.get(0);
                if (vo.getPackType().equals(1) && !mesSfcProductCarton.getWorkOrderId().equals(mesPmWorkOrderByBarCode.getWorkOrderId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012026);
                }
                if (vo.getPackType().equals(2) && !mesSfcProductCarton.getMaterialId().equals(mesPmWorkOrderByBarCode.getMaterialId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012026);
                }
                vo.setProductCartonId(mesSfcProductCarton.getProductCartonId());
                vo.setCartonCode(mesSfcProductCarton.getCartonCode());
                vo.setWorkOrderId(mesSfcProductCarton.getWorkOrderId());
            }

            // 1、检查条码、工单状态、排程
            BarcodeUtils.checkSN(CheckProductionDto.builder()
                    .barCode(vo.getBarCode())
                    .stationId(vo.getStationId())
                    .checkOrNot(vo.getCheckOrNot())
                    .build());

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
                if (vo.getPackType().equals(2) && !mesPmWorkOrderByBarCode.getMaterialId().equals(mesPmWorkOrderById.getMaterialId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012019, mesPmWorkOrderByBarCode.getMaterialId(), mesPmWorkOrderById.getMaterialId());
                }
            }

            // 条码对应工序
            MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .workOrderCode(vo.getBarCode())
                    .processId(vo.getProcessId())
                    .build());

            // 3、判断是否已有箱码，生成箱码
            if (StringUtils.isEmpty(vo.getCartonCode())) {
                // 没有未关闭包箱，重新生成并初始化
                String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrderByBarCode.getMaterialId().toString(),
                        mesPmWorkOrderByBarCode.getBarcodeRuleSetId(),
                        vo.getProcessId().toString(),
                        mesSfcBarcodeProcess.getMaterialCode(),
                        mesPmWorkOrderByBarCode.getWorkOrderId().toString(),
                        "09");
                vo.setCartonCode(cartonCode);
                // 添加包箱表数据
                MesSfcProductCarton sfcProductCarton = MesSfcProductCarton.builder()
                        .cartonCode(cartonCode)
                        .closeStatus((byte) 0)
                        .createTime(new Date())
                        .createUserId(user.getUserId())
                        .isDelete((byte) 1)
                        .orgId(user.getOrganizationId())
                        .stationId(vo.getStationId())
                        .workOrderId(sfcWorkOrderBarcodeDto.getWorkOrderId())
                        .build();
                int saveRes = mesSfcProductCartonService.save(sfcProductCarton);
                if(saveRes >= 0){
                    mesSfcProductCarton = mesSfcProductCartonService.selectOne(sfcProductCarton);
                    if(mesSfcProductCarton != null){
                        vo.setProductCartonId(mesSfcProductCarton.getProductCartonId());
                    }
                }
            } else {
//                mesSfcProductCarton = mesSfcProductCartonService.selectByKey(vo.getProductCartonId());
                // 4、包箱已关闭，生成箱码并添加新的箱码状态数据
                if(mesSfcProductCarton.getCloseStatus().equals(1)){
                    String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrderByBarCode.getMaterialId().toString(),
                            mesPmWorkOrderByBarCode.getBarcodeRuleSetId(),
                            vo.getProcessId().toString(),
                            mesSfcBarcodeProcess.getMaterialCode(),
                            mesPmWorkOrderByBarCode.getWorkOrderId().toString(),
                            "09");
                    vo.setCartonCode(cartonCode);
                    // 添加包箱表数据
                    MesSfcProductCarton sfcProductCarton = MesSfcProductCarton.builder()
                            .cartonCode(cartonCode)
                            .closeStatus((byte) 0)
                            .createTime(new Date())
                            .createUserId(user.getUserId())
                            .isDelete((byte) 1)
                            .orgId(user.getOrganizationId())
                            .stationId(vo.getStationId())
                            .workOrderId(sfcWorkOrderBarcodeDto.getWorkOrderId())
                            .build();
                    int saveRes = mesSfcProductCartonService.save(sfcProductCarton);
                    if(saveRes >= 0){
                        mesSfcProductCarton = mesSfcProductCartonService.selectOne(sfcProductCarton);
                        if(mesSfcProductCarton != null){
                            vo.setProductCartonId(mesSfcProductCarton.getProductCartonId());
                        }
                    }
                }
            }

            // 5、更新包箱号至过站表
            mesSfcBarcodeProcess.setCartonCode(vo.getCartonCode());
            mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);

            // 6、是否扫附件码，若不则检查ok直接过站
            if (!vo.getAnnex()) {
                // 6.1、判断是否包箱已满，关箱
                List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder().cartonCode(vo.getCartonCode()).build());
                if(mesSfcBarcodeProcessList.size() >= vo.getCartonNum()){
                    // 包箱已满，关箱
                    mesSfcProductCarton.setCloseStatus((byte) 1);
                    mesSfcProductCarton.setCloseCartonUserId(user.getUserId());
                    mesSfcProductCarton.setCloseCartonTime(new Date());
                    mesSfcProductCarton.setModifiedUserId(user.getUserId());
                    mesSfcProductCarton.setModifiedTime(new Date());
                    mesSfcProductCartonService.update(mesSfcProductCarton);
                    if (vo.getPrint()) {
                        // 关箱后台才能打印条码
                        BarcodeUtils.printBarCode(sfcWorkOrderBarcodeDto);
                    }
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
                    updateProcessDto.setNextProcessId(mesSfcBarcodeProcess.getProcessId());
                } else {
                    updateProcessDto.setNextProcessId(mesSfcBarcodeProcess.getNextProcessId());
                }
                // 判断是否投产工序，若是则投产数量+1
                if(mesPmWorkOrderByBarCode.getPutIntoProcessId().equals(mesSfcBarcodeProcess.getProcessId())){
                    mesPmWorkOrderByBarCode.setProductionQty(mesPmWorkOrderByBarCode.getProductionQty().add(new BigDecimal(1)));
                }
                // 判断是否产出工序，若是则产出数量+1
                if(mesPmWorkOrderByBarCode.getOutputProcessId().equals(mesSfcBarcodeProcess.getProcessId())){
                    mesPmWorkOrderByBarCode.setOutputQty(mesPmWorkOrderByBarCode.getOutputQty().add(new BigDecimal(1)));
                    // 判断产出数量是否等于工单数量，若是关闭工单
                    if (mesPmWorkOrderByBarCode.getOutputQty().equals(mesPmWorkOrderByBarCode.getWorkOrderQty())){
                        mesPmWorkOrderByBarCode.setWorkOrderStatus((byte) 6);
                    }
                }
                pmFeignApi.updateSmtWorkOrder(mesPmWorkOrderByBarCode);
                // 更新下一工序，增加工序记录
                return ControllerUtil.returnCRUD(BarcodeUtils.updateProcess(updateProcessDto));
            } else {
                // 构造返回值
                ResponseEntity<BaseMaterial> materialResponseEntity = baseFeignApi.materialDetail(mesSfcBarcodeProcess.getMaterialId());
                if(materialResponseEntity.getCode() != 0){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012024, mesSfcBarcodeProcess.getMaterialId());
                }
                // 产品物料信息
                BaseMaterial baseMaterial = materialResponseEntity.getData();
                List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder().workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId()).build());
                // 已扫包箱
                Map<String, List<MesSfcBarcodeProcess>> barcodeProcessGroup = mesSfcBarcodeProcessList.stream().collect(Collectors.groupingBy(MesSfcBarcodeProcess::getCartonCode));
                // 扫描数量
                List<MesSfcBarcodeProcess> barcodeProcessList = barcodeProcessGroup.get(vo.getCartonCode());

                List<MesSfcKeyPartRelevanceDto> sfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcKeyPartRelevance.builder()
                        .workOrderBarcodeId(mesSfcBarcodeProcess.getWorkOrderBarcodeId())
                        .processId(mesSfcBarcodeProcess.getProcessId())
                        .stationId(vo.getStationId())
                        .workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId())
                        .build()));
                Map<String, List<MesSfcKeyPartRelevanceDto>> collect = sfcKeyPartRelevanceDtoList.stream().collect(Collectors.groupingBy(MesSfcKeyPartRelevance::getBarcodeCode));
                return ControllerUtil.returnSuccess("成功", PdaCartonRecordDto.builder()
                        .workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId())
                        .workOrderCode(mesPmWorkOrderByBarCode.getWorkOrderCode())
                        .productionQty(mesPmWorkOrderByBarCode.getProductionQty())
                        .workOrderQty(mesPmWorkOrderByBarCode.getWorkOrderQty())
                        .cartonCode(vo.getCartonCode())
                        .productCartonId(vo.getProductCartonId())
                        .cartonNum(vo.getCartonNum())
                        .materialCode(baseMaterial.getMaterialCode())
                        .materialDesc(baseMaterial.getMaterialDesc())
                        .packedNum(barcodeProcessGroup.size())
                        .scansNum(barcodeProcessList.size())
                        .build());
            }
        } catch (Exception ex) {
            throw new BizErrorException(ex);
        }
    }

    @ApiOperation("PDA包箱作业-附件码提交")
    @PostMapping("/pdaCheckPackingAnnex")
    public ResponseEntity pdaCheckPackingAnnex(PdaCartonAnnexDto vo) {
        try {
            // 1、判断附件码是否存在
            BarcodeUtils.checkSN(CheckProductionDto.builder()
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
