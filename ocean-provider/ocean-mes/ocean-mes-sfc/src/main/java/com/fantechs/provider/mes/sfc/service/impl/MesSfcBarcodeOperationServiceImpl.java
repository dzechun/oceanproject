package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCarton;
import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCartonDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesCodeReSpc;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.leisai.LeisaiFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeOperationMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonMapper;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class MesSfcBarcodeOperationServiceImpl implements MesSfcBarcodeOperationService {
    protected static final Logger logger = LoggerFactory.getLogger(MesSfcBarcodeOperationServiceImpl.class);

    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    private MesSfcProductCartonDetService mesSfcProductCartonDetService;
    @Resource
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
    @Resource
    private MesSfcProductCartonMapper mesSfcProductCartonMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private AuthFeignApi authFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private LeisaiFeignApi leisaiFeignApi;
    @Resource
    private MesSfcBarcodeOperationMapper mesSfcBarcodeOperationMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public Boolean pdaCartonWork(PdaCartonWorkDto dto) throws Exception {
        // 获取登录用户

        long startTime = System.currentTimeMillis();

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(dto.getBarCode())) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012026);
        }
        // 1、检查条码、工单状态、排程
        BarcodeUtils.checkSN(CheckProductionDto.builder()
                .barCode(dto.getBarCode())
                .stationId(dto.getStationId())
                .processId(dto.getProcessId())
                .checkOrNot(dto.getCheckOrNot())
                .packType(dto.getPackType())
                .proLineId(dto.getProLineId())
                .build());


        logger.info("=========检查条码，执行时长：{}",System.currentTimeMillis() - startTime);
        long startTime1 = System.currentTimeMillis();

        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(dto.getBarCode())
                        .build());
        // 条码信息
        MesSfcWorkOrderBarcodeDto orderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        // 2、校验条码是否关联包箱
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", orderBarcodeDto.getWorkOrderBarcodeId());
        List<MesSfcProductCartonDetDto> productCartonDetDtos = mesSfcProductCartonDetService.findList(map);
        if (productCartonDetDtos != null && productCartonDetDtos.size() > 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012013);
        }


        // 条码对应工单
        long add = System.currentTimeMillis();
        MesPmWorkOrder mesPmWorkOrder = mesSfcBarcodeOperationMapper.findWorkOrderDetail(orderBarcodeDto.getWorkOrderId());
        logger.info("=========获取条码信息，执行时长：{}",System.currentTimeMillis() - add);

        // 3、获取工位工序的包箱
        map.clear();
        map.put("stationId", dto.getStationId());
        map.put("closeStatus", 0);
        List<MesSfcProductCartonDto> sfcProductCartonDtos = mesSfcProductCartonService.findList(map);
        MesSfcProductCarton sfcProductCarton = null;
        // 4、校验同一料号/同一工单
        if (sfcProductCartonDtos != null && sfcProductCartonDtos.size() > 0) {
            // 有未关闭包箱
            sfcProductCarton = sfcProductCartonDtos.get(0);
            // 4.1、判断是否同一工单
            if ("1".equals(dto.getPackType()) && !orderBarcodeDto.getWorkOrderId().equals(sfcProductCarton.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012018, orderBarcodeDto.getWorkOrderId(), sfcProductCarton.getWorkOrderId());
            }
            // 4.2、判断是否同一料号
            // PDA当前作业工单
            MesPmWorkOrder mesPmWorkOrderById = mesSfcBarcodeOperationMapper.findMesPmWorkOrder(sfcProductCarton.getWorkOrderId());
            if ("2".equals(dto.getPackType()) && !mesPmWorkOrder.getMaterialId().equals(mesPmWorkOrderById.getMaterialId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012019, mesPmWorkOrder.getMaterialId(), mesPmWorkOrderById.getMaterialId());
            }
        }

        logger.info("=========获取工位工序的包箱，执行时长：{}",System.currentTimeMillis() - startTime1);
        long startTime2 = System.currentTimeMillis();

        // 条码对应过站记录信息
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .barcode(dto.getBarCode())
                .nextProcessId(dto.getProcessId())
                .build());

        // 5、是否要扫附件码
        if(dto.getAnnex() && StringUtils.isEmpty(dto.getBarAnnexCode())){
            return false;
        }
        if (dto.getAnnex()) {
            Example example = new Example(MesSfcKeyPartRelevance.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("partBarcode", dto.getBarAnnexCode());
            int countByExample = mesSfcKeyPartRelevanceService.selectCountByExample(example);
            if (countByExample > 0) {
                // 提示该附件码已绑定
                throw new BizErrorException(ErrorCodeEnum.PDA40012028);
            }

            SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo = new SearchMesPmWorkOrderProcessReWo();
            searchMesPmWorkOrderProcessReWo.setMaterialId(mesPmWorkOrder.getMaterialId().toString());
            searchMesPmWorkOrderProcessReWo.setProcessId(dto.getProcessId().toString());
            searchMesPmWorkOrderProcessReWo.setWorkOrderId(mesPmWorkOrder.getWorkOrderId().toString());
            // 产品关键物料清单 有且只有一条
            List<MesPmWorkOrderProcessReWoDto> pmWorkOrderProcessReWoDtoList = mesSfcBarcodeOperationMapper.findPmWorkOrderProcessReWoList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderProcessReWo));
            if (CollectionUtils.isEmpty(pmWorkOrderProcessReWoDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012016);
            }
            List<MesPmWorkOrderMaterialRePDto> pmWorkOrderMaterialRePDtoList = mesSfcBarcodeOperationMapper.findWorkOrderMaterialReP(pmWorkOrderProcessReWoDtoList.get(0).getWorkOrderProcessReWoId());
            // 零件所有用量
            BigDecimal usageQty = BigDecimal.ZERO;
            for (MesPmWorkOrderMaterialRePDto workOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList) {
                usageQty = usageQty.add(workOrderMaterialRePDto.getUsageQty());
            }

            // 关键部件物料清单
            map.clear();
            map.put("workOrderId", mesPmWorkOrder.getWorkOrderId());
            map.put("processId", dto.getProcessId());
//            map.put("materialId", mesPmWorkOrder.getMaterialId());
            map.put("workOrderBarcodeId", orderBarcodeDto.getWorkOrderBarcodeId());
            List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);

            // 关键部件物料数量大于等于用量
            if (keyPartRelevanceDtos.size() >= usageQty.intValue()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012020);
            }

            BaseProLine proLine = mesSfcBarcodeOperationMapper.findBaseProLine(dto.getProLineId());
            BaseProcess baseProcess = mesSfcBarcodeOperationMapper.findBaseProcess(dto.getProcessId());
            BaseStation baseStation = mesSfcBarcodeOperationMapper.findBaseStation(dto.getStationId());

            // 查找该附件码是否存在系统中
            List<MesSfcWorkOrderBarcodeDto> sfcWorkOrderAnnexBarcodeDtos = mesSfcWorkOrderBarcodeService
                    .findList(SearchMesSfcWorkOrderBarcode.builder()
                            .barcode(dto.getBarAnnexCode())
                            .build());

            boolean isBarCode = false;
            if(sfcWorkOrderAnnexBarcodeDtos != null && sfcWorkOrderAnnexBarcodeDtos.size() > 0){
                for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto: sfcWorkOrderAnnexBarcodeDtos) {
                    if(workOrderBarcodeDto.getBarcode().equals(dto.getBarAnnexCode())){
                        isBarCode = true;
                        break;
                    }
                }
            }
            boolean iswork = false;
            if (!isBarCode) {
                List<Long> materialIds = new ArrayList<>();
                for (MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList) {
                    if (pmWorkOrderMaterialRePDto.getMaterialId() != null) {
                        materialIds.add(pmWorkOrderMaterialRePDto.getMaterialId());
                    }
                }
                SearchBaseSignature baseSignature = new SearchBaseSignature();
                baseSignature.setMaterialIds(materialIds);
                // 查找物料特征码
                Map<String, Object> mapParam = ControllerUtil.dynamicConditionByEntity(baseSignature);
                mapParam.put("orgId", user.getOrganizationId());
                List<BaseSignature> signatureList = mesSfcBarcodeOperationMapper.findSignatureList(mapParam);
                boolean flag = true;
                for (BaseSignature signature : signatureList) {
                    int material_count = 0;
                    for (MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList) {
                        if (signature.getMaterialId().equals(pmWorkOrderMaterialRePDto.getMaterialId())) {
                            material_count = pmWorkOrderMaterialRePDto.getUsageQty().intValue();
                            break;
                        }
                    }
                    if (material_count > 0) {
                        int keyPartCount = 0;
                        for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos) {
                            if (signature.getMaterialId().equals(keyPartRelevanceDto.getMaterialId())) {
                                keyPartCount = keyPartCount + 1;
                            }
                        }
                        if (keyPartCount >= material_count) {
                            // 当前零件料号已满，检查下个零件料号料
                            continue;
                        }
                        // 校验附件码是否符合特征码规则
                        Pattern pattern = Pattern.compile(signature.getSignatureRegex());
                        Matcher matcher = pattern.matcher(dto.getBarAnnexCode());
                        if (!matcher.matches()) {
                            // 该附件码不满足特征码，检查零件料号特征码
                            continue;
                        }
                        // 添加keyPart表关系
                        // 绑定附件码跟条码关系
                        mesSfcKeyPartRelevanceService.save(MesSfcKeyPartRelevance.builder()
                                .barcodeCode(dto.getBarCode())
                                .workOrderId(mesPmWorkOrder.getWorkOrderId())
                                .workOrderCode(mesPmWorkOrder.getWorkOrderCode())
                                .workOrderBarcodeId(orderBarcodeDto.getWorkOrderBarcodeId())
                                .proLineId(proLine.getProLineId())
                                .proCode(proLine.getProCode())
                                .proName(proLine.getProName())
                                .processId(baseProcess.getProcessId())
                                .processCode(baseProcess.getProcessCode())
                                .processName(baseProcess.getProcessName())
                                .stationId(baseStation.getStationId())
                                .stationCode(baseStation.getStationCode())
                                .stationName(baseStation.getStationName())
                                .materialId(mesPmWorkOrder.getMaterialId())
                                .materialCode(signature.getMaterialCode())
                                .materialName(signature.getMaterialName())
                                .materialVer(signature.getMaterialVersion())
                                .partBarcode(dto.getBarAnnexCode())
                                .operatorUserId(user.getUserId())
                                .operatorTime(new Date())
                                .orgId(user.getOrganizationId())
                                .createTime(new Date())
                                .createUserId(user.getUserId())
                                .isDelete((byte) 1)
                                .build());
                        flag = false;
                        iswork = false;
                        break;
                    }
                }
                if (flag) {
                    return false;
                }
            } else {
                // 条码
                for (MesSfcWorkOrderBarcodeDto barcodeDto : sfcWorkOrderAnnexBarcodeDtos) {
                    int material_count = 0;
                    for (MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList) {
                        if (barcodeDto.getLabelCategoryId().equals(pmWorkOrderMaterialRePDto.getLabelCategoryId())) {
                            material_count = pmWorkOrderMaterialRePDto.getUsageQty().intValue();
                            break;
                        }
                    }
                    if (material_count > 0) {
                        int keyPartCount = 0;
                        for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos) {
                            if (barcodeDto.getLabelCategoryId().equals(keyPartRelevanceDto.getLabelCategoryId())) {
                                keyPartCount = keyPartCount + 1;
                            }
                        }
                        if (keyPartCount >= material_count) {
                            throw new BizErrorException(ErrorCodeEnum.PDA40012029);
                        }
                        // 添加keyPart表关系
                        // 绑定附件码跟条码关系
                        mesSfcKeyPartRelevanceService.save(MesSfcKeyPartRelevance.builder()
                                .barcodeCode(dto.getBarCode())
                                .workOrderId(mesPmWorkOrder.getWorkOrderId())
                                .workOrderCode(mesPmWorkOrder.getWorkOrderCode())
                                .workOrderBarcodeId(orderBarcodeDto.getWorkOrderBarcodeId())
                                .proLineId(proLine.getProLineId())
                                .proCode(proLine.getProCode())
                                .proName(proLine.getProName())
                                .processId(baseProcess.getProcessId())
                                .processCode(baseProcess.getProcessCode())
                                .processName(baseProcess.getProcessName())
                                .stationId(baseStation.getStationId())
                                .stationCode(baseStation.getStationCode())
                                .stationName(baseStation.getStationName())
                                .labelCategoryId(barcodeDto.getLabelCategoryId())
                                .partBarcode(dto.getBarAnnexCode())
                                .operatorUserId(user.getUserId())
                                .operatorTime(new Date())
                                .orgId(user.getOrganizationId())
                                .createTime(new Date())
                                .createUserId(user.getUserId())
                                .isDelete((byte) 1)
                                .build());
                        iswork = false;

                        /**
                         * 万宝特性要求
                         * 扫附件码时，通过销售条码查找销售编码与PO关系表
                         * 绑定关系
                         */
                        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                        searchSysSpecItem.setSpecCode("wanbaoAutoClosePallet");
                        List<SysSpecItem> specItems = authFeignApi.findSpecItemList(searchSysSpecItem).getData();
                        if (!specItems.isEmpty() && "1".equals(specItems.get(0).getParaValue())){
                            SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
                            searchOmSalesCodeReSpc.setSamePackageCodeStatus((byte) 1);
                            List<OmSalesCodeReSpcDto> codeReSpcDtos = omFeignApi.findAll(searchOmSalesCodeReSpc).getData();
                            // 同个销售编码
                            List<OmSalesCodeReSpcDto> currentDtos = new ArrayList<>();
                            for (OmSalesCodeReSpcDto omSalesCodeReSpcDto : codeReSpcDtos){
                                if(StringUtils.isNotEmpty(omSalesCodeReSpcDto.getSalesCode())) {
                                    if (dto.getBarAnnexCode().contains(omSalesCodeReSpcDto.getSalesCode())) {
                                        currentDtos.add(omSalesCodeReSpcDto);
                                        continue;
                                    }
                                }
                            }
                            if (!currentDtos.isEmpty() && currentDtos.size() > 0){
                                // 排序，优先级最高的
                                currentDtos = currentDtos.stream().sorted(Comparator.comparing(OmSalesCodeReSpc::getPriority)).collect(Collectors.toList());
                                OmSalesCodeReSpc omSalesCodeReSpc = currentDtos.get(0);
                                // 绑定关系
                                mesSfcBarcodeProcess.setSamePackageCode(omSalesCodeReSpc.getSamePackageCode());
                                mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);

                                // 增加以匹配数
                                omSalesCodeReSpc.setMatchedQty(omSalesCodeReSpc.getMatchedQty() != null ? BigDecimal.ONE.add(omSalesCodeReSpc.getMatchedQty()) : BigDecimal.ONE);
                                if (omSalesCodeReSpc.getMatchedQty().compareTo(omSalesCodeReSpc.getSamePackageCodeQty()) == 0){
                                    omSalesCodeReSpc.setSamePackageCodeStatus((byte) 3);
                                }
                                omFeignApi.update(omSalesCodeReSpc);
                            }
                        }
                        break;
                    }
                }
            }
            if (iswork) {
                // 有附件码未作业，直接返回
                return false;
            }else {
                // 有附件码并且已作业
                if(keyPartRelevanceDtos.size() + 1 < usageQty.intValue()){
                    // 但是附件数量不满足工单清单用量，直接返回
                    return false;
                }
            }
        }
        // 6、判断是否已有箱码，生成箱码
        if (sfcProductCarton == null) {
            // 未关闭包箱不存在，生成包箱号并初始化包箱数据
            String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrder.getMaterialId(),
                    dto.getProcessId(),
                    mesSfcBarcodeProcess.getMaterialCode(),
                    mesPmWorkOrder.getWorkOrderId(),
                    "09");
            // 查询基础资料包装规格
            BasePackageSpecificationDto packageSpecificationDto = getPackageSpecification(mesSfcBarcodeProcess.getMaterialId(), dto.getProcessId());
            // 添加包箱表数据
            sfcProductCarton = saveCarton(cartonCode, user.getUserId(), user.getOrganizationId(), dto.getStationId(), mesPmWorkOrder.getWorkOrderId(), packageSpecificationDto.getPackageSpecificationQuantity(), mesPmWorkOrder.getMaterialId());
        }

        logger.info("=========生成箱码，执行时长：{}",System.currentTimeMillis() - startTime2);
        long startTime3 = System.currentTimeMillis();

        // 7、过站
        UpdateProcessDto updateProcessDto = UpdateProcessDto.builder()
                .badnessPhenotypeCode("N/A")
                .barCode(dto.getBarCode())
                .equipmentId("N/A")
                .operatorUserId(user.getUserId())
                .proLineId(dto.getProLineId())
                .routeId(mesPmWorkOrder.getRouteId())
                .nowProcessId(dto.getProcessId())
                .nowStationId(dto.getStationId())
                .workOrderId(mesPmWorkOrder.getWorkOrderId())
                .passCode(sfcProductCarton.getCartonCode())
                .passCodeType((byte) 1)
                .build();

        // 保存条码包箱关系
        mesSfcProductCartonDetService.save(MesSfcProductCartonDet.builder()
                .productCartonId(sfcProductCarton.getProductCartonId())
                .workOrderBarcodeId(orderBarcodeDto.getWorkOrderBarcodeId())
                .orgId(user.getOrganizationId())
                .createTime(new Date())
                .createUserId(user.getUserId())
                .isDelete((byte) 1)
                .build());

        logger.info("==========保存条码包箱关系，执行时长：{}",System.currentTimeMillis() - startTime3);
        long startTime4 = System.currentTimeMillis();

        // 更新下一工序，增加工序记录
        BarcodeUtils.updateProcess(updateProcessDto);

        logger.info("=========更新下一工序，增加工序记录，执行时长：{}",System.currentTimeMillis() - startTime4);
        long startTime5 = System.currentTimeMillis();

        // 8、判断是否包箱已满，关箱
        List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder()
                .cartonCode(sfcProductCarton.getCartonCode())
                .build());
        if (mesSfcBarcodeProcessList.size() >= sfcProductCarton.getNowPackageSpecQty().intValue()) {
            // 包箱已满，关箱
            sfcProductCarton.setCloseStatus((byte) 1);
            sfcProductCarton.setCloseCartonUserId(user.getUserId());
            sfcProductCarton.setCloseCartonTime(new Date());
            sfcProductCarton.setModifiedUserId(user.getUserId());
            sfcProductCarton.setModifiedTime(new Date());
            int update = mesSfcProductCartonService.update(sfcProductCarton);
            if (dto.getPrint() && update > 0) {
                // 关箱后台才能打印条码
                BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                        .barcode(sfcProductCarton.getCartonCode())
                        .labelTypeCode("09")
                        .workOrderId(mesPmWorkOrder.getWorkOrderId())
                        .printName(dto.getPrintName() != null ? dto.getPrintName() : "测试")
                        .build());
            }

            //雷赛包箱数据是否同步到WMS开始
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("CartonIfToLeisaiWms");
            List<SysSpecItem> specItems = authFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(specItems.size()>0) {
                SysSpecItem sysSpecItem = specItems.get(0);
                if(sysSpecItem.getParaValue().equals("1")){
                    LeisaiWmsCarton leisaiWmsCarton=new LeisaiWmsCarton();
                    List<LeisaiWmsCartonDet> dets=new ArrayList<>();

                    leisaiWmsCarton.setBOXID(sfcProductCarton.getCartonCode());
                    leisaiWmsCarton.setSTATUS("1");
                    leisaiWmsCarton.setCREATEBY("MES");
                    leisaiWmsCarton.setCREATEDATE(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
                    leisaiWmsCarton.setCLIENT("93");
                    leisaiWmsCarton.setPRINTCOUNT("1");
                    leisaiWmsCarton.setPRINTBY("MES");
                    leisaiWmsCarton.setPRINTDATE(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));

                    for (MesSfcBarcodeProcess sfcBarcodeProcess : mesSfcBarcodeProcessList) {
                        LeisaiWmsCartonDet leisaiWmsCartonDet=new LeisaiWmsCartonDet();
                        leisaiWmsCartonDet.setBOXID(sfcProductCarton.getCartonCode());
                        leisaiWmsCartonDet.setBARCODENO(sfcBarcodeProcess.getBarcode());
                        leisaiWmsCartonDet.setPN(sfcBarcodeProcess.getMaterialCode());
                        leisaiWmsCartonDet.setORDERNO(sfcBarcodeProcess.getWorkOrderCode());
                        leisaiWmsCartonDet.setCLIENT("93");
                        leisaiWmsCartonDet.setCREATEBY("MES");
                        leisaiWmsCartonDet.setCREATEDATE(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));

                        dets.add(leisaiWmsCartonDet);

                    }
                    leisaiWmsCarton.setLeisaiWmsCartonDetList(dets);
                    leisaiFeignApi.syncCartonData(leisaiWmsCarton);
                }
            }

            //雷赛包箱数据是否同步到WMS结束
        }

        logger.info("=========包箱过站，执行总时长：{}",System.currentTimeMillis() - startTime);

        // TODO 方便测试使用，测试需要删除
        if (true) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012029);
        }

        return true;
    }

    @Override
    public PdaCartonRecordDto findLastCarton(Long processId, Long stationId, String packType) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("closeStatus", 0);
        List<MesSfcProductCartonDto> sfcProductCartonList = mesSfcProductCartonService.findList(map);
        if (sfcProductCartonList.isEmpty()) {
            return new PdaCartonRecordDto();
        }
        // 组装
        return buildCartonData(sfcProductCartonList.get(0), packType);
    }

    @Override
    public int updateCartonDescNum(Long productCartonId, BigDecimal cartonDescNum, String packType, Boolean print, String printName) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(productCartonId);
        SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = SearchMesSfcBarcodeProcess.builder()
                .cartonCode(mesSfcProductCarton.getCartonCode())
                .build();
        if ("1".equals(packType)) {
            searchMesSfcBarcodeProcess.setWorkOrderId(mesSfcProductCarton.getWorkOrderId());
        } else if ("2".equals(packType)) {
            searchMesSfcBarcodeProcess.setMaterialId(mesSfcProductCarton.getMaterialId());
        }
        List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(searchMesSfcBarcodeProcess);
        if (cartonDescNum.compareTo(new BigDecimal(mesSfcBarcodeProcessList.size())) == -1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012015);
        }
        int compare = new BigDecimal(mesSfcBarcodeProcessList.size()).compareTo(cartonDescNum);
        if (compare == 0) {
            // 包箱已满，关箱
            mesSfcProductCarton.setNowPackageSpecQty(cartonDescNum);
            mesSfcProductCarton.setCloseStatus((byte) 1);
            mesSfcProductCarton.setCloseCartonUserId(user.getUserId());
            mesSfcProductCarton.setCloseCartonTime(new Date());
            mesSfcProductCarton.setModifiedUserId(user.getUserId());
            mesSfcProductCarton.setModifiedTime(new Date());
            int update = mesSfcProductCartonService.update(mesSfcProductCarton);
            if (print && update > 0) {
                // 关箱后才能打印条码
                BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                        .barcode(mesSfcProductCarton.getCartonCode())
                        .labelTypeCode("09")
                        .workOrderId(mesSfcProductCarton.getWorkOrderId())
                        .printName(printName != null ? printName : "测试")
                        .build());
            }
            return update;
        } else {
            mesSfcProductCarton.setNowPackageSpecQty(cartonDescNum);
            return mesSfcProductCartonService.update(mesSfcProductCarton);
        }
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

    @Override
    public int closeCarton(CloseCartonDto dto) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // 获取工位工序的包箱
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", dto.getStationId());
        map.put("closeStatus", 0);
        List<MesSfcProductCartonDto> sfcProductCartonDtos = mesSfcProductCartonService.findList(map);
        MesSfcProductCartonDto productCartonDto = sfcProductCartonDtos.get(0);

        /*// 校验附件码是否已满足
        if(dto.getAnnex()){
            SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo = new SearchMesPmWorkOrderProcessReWo();
            searchMesPmWorkOrderProcessReWo.setMaterialId(productCartonDto.getMaterialId().toString());
            searchMesPmWorkOrderProcessReWo.setProcessId(dto.getProcessId().toString());
            searchMesPmWorkOrderProcessReWo.setWorkOrderId(productCartonDto.getWorkOrderId().toString());
            // 产品关键物料清单 有且只有一条
            List<MesPmWorkOrderProcessReWoDto> pmWorkOrderProcessReWoDtoList = pmFeignApi.findPmWorkOrderProcessReWoList(searchMesPmWorkOrderProcessReWo).getData();
            if (pmWorkOrderProcessReWoDtoList == null || pmWorkOrderProcessReWoDtoList.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012016);
            }
            MesPmWorkOrderProcessReWoDto pmWorkOrderProcessReWoDto = pmWorkOrderProcessReWoDtoList.get(0);
            List<MesPmWorkOrderMaterialRePDto> pmWorkOrderMaterialRePDtoList = pmWorkOrderProcessReWoDto.getList();
            // 零件所有用量
            BigDecimal usageQty = pmWorkOrderMaterialRePDtoList.stream().map(item -> item.getUsageQty()).reduce(BigDecimal::multiply).get();

            // 包箱里所有条码
            map.clear();
            map.put("closeStatus", 0);
            List<MesSfcProductCartonDetDto> relationList = mesSfcProductCartonDetService.findRelationList(map);
            for (MesSfcProductCartonDetDto productCartonDetDto: relationList) {
                // 关键部件物料清单
                map.clear();
                map.put("workOrderId", productCartonDto.getWorkOrderId());
                map.put("processId", dto.getProcessId());
                map.put("materialId", productCartonDto.getMaterialId());
                map.put("workOrderBarcodeId", productCartonDetDto.getWorkOrderBarcodeId());
                List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
                if (keyPartRelevanceDtos.size() < usageQty.intValue()) {
                    // 条码附件码数量不齐全，不可关闭
                    throw new BizErrorException(ErrorCodeEnum.PDA40012025);
                }
            }
        }*/

        MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(dto.getProductCartonId());
        mesSfcProductCarton.setCloseStatus((byte) 1);
        mesSfcProductCarton.setCloseCartonUserId(user.getUserId());
        mesSfcProductCarton.setCloseCartonTime(new Date());
        mesSfcProductCarton.setModifiedUserId(user.getUserId());
        mesSfcProductCarton.setModifiedTime(new Date());
        int update = mesSfcProductCartonService.update(mesSfcProductCarton);
        if (dto.getPrint() && update > 0) {
            // 关箱后才能打印条码
            BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                    .barcode(productCartonDto.getCartonCode())
                    .labelTypeCode("09")
                    .workOrderId(productCartonDto.getWorkOrderId())
                    .printName(dto.getPrintName() != null ? dto.getPrintName() : "测试")
                    .build());
        }
        return update;
    }

    // region 公用方法

    /**
     * 组装包箱数据
     *
     * @param mesSfcProductCarton
     * @return
     */
    private PdaCartonRecordDto buildCartonData(MesSfcProductCarton mesSfcProductCarton, String packType) {

        MesPmWorkOrder mesPmWorkOrderByBarCode = pmFeignApi.workOrderDetail(mesSfcProductCarton.getWorkOrderId()).getData();
        ResponseEntity<BaseMaterial> materialResponseEntity = baseFeignApi.materialDetail(mesSfcProductCarton.getMaterialId());
        if (materialResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012024, mesSfcProductCarton.getMaterialId());
        }
        // 产品物料信息
        BaseMaterial baseMaterial = materialResponseEntity.getData();
        SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = SearchMesSfcBarcodeProcess.builder().build();
        if ("1".equals(packType)) {
            searchMesSfcBarcodeProcess.setWorkOrderId(mesPmWorkOrderByBarCode.getWorkOrderId());
        } else if ("2".equals(packType)) {
            searchMesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderByBarCode.getMaterialId());
        }
        List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(searchMesSfcBarcodeProcess);
        // 已扫包箱
        Map<String, List<MesSfcBarcodeProcess>> barcodeProcessGroup = mesSfcBarcodeProcessList.stream().filter(item -> StringUtils.isNotEmpty(item.getCartonCode())).collect(Collectors.groupingBy(MesSfcBarcodeProcess::getCartonCode));
        // 扫描数量
        List<MesSfcBarcodeProcessDto> dtos = new ArrayList<>();
        List<MesSfcBarcodeProcess> barcodeProcessList = barcodeProcessGroup.get(mesSfcProductCarton.getCartonCode());
        if (barcodeProcessList != null && barcodeProcessList.size() > 0) {
            Example example = new Example(MesSfcKeyPartRelevance.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("workOrderBarcodeId", barcodeProcessList.stream().map(MesSfcBarcodeProcess::getWorkOrderBarcodeId).collect(Collectors.toList()));
            List<MesSfcKeyPartRelevance> sfcKeyPartRelevanceList = mesSfcKeyPartRelevanceService.selectByExample(example);
            for (MesSfcBarcodeProcess mesSfcBarcodeProcess1 : barcodeProcessList) {
                MesSfcBarcodeProcessDto dto = new MesSfcBarcodeProcessDto();
                BeanUtils.copyProperties(mesSfcBarcodeProcess1, dto);
                dto.setSfcKeyPartRelevanceList(sfcKeyPartRelevanceList.stream().filter(item -> item.getWorkOrderBarcodeId().equals(dto.getWorkOrderBarcodeId())).collect(Collectors.toList()));
                dtos.add(dto);
            }
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
                .scansNum(barcodeProcessList != null ? barcodeProcessList.size() : 0)
                .barcodeProcessList(dtos)
                .build();
    }

    /**
     * 通过物料、工序获取包装规格
     * 产品设计的时候当前配置在物料、工序下只能有一条包装规格
     *
     * @param materialId
     * @param processId
     * @return
     */
    private BasePackageSpecificationDto getPackageSpecification(Long materialId, Long processId) {
        // 查询包装规格
        SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
        searchBasePackageSpecification.setMaterialId(materialId);
        searchBasePackageSpecification.setProcessId(processId);
        List<BasePackageSpecificationDto> packageSpecificationDtos = BarcodeUtils.findByMaterialProcess(searchBasePackageSpecification);
        if (packageSpecificationDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        return packageSpecificationDtos.get(0);
    }

    /**
     * 保存包箱数据以及包箱条码关系
     *
     * @param cartonCode
     * @param userId
     * @param orgId
     * @param stationId
     * @param workOrderId
     * @param cartonNum
     * @param materialId
     * @return
     */
    private MesSfcProductCarton saveCarton(String cartonCode, Long userId, Long orgId, Long stationId, Long workOrderId, BigDecimal cartonNum, Long materialId) {
        // 添加包箱表数据
        MesSfcProductCarton sfcProductCarton = MesSfcProductCarton.builder()
                .cartonCode(cartonCode)
                .nowPackageSpecQty(cartonNum)
                .workOrderId(workOrderId)
                .materialId(materialId)
                .stationId(stationId)
                .closeStatus((byte) 0)
                .closeCartonUserId(userId)
                .closeCartonTime(new Date())
                .orgId(orgId)
                .createTime(new Date())
                .createUserId(userId)
                .isDelete((byte) 1)
                .build();
        int saveRes = mesSfcProductCartonMapper.insertUseGeneratedKeys(sfcProductCarton);
        if (saveRes > 0) {
            return sfcProductCarton;
        } else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
    }

}
