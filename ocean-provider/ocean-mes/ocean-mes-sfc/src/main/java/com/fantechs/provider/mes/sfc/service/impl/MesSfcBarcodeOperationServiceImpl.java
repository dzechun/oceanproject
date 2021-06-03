package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonMapper;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    @Override
    public int pdaCartonWork(PdaCartonWorkDto dto) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        // 1、检查条码、工单状态、排程
        BarcodeUtils.checkSN(CheckProductionDto.builder()
                .barCode(dto.getBarCode())
                .stationId(dto.getStationId())
                .processId(dto.getProcessId())
                .checkOrNot(dto.getCheckOrNot())
//                .workOrderId(dto.getWorkOrderId())
                .packType(dto.getPackType())
                .build());

        // 2、校验条码是否关联包箱
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(dto.getBarCode())
                        .build());
        MesSfcWorkOrderBarcodeDto orderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", orderBarcodeDto.getWorkOrderBarcodeId());
        List<MesSfcProductCartonDetDto> productCartonDetDtos = mesSfcProductCartonDetService.findList(map);
        if(productCartonDetDtos != null && productCartonDetDtos.size() > 0){
            throw new BizErrorException(ErrorCodeEnum.PDA40012013);
        }

        return 0;
    }

    @Override
    public int pdaPutIntoProduction(PdaPutIntoProductionDto vo) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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
                .nowStationId(vo.getStationId())
                .workOrderId(mesPmWorkOrder.getWorkOrderId())
                .build();
        // 3、判断首条码，若是则更新工单状态
        if (mesPmWorkOrder.getWorkOrderStatus().equals("0") || mesPmWorkOrder.getWorkOrderStatus().equals("1")) {
            mesPmWorkOrder.setWorkOrderStatus(Byte.valueOf("2"));
            pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
            updateProcessDto.setNowProcessId(vo.getProcessId());
        } else {
            MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .barcode(vo.getBarCode())
                    .build());
            updateProcessDto.setNowProcessId(mesSfcBarcodeProcess.getNextProcessId());
        }
        // 4、更新下一工序，增加工序记录
        return BarcodeUtils.updateProcess(updateProcessDto);
    }

    @Override
    public PdaCartonRecordDto findLastCarton(Long processId, Long stationId, String packType) {
        Example example = new Example(MesSfcProductCarton.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationId", stationId);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductCarton> sfcProductCartonList = mesSfcProductCartonService.selectByExample(example);
        if (sfcProductCartonList.isEmpty()) {
            return null;
        }
        // 组装
        return buildCartonData(sfcProductCartonList.get(0), packType);
    }

    @Override
    public int cartonOperation(PdaCartonDto vo) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return barCodeOperation(vo, user, false);
    }

    @Override
    public int cartonAnnexOperation(PdaCartonAnnexDto vo) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(vo.getCartonCode())) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012026);
        }
        // 校验附件码有没有被使用
        Example example = new Example(MesSfcKeyPartRelevance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationCode", vo.getBarAnnexCode());
        int countByExample = mesSfcKeyPartRelevanceService.selectCountByExample(example);
        if (countByExample > 0) {
            // 提示该附件码已绑定
            throw new BizErrorException(ErrorCodeEnum.PDA40012028);
        }
        // 校验附件码是否要绑定当前产品
        // 条码所属工单
        MesPmWorkOrder mesPmWorkOrderByBarCode = pmFeignApi.workOrderDetail(vo.getWorkOrderId()).getData();
        List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(vo.getBarCode())
                        .build());
        // 条码
        MesSfcWorkOrderBarcodeDto workOrderBarcodeDto = workOrderBarcodeDtos.get(0);

        SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo = new SearchMesPmWorkOrderProcessReWo();
        searchMesPmWorkOrderProcessReWo.setMaterialId(mesPmWorkOrderByBarCode.getMaterialId().toString());
        searchMesPmWorkOrderProcessReWo.setProcessId(vo.getProcessId().toString());
        searchMesPmWorkOrderProcessReWo.setWorkOrderId(mesPmWorkOrderByBarCode.getWorkOrderId().toString());
        // 产品关键物料清单 有且只有一条
        List<MesPmWorkOrderProcessReWoDto> pmWorkOrderProcessReWoDtoList = pmFeignApi.findPmWorkOrderProcessReWoList(searchMesPmWorkOrderProcessReWo).getData();
        if(pmWorkOrderProcessReWoDtoList == null || pmWorkOrderProcessReWoDtoList.size() <= 0){
            throw new BizErrorException(ErrorCodeEnum.PDA40012016);
        }
        // 关键部件物料清单
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderId", mesPmWorkOrderByBarCode.getWorkOrderId());
        map.put("processId", vo.getProcessId());
        map.put("materialId", mesPmWorkOrderByBarCode.getMaterialId());
        map.put("workOrderBarcodeId", workOrderBarcodeDto.getWorkOrderBarcodeId());
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);

        MesPmWorkOrderProcessReWoDto pmWorkOrderProcessReWoDto = pmWorkOrderProcessReWoDtoList.get(0);
        List<MesPmWorkOrderMaterialRePDto> pmWorkOrderMaterialRePDtoList = pmWorkOrderProcessReWoDto.getList();
        BigDecimal usageQty = pmWorkOrderMaterialRePDtoList.stream().map(item -> item.getUsageQty()).reduce(BigDecimal::multiply).get();
        // 关键部件物料数量大于等于用量
        if(keyPartRelevanceDtos.size() >= usageQty.intValue()){
            throw new BizErrorException(ErrorCodeEnum.PDA40012020);
        }

        BaseProLine proLine = baseFeignApi.selectProLinesDetail(vo.getProLineId()).getData();
        BaseProcess baseProcess = baseFeignApi.processDetail(vo.getProcessId()).getData();
        BaseStation baseStation = baseFeignApi.findStationDetail(vo.getStationId()).getData();

        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(vo.getBarAnnexCode())
                        .build());
        if (mesSfcWorkOrderBarcodeDtos.size() <= 0) {
            List<Long> materialIds = new ArrayList<>();
            for(MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList){
                if(pmWorkOrderMaterialRePDto.getMaterialId() != null){
                    materialIds.add(pmWorkOrderMaterialRePDto.getMaterialId());
                }
            }
            SearchBaseSignature baseSignature = new SearchBaseSignature();
            baseSignature.setMaterialIds(materialIds);
            // 查找物料特征码
            List<BaseSignature> signatureList = baseFeignApi.findSignatureList(baseSignature).getData();
            boolean flag = true;
            for (BaseSignature signature : signatureList){
                int material_count = 0;
                for(MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList){
                    if(signature.getMaterialId().equals(pmWorkOrderMaterialRePDto.getMaterialId())){
                        material_count = pmWorkOrderMaterialRePDto.getUsageQty().intValue();
                        break;
                    }
                }
                if(material_count > 0){
                    int keyPartCount = 0;
                    for(MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos){
                        if(signature.getMaterialId().equals(keyPartRelevanceDto.getMaterialId())){
                            keyPartCount = keyPartCount + 1;
                        }
                    }
                    if(keyPartCount >= material_count){
                        // 当前零件料号已满，检查下个零件料号料
                        continue;
                    }
                    // 校验附件码是否符合特征码规则
                    Pattern pattern = Pattern.compile(signature.getSignatureRegex());
                    Matcher matcher = pattern.matcher(vo.getBarAnnexCode());
                    if(!matcher.matches()){
                        // 该附件码不满足特征码，检查零件料号特征码
                        continue;
                    }
                    // 添加keyPart表关系
                    // 绑定附件码跟条码关系
                    mesSfcKeyPartRelevanceService.save(MesSfcKeyPartRelevance.builder()
                            .barcodeCode(vo.getBarCode())
                            .workOrderId(vo.getWorkOrderId())
                            .workOrderCode(mesPmWorkOrderByBarCode.getWorkOrderCode())
                            .workOrderBarcodeId(workOrderBarcodeDto.getWorkOrderBarcodeId())
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
                            .materialCode(signature.getMaterialCode())
                            .materialName(signature.getMaterialName())
                            .materialVer(signature.getMaterialVersion())
                            .partBarcode(vo.getBarAnnexCode())
                            .operatorUserId(user.getUserId())
                            .operatorTime(new Date())
                            .orgId(user.getOrganizationId())
                            .createTime(new Date())
                            .createUserId(user.getUserId())
                            .isDelete((byte) 1)
                            .build());
                    flag = false;
                    break;
                }
            }
            if(flag){
                throw new BizErrorException(ErrorCodeEnum.PDA40012031);
            }
        }else {
            for (MesSfcWorkOrderBarcodeDto barcodeDto : mesSfcWorkOrderBarcodeDtos) {
                int material_count = 0;
                for(MesPmWorkOrderMaterialRePDto pmWorkOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList){
                    if(barcodeDto.getLabelCategoryId().equals(pmWorkOrderMaterialRePDto.getLabelCategoryId())){
                        material_count = pmWorkOrderMaterialRePDto.getUsageQty().intValue();
                        break;
                    }
                }
                if(material_count > 0){
                    int keyPartCount = 0;
                    for(MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos){
                        if(barcodeDto.getLabelCategoryId().equals(keyPartRelevanceDto.getLabelCategoryId())){
                            keyPartCount = keyPartCount + 1;
                        }
                    }
                    if(keyPartCount >= material_count){
                        throw new BizErrorException(ErrorCodeEnum.PDA40012029);
                    }
                    // 添加keyPart表关系
                    // 绑定附件码跟条码关系
                    mesSfcKeyPartRelevanceService.save(MesSfcKeyPartRelevance.builder()
                            .barcodeCode(vo.getBarCode())
                            .workOrderId(vo.getWorkOrderId())
                            .workOrderCode(mesPmWorkOrderByBarCode.getWorkOrderCode())
                            .workOrderBarcodeId(workOrderBarcodeDto.getWorkOrderBarcodeId())
                            .proLineId(vo.getProLineId())
                            .proCode(proLine.getProCode())
                            .proName(proLine.getProName())
                            .processId(vo.getProcessId())
                            .processCode(baseProcess.getProcessCode())
                            .processName(baseProcess.getProcessName())
                            .stationId(vo.getStationId())
                            .stationCode(baseStation.getStationCode())
                            .stationName(baseStation.getStationName())
                            .labelCategoryId(barcodeDto.getLabelCategoryId())
                            .partBarcode(vo.getBarAnnexCode())
                            .operatorUserId(user.getUserId())
                            .operatorTime(new Date())
                            .orgId(user.getOrganizationId())
                            .createTime(new Date())
                            .createUserId(user.getUserId())
                            .isDelete((byte) 1)
                            .build());

                }
            }
        }

        // 走条码过站流程
        return barCodeOperation(vo, user, true);
    }

    @Override
    public int updateCartonDescNum(Long productCartonId, BigDecimal cartonDescNum, String packType) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(productCartonId);
        SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = SearchMesSfcBarcodeProcess.builder()
                .cartonCode(mesSfcProductCarton.getCartonCode())
                .build();
        if("1".equals(packType)){
            searchMesSfcBarcodeProcess.setWorkOrderId(mesSfcProductCarton.getWorkOrderId());
        }else if ("2".equals(packType)){
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
            return mesSfcProductCartonService.update(mesSfcProductCarton);
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
        if("1".equals(packType)){
            searchMesSfcBarcodeProcess.setWorkOrderId(mesPmWorkOrderByBarCode.getWorkOrderId());
        }else if ("2".equals(packType)){
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
     * 包箱作业
     *
     * @param vo
     * @param user
     * @return
     * @throws Exception
     */
    private int barCodeOperation(PdaCartonDto vo, SysUser user, Boolean source) throws Exception {

        // 1、检查条码、工单状态、排程
        BarcodeUtils.checkSN(CheckProductionDto.builder()
                .barCode(vo.getBarCode())
                .stationId(vo.getStationId())
                .processId(vo.getProcessId())
                .checkOrNot(vo.getCheckOrNot())
                .workOrderId(vo.getWorkOrderId())
                .packType(vo.getPackType())
                .build());

        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(vo.getBarCode())
                        .build());
        MesSfcWorkOrderBarcodeDto sfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        // 条码所属工单
        MesPmWorkOrder mesPmWorkOrderByBarCode = pmFeignApi.workOrderDetail(sfcWorkOrderBarcodeDto.getWorkOrderId()).getData();

        // 箱码对应状态表
        MesSfcProductCarton mesSfcProductCarton = null;
        // 条码对应工序
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .barcode(vo.getBarCode())
                .nextProcessId(vo.getProcessId())
                .build());

        // 3、判断是否已有箱码，生成箱码
        if (StringUtils.isEmpty(vo.getCartonCode())) {
            // 没有未关闭包箱，重新生成并初始化
            String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrderByBarCode.getMaterialId(),
                    vo.getProcessId(),
                    mesSfcBarcodeProcess.getMaterialCode(),
                    mesPmWorkOrderByBarCode.getWorkOrderId(),
                    "09");
            vo.setCartonCode(cartonCode);
            // 查询基础资料包装规格
            BasePackageSpecificationDto packageSpecificationDto = getPackageSpecification(mesSfcBarcodeProcess.getMaterialId(), vo.getProcessId());
            vo.setCartonNum(packageSpecificationDto.getPackageSpecificationQuantity());
            // 添加包箱表数据
            mesSfcProductCarton = saveCarton(cartonCode, user.getUserId(), user.getOrganizationId(), vo.getStationId(), sfcWorkOrderBarcodeDto.getWorkOrderId(), vo.getCartonNum(), mesPmWorkOrderByBarCode.getMaterialId());
            vo.setProductCartonId(mesSfcProductCarton.getProductCartonId());
        } else {
            mesSfcProductCarton = mesSfcProductCartonService.selectByKey(vo.getProductCartonId());

            // 4、包箱已关闭，生成箱码并添加新的箱码状态数据
            if (mesSfcProductCarton.getCloseStatus().equals(1)) {
                String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrderByBarCode.getMaterialId(),
                        vo.getProcessId(),
                        mesSfcBarcodeProcess.getMaterialCode(),
                        mesPmWorkOrderByBarCode.getWorkOrderId(),
                        "09");
                vo.setCartonCode(cartonCode);
                // 查询基础资料包装规格
                BasePackageSpecificationDto packageSpecificationDto = getPackageSpecification(mesSfcBarcodeProcess.getMaterialId(), vo.getProcessId());
                vo.setCartonNum(packageSpecificationDto.getPackageSpecificationQuantity());
                // 添加包箱表数据
                mesSfcProductCarton = saveCarton(cartonCode, user.getUserId(), user.getOrganizationId(), vo.getStationId(), sfcWorkOrderBarcodeDto.getWorkOrderId(), vo.getCartonNum(), mesPmWorkOrderByBarCode.getMaterialId());
                vo.setProductCartonId(mesSfcProductCarton.getProductCartonId());
            }
        }

        // 6、不扫附件码则直接过站
        if (!vo.getAnnex() || source) {
            UpdateProcessDto updateProcessDto = UpdateProcessDto.builder()
                    .badnessPhenotypeCode("N/A")
                    .barCode(vo.getBarCode())
                    .equipmentId("N/A")
                    .operatorUserId(user.getUserId())
                    .proLineId(mesPmWorkOrderByBarCode.getProLineId())
                    .routeId(mesPmWorkOrderByBarCode.getRouteId())
                    .nowProcessId(vo.getProcessId())
                    .nowStationId(vo.getStationId())
                    .workOrderId(mesPmWorkOrderByBarCode.getWorkOrderId())
                    .passCode(vo.getCartonCode())
                    .passCodeType((byte) 1)
                    .build();

            // 保存条码包箱关系
            mesSfcProductCartonDetService.save(MesSfcProductCartonDet.builder()
                    .productCartonId(mesSfcProductCarton.getProductCartonId())
                    .workOrderBarcodeId(sfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                    .orgId(user.getOrganizationId())
                    .createTime(new Date())
                    .createUserId(user.getUserId())
                    .isDelete((byte) 1)
                    .build());
            // 更新下一工序，增加工序记录
            BarcodeUtils.updateProcess(updateProcessDto);

            // 6.1、判断是否包箱已满，关箱
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder().cartonCode(vo.getCartonCode()).build());
            if (mesSfcBarcodeProcessList.size() >= vo.getCartonNum().intValue()) {
                if (vo.getPrint()) {
                    // 关箱后台才能打印条码
                    BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                            .barcode(vo.getCartonCode())
                            .labelTypeCode("09")
                            .labelCategoryId(sfcWorkOrderBarcodeDto.getLabelCategoryId())
                            .workOrderId(sfcWorkOrderBarcodeDto.getWorkOrderId())
                            .build());
                }
                // 包箱已满，关箱
                mesSfcProductCarton.setCloseStatus((byte) 1);
                mesSfcProductCarton.setCloseCartonUserId(user.getUserId());
                mesSfcProductCarton.setCloseCartonTime(new Date());
                mesSfcProductCarton.setModifiedUserId(user.getUserId());
                mesSfcProductCarton.setModifiedTime(new Date());
                return mesSfcProductCartonService.update(mesSfcProductCarton);
            }
        }else {
            if(!vo.getAnnex()){
                // 保存条码包箱关系
                mesSfcProductCartonDetService.save(MesSfcProductCartonDet.builder()
                        .productCartonId(mesSfcProductCarton.getProductCartonId())
                        .workOrderBarcodeId(sfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                        .orgId(user.getOrganizationId())
                        .createTime(new Date())
                        .createUserId(user.getUserId())
                        .isDelete((byte) 1)
                        .build());
            }
            // 保存包箱号
            mesSfcBarcodeProcess.setStationId(vo.getStationId());
            mesSfcBarcodeProcess.setCartonCode(vo.getCartonCode());
            mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);
        }

        return 1;
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
        List<BasePackageSpecificationDto> packageSpecificationDtos = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
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
