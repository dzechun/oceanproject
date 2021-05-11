package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStation;
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
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MesSfcBarcodeOperationServiceImpl implements MesSfcBarcodeOperationService {


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


    @Override
    public int pdaPutIntoProduction(PdaPutIntoProductionDto vo) {
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
            return BarcodeUtils.updateProcess(updateProcessDto);
        } catch (Exception ex) {
            throw new BizErrorException(ex);
        }
    }

    @Override
    public PdaCartonRecordDto findLastCarton(Long processId, Long stationId) {
        Example example = new Example(MesSfcProductCarton.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationId", stationId);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductCarton> sfcProductCartonList = mesSfcProductCartonService.selectByExample(example);
        if (sfcProductCartonList.isEmpty()) {
            return null;
        }
        // 组装
        return buildCartonData(sfcProductCartonList, processId, stationId);
    }

    @Override
    public PdaCartonRecordDto cartonOperation(PdaCartonDto vo) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<MesSfcProductCarton> sfcProductCartonList = barCodeOperation(vo, user);
        return buildCartonData(sfcProductCartonList, vo.getProcessId(), vo.getStationId());
    }

    @Override
    public PdaCartonRecordDto cartonAnnexOperation(PdaCartonAnnexDto vo) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // 校验附件码有没有被使用
        Example example = new Example(MesSfcKeyPartRelevance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationCode", vo.getBarAnnexCode());
        int countByExample = mesSfcKeyPartRelevanceService.selectCountByExample(criteria);
        if (countByExample > 0) {
            // 提示该附件码已绑定
            throw new BizErrorException(ErrorCodeEnum.PDA40012028);
        }
        // 校验附件码是否要绑定当前产品
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityByBarCode = pmFeignApi.workOrderDetail(vo.getWorkOrderId());
        // 条码所属工单
        MesPmWorkOrder mesPmWorkOrderByBarCode = pmWorkOrderResponseEntityByBarCode.getData();
        SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo = new SearchMesPmWorkOrderProcessReWo();
        searchMesPmWorkOrderProcessReWo.setWorkOrderId(mesPmWorkOrderByBarCode.getWorkOrderId().toString());
        ResponseEntity<List<MesPmWorkOrderProcessReWoDto>> pmWorkOrderProcessReWoResponseEntity = pmFeignApi.findPmWorkOrderProcessReWoList(searchMesPmWorkOrderProcessReWo);
        List<MesPmWorkOrderProcessReWoDto> pmWorkOrderProcessReWoDtoList = pmWorkOrderProcessReWoResponseEntity.getData();
        Byte barcodeType = null;
        BigDecimal usageQty = null;
//            for (MesPmWorkOrderProcessReWoDto pmWorkOrderProcessReWoDto : pmWorkOrderProcessReWoDtoList) {
//                if (pmWorkOrderProcessReWoDto.getProcessId().equals(vo.getProcessId())) {
//                    List<MesPmWorkOrderMaterialRePDto> pmWorkOrderMaterialRePDtoList = pmWorkOrderProcessReWoDto.getList();
//                    MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto = pmWorkOrderMaterialRePDtoList.get(0);
//                    if (pmWorkOrderMaterialRePDto.getScanType().equals(2)) {
//                        usageQty = pmWorkOrderMaterialRePDto.getUsageQty();
//                        materialTypeCode = ;
//                        break;
//                    }
//                }
//            }
        if (barcodeType == null) {
            // 提示该条码对应条码类别找不到
            throw new BizErrorException(ErrorCodeEnum.PDA40012030);
        }
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(vo.getBarAnnexCode())
                        .barcodeType(barcodeType)
                        .build());
        if (mesSfcWorkOrderBarcodeDtos.size() <= 0) {
            // 提示找不到对应的附件码
            throw new BizErrorException(ErrorCodeEnum.PDA40012031);
        }
        MesSfcWorkOrderBarcodeDto sfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        // 判断附件码数目是否满足
        Example example1 = new Example(MesSfcKeyPartRelevance.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("workOrderBarcodeId", vo.getWorkOrderId());
        List<MesSfcKeyPartRelevance> sfcKeyPartRelevanceList = mesSfcKeyPartRelevanceService.selectByExample(criteria1);
        if (usageQty.compareTo(new BigDecimal(sfcKeyPartRelevanceList.size())) != 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012029);
        }
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderCode(vo.getBarCode())
                .processId(vo.getProcessId())
                .build());
        BaseProLine proLine = baseFeignApi.selectProLinesDetail(vo.getProLineId()).getData();
        BaseProcess baseProcess = baseFeignApi.processDetail(vo.getProcessId()).getData();
        BaseStation baseStation = baseFeignApi.findStationDetail(vo.getStationId()).getData();
        // 绑定附件码跟条码关系
        mesSfcKeyPartRelevanceService.save(MesSfcKeyPartRelevance.builder()
                .barcodeCode(vo.getBarCode())
                .workOrderId(vo.getWorkOrderId())
                .workOrderCode(mesPmWorkOrderByBarCode.getWorkOrderCode())
                .workOrderBarcodeId(sfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                .proLineId(vo.getProLineId())
                .proCode(proLine.getProCode())
                .proName(proLine.getProName())
                .processId(vo.getProcessId())
                .processCode(baseProcess.getProcessCode())
                .processName(baseProcess.getProcessName())
                .stationId(vo.getStationId())
                .stationCode(baseStation.getStationCode())
                .stationName(baseStation.getStationName())
                .materialId(mesPmWorkOrderByBarCode.getMaterialId())
                .materialCode(mesSfcBarcodeProcess.getMaterialCode())
                .materialName(mesSfcBarcodeProcess.getMaterialName())
                .materialVer(mesSfcBarcodeProcess.getMaterialVer())
                .workOrderPartBarcodeId(sfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                .partBarcode(vo.getBarAnnexCode())
                .operatorUserId(user.getUserId())
                .operatorTime(new Date())
                .orgId(user.getOrganizationId())
                .createTime(new Date())
                .createUserId(user.getUserId())
                .isDelete((byte) 1)
                .build());
        // 走条码过站流程
        List<MesSfcProductCarton> sfcProductCartonList = barCodeOperation(vo, user);
        return buildCartonData(sfcProductCartonList, vo.getProcessId(), vo.getStationId());
    }

    @Override
    public int updateCartonDescNum(Long productCartonId, BigDecimal cartonDescNum) {
        return mesSfcProductCartonService.update(MesSfcProductCarton.builder()
                .productCartonId(productCartonId)
                .nowPackageSpecQty(cartonDescNum)
                .build());
    }

    @Override
    public List<MesSfcProductCarton> findCartonByStationId(Long stationId) {
        Example example = new Example(MesSfcProductCarton.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationId", stationId);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductCarton> sfcProductCartonList = mesSfcProductCartonService.selectByExample(example);
        return sfcProductCartonList;
    }


    /**
     * 组装包箱数据
     *
     * @param sfcProductCartonList
     * @param processId
     * @param stationId
     * @return
     */
    private PdaCartonRecordDto buildCartonData(List<MesSfcProductCarton> sfcProductCartonList, Long processId, Long stationId) {

        MesSfcProductCarton mesSfcProductCarton = sfcProductCartonList.get(0);
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityByBarCode = pmFeignApi.workOrderDetail(mesSfcProductCarton.getWorkOrderId());
        // 条码所属工单
        MesPmWorkOrder mesPmWorkOrderByBarCode = pmWorkOrderResponseEntityByBarCode.getData();
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId())
                .stationId(stationId)
                .build());
        if (!mesSfcBarcodeProcess.getProcessId().equals(processId)) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012003, mesSfcBarcodeProcess.getBarcode(), mesSfcBarcodeProcess.getNextProcessCode());
        }
        ResponseEntity<BaseMaterial> materialResponseEntity = baseFeignApi.materialDetail(mesSfcBarcodeProcess.getMaterialId());
        if (materialResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012024, mesSfcBarcodeProcess.getMaterialId());
        }
        // 产品物料信息
        BaseMaterial baseMaterial = materialResponseEntity.getData();
        List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder().workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId()).build());
        // 已扫包箱
        Map<String, List<MesSfcBarcodeProcess>> barcodeProcessGroup = mesSfcBarcodeProcessList.stream().collect(Collectors.groupingBy(MesSfcBarcodeProcess::getCartonCode));
        // 扫描数量
        List<MesSfcBarcodeProcess> barcodeProcessList = barcodeProcessGroup.get(mesSfcProductCarton.getCartonCode());
        Example example = new Example(MesSfcKeyPartRelevance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("workOrderBarcodeId", barcodeProcessList.stream().map(MesSfcBarcodeProcess::getWorkOrderBarcodeId).collect(Collectors.toList()));
        List<MesSfcKeyPartRelevance> sfcKeyPartRelevanceList = mesSfcKeyPartRelevanceService.selectByExample(criteria);
        List<MesSfcBarcodeProcessDto> dtos = new ArrayList<>();
        for (MesSfcBarcodeProcess mesSfcBarcodeProcess1 : barcodeProcessList) {
            MesSfcBarcodeProcessDto dto = new MesSfcBarcodeProcessDto();
            BeanUtils.copyProperties(mesSfcBarcodeProcess1, dto);
            dto.setSfcKeyPartRelevanceList(sfcKeyPartRelevanceList.stream().filter(item -> item.getWorkOrderBarcodeId().equals(dto.getWorkOrderBarcodeId())).collect(Collectors.toList()));
            dtos.add(dto);
        }
        return PdaCartonRecordDto.builder()
                .workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId())
                .workOrderCode(mesPmWorkOrderByBarCode.getWorkOrderCode())
                .productionQty(mesPmWorkOrderByBarCode.getProductionQty())
                .workOrderQty(mesPmWorkOrderByBarCode.getWorkOrderQty())
                .cartonCode(mesSfcProductCarton.getCartonCode())
                .productCartonId(mesSfcProductCarton.getProductCartonId())
                .cartonNum(mesSfcProductCarton.getNowPackageSpecQty())
                .materialCode(baseMaterial.getMaterialCode())
                .materialDesc(baseMaterial.getMaterialDesc())
                .packedNum(barcodeProcessGroup.size())
                .scansNum(barcodeProcessList.size())
                .barcodeProcessList(dtos)
                .build();
    }

    /**
     * 包箱作业
     * @param vo
     * @param user
     * @return
     * @throws Exception
     */
    private List<MesSfcProductCarton> barCodeOperation(PdaCartonDto vo, SysUser user) throws Exception{
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(vo.getBarCode())
                        .build());
        MesSfcWorkOrderBarcodeDto sfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntityByBarCode = pmFeignApi.workOrderDetail(sfcWorkOrderBarcodeDto.getWorkOrderId());
        // 条码所属工单
        MesPmWorkOrder mesPmWorkOrderByBarCode = pmWorkOrderResponseEntityByBarCode.getData();

        // 1、检查条码、工单状态、排程
        BarcodeUtils.checkSN(CheckProductionDto.builder()
                .barCode(vo.getBarCode())
                .stationId(vo.getStationId())
                .checkOrNot(vo.getCheckOrNot())
                .build());

        // 箱码对应状态表
        MesSfcProductCarton mesSfcProductCarton = null;
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
            if (saveRes >= 0) {
                mesSfcProductCarton = mesSfcProductCartonService.selectOne(sfcProductCarton);
                if (mesSfcProductCarton != null) {
                    vo.setProductCartonId(mesSfcProductCarton.getProductCartonId());
                }
            }
        } else {
            mesSfcProductCarton = mesSfcProductCartonService.selectByKey(vo.getProductCartonId());
            // 4、包箱已关闭，生成箱码并添加新的箱码状态数据
            if (mesSfcProductCarton.getCloseStatus().equals(1)) {
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
                if (saveRes >= 0) {
                    mesSfcProductCarton = mesSfcProductCartonService.selectOne(sfcProductCarton);
                    if (mesSfcProductCarton != null) {
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
            if (mesSfcBarcodeProcessList.size() >= vo.getCartonNum()) {
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
            if (mesPmWorkOrderByBarCode.getPutIntoProcessId().equals(mesSfcBarcodeProcess.getProcessId())) {
                mesPmWorkOrderByBarCode.setProductionQty(mesPmWorkOrderByBarCode.getProductionQty().add(new BigDecimal(1)));
            }
            // 判断是否产出工序，若是则产出数量+1
            if (mesPmWorkOrderByBarCode.getOutputProcessId().equals(mesSfcBarcodeProcess.getProcessId())) {
                mesPmWorkOrderByBarCode.setOutputQty(mesPmWorkOrderByBarCode.getOutputQty().add(new BigDecimal(1)));
                // 判断产出数量是否等于工单数量，若是关闭工单
                if (mesPmWorkOrderByBarCode.getOutputQty().equals(mesPmWorkOrderByBarCode.getWorkOrderQty())) {
                    mesPmWorkOrderByBarCode.setWorkOrderStatus((byte) 6);
                }
            }
            pmFeignApi.updateSmtWorkOrder(mesPmWorkOrderByBarCode);
            // 更新下一工序，增加工序记录
            BarcodeUtils.updateProcess(updateProcessDto);
        }
        // 构造返回值
        Example example = new Example(MesSfcProductCarton.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationId", vo.getStationId());
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductCarton> sfcProductCartonList = mesSfcProductCartonService.selectByExample(example);
        return sfcProductCartonList;
    }
}
