package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.wms.inner.NotOrderInStorage;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.wanbao.WanbaoFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MesSfcBarcodeOperationServiceImpl implements MesSfcBarcodeOperationService {


    // region service注入

    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private MesSfcWorkOrderBarcodeMapper mesSfcWorkOrderBarcodeMapper;
    @Resource
    private MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    private MesSfcProductCartonDetService mesSfcProductCartonDetService;
    @Resource
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
    @Resource
    private MesSfcProductCartonMapper mesSfcProductCartonMapper;
    @Resource
    private MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    InFeignApi inFeignApi;
    @Resource
    InnerFeignApi innerFeignApi;
    @Resource
    WanbaoFeignApi wanbaoFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;

    // endregion

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public Boolean pdaCartonWork(PdaCartonWorkDto dto) throws Exception {
        long start = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        // 2022-03-08 判断是否质检完成之后走产线入库
        WmsInnerInventoryDet innerInventoryDet = innerFeignApi.findByDet(dto.getBarCode()).getData();
        if (StringUtils.isNotEmpty(innerInventoryDet)){
            this.scanBarocodeByQmsFinish(dto.getBarCode(), innerInventoryDet);
            return true;
        }

        long InventoryDet = System.currentTimeMillis();
        log.info("=========== 判断是否质检完成之后走产线入库耗时:" + (InventoryDet - start));

        String customerBarcode = null;
        if (dto.getBarCode().length() != 23){
            // 判断是三星客户条码还是厂内码
            Example example = new Example(MesSfcBarcodeProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("customerBarcode", dto.getBarCode() + "%");
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
            if (mesSfcBarcodeProcesses.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "厂内码不存在或未扫描到厂内码");
            }
            customerBarcode = dto.getBarCode();
            MesSfcBarcodeProcess barcodeProcess = mesSfcBarcodeProcesses.get(0);
            dto.setBarCode(barcodeProcess.getBarcode());
        }

        long custBarcode = System.currentTimeMillis();
        log.info("=========== 判断是三星客户条码还是厂内码耗时:" + (custBarcode-InventoryDet));

        // 条码生产订单条码表
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(dto.getBarCode())
                        .build());
        if (mesSfcWorkOrderBarcodeDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        if (mesSfcWorkOrderBarcodeDtos.size() > 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012001);
        }

        long WorkOrderBarcode = System.currentTimeMillis();
        log.info("=========== 查询生产订单条码表耗时 :" + (WorkOrderBarcode-custBarcode));

        /*
         * 流转卡状态(0-待投产 1-投产中 2-已完成 3-待打印)
         */
        MesSfcWorkOrderBarcodeDto orderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        if (StringUtils.isNotEmpty(orderBarcodeDto.getBarcodeStatus()) && (orderBarcodeDto.getBarcodeStatus() == 2 || orderBarcodeDto.getBarcodeStatus() == 3)) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012004, orderBarcodeDto.getBarcodeStatus() == 2 ? "已完成" : "待打印");
        }
        // 条码对应工单
        MesPmWorkOrder mesPmWorkOrder = pmFeignApi.workOrderDetail(orderBarcodeDto.getWorkOrderId()).getData();
        if (4 == mesPmWorkOrder.getWorkOrderStatus() || 5 == mesPmWorkOrder.getWorkOrderStatus()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012006);
        }
        if (mesPmWorkOrder.getProductionQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012007, mesPmWorkOrder.getWorkOrderCode());
        }
        if (!mesPmWorkOrder.getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.PDA40012007.getCode(), "作业配置的产线与工单上的产线不符，不可操作");
        }

        long workorderTime = System.currentTimeMillis();
        log.info("=========== 查询工单耗时 :" + (workorderTime-WorkOrderBarcode));

        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderBarcodeId(orderBarcodeDto.getWorkOrderBarcodeId())
                .build());
        if(StringUtils.isEmpty(mesSfcBarcodeProcess)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012002.getCode(), "该条码未生成或条码过站数据不存在");
        }
        if (!mesSfcBarcodeProcess.getNextProcessId().equals(dto.getProcessId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "配置工序与条码所处工序不匹配");
        }
        if (!mesSfcBarcodeProcess.getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码产线跟该工位产线不匹配");
        }
        // 已完成所有过站工序
        if (mesSfcBarcodeProcess.getNextProcessId().equals(0L)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码已完成所有工序过站");
        }
        // 是否已不良
        if (StringUtils.isNotEmpty(mesSfcBarcodeProcess.getBarcodeStatus()) && mesSfcBarcodeProcess.getBarcodeStatus().equals((byte)0)){
            throw new BizErrorException("该产品条码已不良 不可继续");
        }

        long BarcodeProcess = System.currentTimeMillis();
        log.info("=========== 查询条码对应过站记录信息耗时 :" + (BarcodeProcess-workorderTime));

        // 查询包装规格信息
        BasePackageSpecificationDto packageSpecificationDto = getPackageSpecification(mesSfcBarcodeProcess.getMaterialId(), dto.getProcessId());
        long CartonDetDto = System.currentTimeMillis();
        log.info("=========== 查询包装规格信息耗时 :" + (CartonDetDto-BarcodeProcess));

        //产线
        BaseProLine proLine = baseFeignApi.selectProLinesDetail(mesPmWorkOrder.getProLineId()).getData();
        //工序
        BaseProcess  baseProcess = baseFeignApi.processDetail(dto.getProcessId()).getData();
        //工位
        BaseStation baseStation = baseFeignApi.findStationDetail(dto.getStationId()).getData();

        if(StringUtils.isEmpty(proLine,baseProcess,baseStation,mesSfcWorkOrderBarcodeDtos,mesPmWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);

        }
        long selectBaseTime = System.currentTimeMillis();
        log.info("=========== 查询基础数据信息耗时 :" + (selectBaseTime-CartonDetDto));

        Map<String, Object> map = new HashMap<>();
        // 5、是否要扫附件码
        if (dto.getAnnex()) {
            long CartonDto = System.currentTimeMillis();
            if(StringUtils.isEmpty(dto.getBarAnnexCode())){
                return false;
            }
            if (dto.getBarCode().equals(dto.getBarAnnexCode())){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "厂内码与附件码重复扫描");
            }
            Example example = new Example(MesSfcKeyPartRelevance.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("partBarcode", dto.getBarAnnexCode());
            int countByExample = mesSfcKeyPartRelevanceService.selectCountByExample(example);
            if (countByExample > 0) {
                // 提示该附件码已绑定
                throw new BizErrorException(ErrorCodeEnum.PDA40012028);
            }
            long checkAnnexTime = System.currentTimeMillis();
            log.info("=========== 校验附件码耗时 :" + (checkAnnexTime-CartonDto));

            SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo = new SearchMesPmWorkOrderProcessReWo();
            searchMesPmWorkOrderProcessReWo.setMaterialId(mesPmWorkOrder.getMaterialId().toString());
            searchMesPmWorkOrderProcessReWo.setProcessId(dto.getProcessId().toString());
            searchMesPmWorkOrderProcessReWo.setWorkOrderId(mesPmWorkOrder.getWorkOrderId().toString());
            // 产品关键物料清单 有且只有一条
            List<MesPmWorkOrderProcessReWoDto> pmWorkOrderProcessReWoDtoList = pmFeignApi.findPmWorkOrderProcessReWoList(searchMesPmWorkOrderProcessReWo).getData();
            if (pmWorkOrderProcessReWoDtoList == null || pmWorkOrderProcessReWoDtoList.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012016);
            }
            MesPmWorkOrderProcessReWoDto pmWorkOrderProcessReWoDto = pmWorkOrderProcessReWoDtoList.get(0);
            List<MesPmWorkOrderMaterialRePDto> pmWorkOrderMaterialRePDtoList = pmWorkOrderProcessReWoDto.getList();
            if (pmWorkOrderMaterialRePDtoList == null || pmWorkOrderMaterialRePDtoList.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012016.getCode(), "条码的工单关键物料清单未配置");
            }
            // 零件所有用量
            BigDecimal usageQty = BigDecimal.ZERO;
            for (MesPmWorkOrderMaterialRePDto workOrderMaterialRePDto : pmWorkOrderMaterialRePDtoList) {
                usageQty = usageQty.add(workOrderMaterialRePDto.getUsageQty());
            }

            long checkrewo = System.currentTimeMillis();
            log.info("=========== 校验关键物料清单耗时 :" + (checkrewo-checkAnnexTime));

            // 关键部件物料清单
            map.clear();
            map.put("workOrderId", mesPmWorkOrder.getWorkOrderId());
            map.put("processId", dto.getProcessId());
            map.put("workOrderBarcodeId", orderBarcodeDto.getWorkOrderBarcodeId());
            List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);

            // 关键部件物料数量大于等于用量
            if (keyPartRelevanceDtos.size() >= usageQty.intValue()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012020);
            }

            long checkKeyPart = System.currentTimeMillis();
            log.info("=========== 校验关键部件物料清单耗时 :" + (checkKeyPart-checkrewo));

            // 查找该附件码是否存在系统中
            MesSfcWorkOrderBarcode barcodeDto = mesSfcWorkOrderBarcodeService.findBarcode(dto.getBarAnnexCode());
            if (StringUtils.isEmpty(barcodeDto)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该附件码不存在系统中");
            }
            if (barcodeDto.getLabelCategoryId().equals(mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"))){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该附件码为厂内码类型，不可扫码");
            }

            long checkAnnerExists = System.currentTimeMillis();
            log.info("=========== 校验附件码是否存在耗时 :" + (checkAnnerExists-checkKeyPart));

            BigDecimal material_count = pmWorkOrderMaterialRePDtoList.stream()
                    .filter(i -> barcodeDto.getLabelCategoryId().equals(i.getLabelCategoryId()) && StringUtils.isNotEmpty(i.getUsageQty()))
                    .map(MesPmWorkOrderMaterialReP::getUsageQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            boolean isWork = false;
            if (material_count.compareTo(BigDecimal.ZERO) == 1) {
                long keyPartCount = keyPartRelevanceDtos.stream()
                        .filter(i -> barcodeDto.getLabelCategoryId().equals(i.getLabelCategoryId()))
                        .count();

                if (keyPartCount >= material_count.intValue()) {
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
                isWork = false;

                SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
                searchOmSalesCodeReSpc.setSamePackageCodeStatus((byte) 1);
                searchOmSalesCodeReSpc.setMaterialId(mesPmWorkOrder.getMaterialId());
                List<OmSalesCodeReSpcDto> codeReSpcDtos = omFeignApi.findAll(searchOmSalesCodeReSpc).getData();
                // 同个销售编码
                List<OmSalesCodeReSpcDto> currentDtos = new ArrayList<>();
                for (OmSalesCodeReSpcDto omSalesCodeReSpcDto : codeReSpcDtos){
                    if(StringUtils.isNotEmpty(omSalesCodeReSpcDto.getSalesCode()) && dto.getBarAnnexCode().contains(omSalesCodeReSpcDto.getSalesCode())) {
                        currentDtos.add(omSalesCodeReSpcDto);
                        continue;
                    }
                }
                if (!currentDtos.isEmpty() && currentDtos.size() > 0){
                    // 排序，优先级最高的
                    currentDtos = currentDtos.stream().sorted(Comparator.comparing(OmSalesCodeReSpc::getPriority)).collect(Collectors.toList());
                    OmSalesCodeReSpc omSalesCodeReSpc = currentDtos.get(0);
                    // 绑定关系
                    mesSfcBarcodeProcess.setSamePackageCode(omSalesCodeReSpc.getSamePackageCode());
                    mesSfcBarcodeProcess.setSalesCodeReSpcId(omSalesCodeReSpc.getSalesCodeReSpcId());
                    mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);

                    // 增加以匹配数
                    omSalesCodeReSpc.setMatchedQty(omSalesCodeReSpc.getMatchedQty() != null ? BigDecimal.ONE.add(omSalesCodeReSpc.getMatchedQty()) : BigDecimal.ONE);
                    if (omSalesCodeReSpc.getMatchedQty().compareTo(omSalesCodeReSpc.getSamePackageCodeQty()) == 0){
                        omSalesCodeReSpc.setSamePackageCodeStatus((byte) 3);
                    }
                    omFeignApi.update(omSalesCodeReSpc);
                }
            }

            long getAnnex = System.currentTimeMillis();
            log.info("=========== 扫描附件码耗时 :" + (getAnnex-checkAnnerExists));

            // 有附件码未作业或有附件码并且已作业，直接返回
            if (isWork||keyPartRelevanceDtos.size() + 1 < usageQty.intValue()) {
                return false;
            }
        }else {
            if (StringUtils.isNotEmpty(customerBarcode)){
                // 不扫附件码，只扫一次，是三星客户条码，需补充附件码关键物料条码表数据
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
                        .materialCode(mesSfcBarcodeProcess.getMaterialCode())
                        .materialName(mesSfcBarcodeProcess.getMaterialName())
                        .partBarcode(customerBarcode)
                        .operatorUserId(user.getUserId())
                        .operatorTime(new Date())
                        .orgId(user.getOrganizationId())
                        .createTime(new Date())
                        .createUserId(user.getUserId())
                        .isDelete((byte) 1)
                        .build());
            }
        }
        long one1 = System.currentTimeMillis();
        log.info("============== 附件码处理耗时:"+ (one1 - selectBaseTime));
        // 7、过站
        // 更新下一工序，增加工序记录
        List<BaseRouteProcess> routeProcessList = baseFeignApi.findConfigureRout(mesPmWorkOrder.getRouteId()).getData();

        // 判断当前工序是否为产出工序,若是产出工序则不用判断下一工序
        if (!mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
            // 若入参当前扫码工序ID跟过站表下一工序ID不一致
            // 则判断过站表下一工序是否必过工序
            if (!dto.getProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())) {
                MesSfcBarcodeProcess mesSfcBarcodeProcessOptional = mesSfcBarcodeProcess;
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> mesSfcBarcodeProcessOptional.getNextProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012009, mesSfcBarcodeProcess.getNextProcessName());
                }
                BaseRouteProcess routeProcess = routeProcessOptional.get();
                if (routeProcess.getIsMustPass() == 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012010, mesSfcBarcodeProcess.getNextProcessName());
                }
            }
        }

        if (mesPmWorkOrder.getWorkOrderStatus() > 3) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012032);
        }

        long one = System.currentTimeMillis();
        log.info("============== 判断当前工序one:"+ (one - one1));

        // 更新当前工序
        mesSfcBarcodeProcess.setProcessId(baseProcess.getProcessId());
        mesSfcBarcodeProcess.setProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(baseProcess.getProcessName());
        // 更新工位、工段、产线
        mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
        mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
        mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        long two = System.currentTimeMillis();
        log.info("============== 更新工位:"+ (two - one));
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if (baseWorkshopSection == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);
        }
        mesSfcBarcodeProcess.setSectionId(baseProcess.getSectionId());//工段id
        mesSfcBarcodeProcess.setSectionCode(baseWorkshopSection.getSectionCode());//工段code
        mesSfcBarcodeProcess.setSectionName(baseWorkshopSection.getSectionName());//工段名称
        long two1 = System.currentTimeMillis();
        log.info("============== 取出工段编码和工段名称:"+ (two1 - two));
        BaseProLine baseProLine = baseFeignApi.getProLineDetail(dto.getProLineId()).getData();
        if (baseProLine == null) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "该产线不存在或已被删除");
        }
        mesSfcBarcodeProcess.setProLineId(baseProLine.getProLineId());
        mesSfcBarcodeProcess.setProCode(baseProLine.getProCode());
        mesSfcBarcodeProcess.setProName(baseProLine.getProName());

        long two2 = System.currentTimeMillis();
        log.info("============== 产线:"+ (two2 - two1));

        if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getProcessId())) {
            mesSfcBarcodeProcess.setDevoteTime(new Date());
        }
        if (mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId()) || mesPmWorkOrder.getWorkOrderCode().startsWith("ZL")) {
            // 产出工序置空下一道工序关信息
            mesSfcBarcodeProcess.setProductionTime(new Date());
            mesSfcBarcodeProcess.setNextProcessId(0L);
            mesSfcBarcodeProcess.setNextProcessName("");
            mesSfcBarcodeProcess.setNextProcessCode("");
            mesSfcBarcodeProcess.setOutProcessTime(new Date());
        }else {
            Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                    .filter(i -> dto.getProcessId().equals(i.getProcessId()))
                    .findFirst();
            if (!routeProcessOptional.isPresent()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012011, mesSfcBarcodeProcess.getNextProcessId());
            }

            BaseRouteProcess routeProcess = routeProcessOptional.get();
            baseProcess = baseFeignApi.processDetail(routeProcess.getNextProcessId()).getData();
            if (StringUtils.isEmpty(baseProcess)) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
            }

            // 设置下一工序
            mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
            mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
            mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
        }

        long three = System.currentTimeMillis();
        log.info("============== 设置next工序:"+ (three - two2));
        //客户条码
        if (StringUtils.isNotEmpty(customerBarcode)){
            mesSfcBarcodeProcess.setCustomerBarcode(customerBarcode);
        }
        mesSfcBarcodeProcess.setBarcodeStatus((byte) 1);
        mesSfcBarcodeProcess.setInProcessTime(new Date());
        mesSfcBarcodeProcess.setOperatorUserId(user.getUserId());
        mesSfcBarcodeProcess.setModifiedUserId(user.getUserId());
        mesSfcBarcodeProcess.setModifiedTime(new Date());
        int update = mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);
        if (update < 1) {
            throw new RuntimeException("更新过站表下一工序失败！");
        }

        // 增加过站记录
        /*MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(mesSfcBarcodeProcess, mesSfcBarcodeProcessRecord);
        mesSfcBarcodeProcessRecord.setOperatorUserId(user.getUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(user.getUserId());
        mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);*/

        long four = System.currentTimeMillis();
        log.info("============== 增加过站记录:"+ (four - three));

        /**
         * 日期：20211109
         * 条码状态过站变更
         */
        MesSfcWorkOrderBarcode sfcWorkOrderBarcode = mesSfcWorkOrderBarcodeService.selectByKey(mesSfcBarcodeProcess.getWorkOrderBarcodeId());

        // 是否投产工序且是该条码在工单工序第一次过站，工单投产数量 +1 mesSfcBarcodeProcessRecordDtoList.isEmpty()
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getPutIntoProcessId())) {
            if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getProcessId())) {
                /**
                 * 20211215 bgkun
                 * 如果有附件码，变更销售订单条码状态
                 */
                map.clear();
                map.put("workOrderBarcodeId", sfcWorkOrderBarcode.getWorkOrderBarcodeId());
                List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
                if (!keyPartRelevanceDtos.isEmpty() && keyPartRelevanceDtos.size() >0){
                    List<MesSfcWorkOrderBarcode> barcodes = new ArrayList<>();
                    for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos){
                        if (keyPartRelevanceDto.getPartBarcode() != null && mesSfcBarcodeProcess.getCustomerBarcode() == null){
                            MesSfcWorkOrderBarcode barcodeDto = mesSfcWorkOrderBarcodeService.findBarcode(keyPartRelevanceDto.getPartBarcode());
                            if (StringUtils.isNotEmpty(barcodeDto)){
                                MesSfcWorkOrderBarcode barcode = new MesSfcWorkOrderBarcode();
                                barcode.setWorkOrderBarcodeId(barcodeDto.getWorkOrderBarcodeId());
                                if (mesPmWorkOrder.getWorkOrderCode().startsWith("ZL")){
                                    barcode.setBarcodeStatus((byte) 2);
                                } else {
                                    barcode.setBarcodeStatus((byte) 1);
                                }
                                barcodes.add(barcode);
                            }
                        }
                    }
                    if (barcodes.size() > 0){
                        mesSfcWorkOrderBarcodeService.batchUpdate(barcodes);
                    }
                }

                long updatePartKey = System.currentTimeMillis();
                log.info("============== 变更附件码耗时:"+ (updatePartKey - four));

                mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty() != null ? mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE) : BigDecimal.ONE);
                // 若是投产工序，则判断是否首条码，若是则更新工单状态为生产中
                if (mesPmWorkOrder.getWorkOrderStatus() == (byte) 1){
                    mesPmWorkOrder.setWorkOrderStatus((byte) 3);
                    mesPmWorkOrder.setActualStartTime(new Date());
                }
                if (mesPmWorkOrder.getWorkOrderCode().startsWith("ZL")){
                    // 判断当前工序是否为产出工序，且是该条码在工单工序第一次过站，工单产出 +1
                    mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()) : BigDecimal.ONE);
                    if (mesPmWorkOrder.getOutputQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
                        // A16车间产出数量等于工单数量，工单完工
                        mesPmWorkOrder.setWorkOrderStatus((byte) 6);
                        mesPmWorkOrder.setActualEndTime(new Date());
                        mesPmWorkOrder.setModifiedTime(new Date());
                        mesPmWorkOrder.setModifiedUserId(user.getUserId());
                    }
                }

                pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                long updateworkorder = System.currentTimeMillis();
                log.info("============== 反写工单耗时:"+ (updateworkorder - updatePartKey));
                /**
                 * 日期：20211109
                 * 更新条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                 */
                if (mesPmWorkOrder.getWorkOrderCode().startsWith("ZL")){
                    sfcWorkOrderBarcode.setBarcodeStatus((byte) 2);
                } else {
                    sfcWorkOrderBarcode.setBarcodeStatus((byte) 1);
                }
                mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
                long updateOrderBarcode = System.currentTimeMillis();
                log.info("============== 反写条码表耗时:"+ (updateOrderBarcode - updateworkorder));
            }
        }

        long five = System.currentTimeMillis();
        log.info("============== 投产工序:"+ (five - four));


        /**
         * A16车间增加特性业务
         */
        if (mesPmWorkOrder.getWorkOrderCode().startsWith("ZL")){
            NotOrderInStorage notOrderInStorage = new NotOrderInStorage();
            notOrderInStorage.setMaterialId(mesPmWorkOrder.getMaterialId());
            notOrderInStorage.setRelevanceOrderCode(mesPmWorkOrder.getWorkOrderCode());

            SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
            searchBasePackageSpecification.setMaterialId(mesPmWorkOrder.getMaterialId());
            searchBasePackageSpecification.setOrgId(user.getOrganizationId());
            List<BasePackageSpecificationDto> basePackgeSpecifications = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
            if (StringUtils.isNotEmpty(basePackgeSpecifications)) {
                List<BaseMaterialPackageDto> baseMaterialPackageDtos = basePackgeSpecifications.get(0).getBaseMaterialPackages();
                if (StringUtils.isNotEmpty(baseMaterialPackageDtos)) {
                    BaseMaterialPackageDto baseMaterialPackageDto = baseMaterialPackageDtos.get(0);
                    notOrderInStorage.setPackingUnitName(baseMaterialPackageDto.getPackingUnitName());
                }
            }
            notOrderInStorage.setPackingQty(BigDecimal.ONE);
            notOrderInStorage.setBarcode(mesSfcBarcodeProcess.getBarcode());
            if (StringUtils.isNotEmpty(dto.getBarAnnexCode())){
                notOrderInStorage.setSalesBarcode(dto.getBarAnnexCode());
            }
            innerFeignApi.notOrderInStorage(notOrderInStorage);
            long six = System.currentTimeMillis();
            log.info("============== A16无单入库:"+ (six - five));
        }


        log.info("=========== 包箱过站总耗时 :" + (System.currentTimeMillis() - start));

        //补扫入库下线，发送消息mq到前端，自动确认滚动
        if(ObjectUtil.isNull(dto.getIsReadHead()) || !dto.getIsReadHead()){
            log.info("============== 补扫入库下线，发送消息mq到前端，产线ID：{}, 工位ID:{}",dto.getProLineId(),dto.getStationId());
            String topic = dto.getProLineId() + "_" + dto.getStationId();
            String code = "{\"code\":\"0\"}";
            rabbitProducer.sendMakeUp(code,topic);
        }
        return true;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    @LcnTransaction
    public Boolean pdaCartonWorkOld(PdaCartonWorkDto dto) throws Exception {
        long start = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        // 2022-03-08 判断是否质检完成之后走产线入库
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setBarcode(dto.getBarCode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (!inventoryDetDtos.isEmpty()){
//            this.scanBarocodeByQmsFinish(dto.getBarCode(), inventoryDetDtos.get(0));
            return true;
        }

        long InventoryDet = System.currentTimeMillis();
        log.info("===========查询库存明细耗时===================:" + (InventoryDet - start));

        String customerBarcode = null;
        if (dto.getBarCode().length() != 23){
            // 万宝项目-判断是三星客户条码还是厂内码
            Example example = new Example(MesSfcBarcodeProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("customerBarcode", dto.getBarCode() + "%");
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
            if (mesSfcBarcodeProcesses.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "厂内码不存在或未扫描到厂内码");
            }
            customerBarcode = dto.getBarCode();
            MesSfcBarcodeProcess barcodeProcess = mesSfcBarcodeProcesses.get(0);
            dto.setBarCode(barcodeProcess.getBarcode());
        }

        long custBarcode = System.currentTimeMillis();
        log.info("===========判断是三星客户条码还是厂内码耗时===================:" + (custBarcode-InventoryDet));

        //条码生产订单条码表
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(dto.getBarCode())
                        .build());
        if (mesSfcWorkOrderBarcodeDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        if (mesSfcWorkOrderBarcodeDtos.size() > 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012001);
        }

        long WorkOrderBarcode = System.currentTimeMillis();
        log.info("===========查询生产订单条码表耗时===================:" + (WorkOrderBarcode-custBarcode));

        /*
         * 流转卡状态(0-待投产 1-投产中 2-已完成 3-待打印)
         */
        MesSfcWorkOrderBarcodeDto orderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        if (StringUtils.isNotEmpty(orderBarcodeDto.getBarcodeStatus()) && (orderBarcodeDto.getBarcodeStatus() == 2 || orderBarcodeDto.getBarcodeStatus() == 3)) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012004, orderBarcodeDto.getBarcodeStatus() == 2 ? "已完成" : "待打印");
        }
        //条码对应工单
        MesPmWorkOrder mesPmWorkOrder = pmFeignApi.workOrderDetail(orderBarcodeDto.getWorkOrderId()).getData();
        if (4 == mesPmWorkOrder.getWorkOrderStatus() || 5 == mesPmWorkOrder.getWorkOrderStatus()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012006);
        }
        if (mesPmWorkOrder.getProductionQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012007, mesPmWorkOrder.getWorkOrderCode());
        }
        if (!mesPmWorkOrder.getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.PDA40012007.getCode(), "作业配置的产线与工单上的产线不符，不可操作");
        }

        // 条码对应过站记录信息
        /*MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .barcode(dto.getBarCode())
                .nextProcessId(dto.getProcessId())
                .build());*/

        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderBarcodeId(orderBarcodeDto.getWorkOrderBarcodeId())
                .nextProcessId(dto.getProcessId())
                .build());
        if(StringUtils.isEmpty(mesSfcBarcodeProcess)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012002);
        }
        if (!mesSfcBarcodeProcess.getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码产线跟该工位产线不匹配");
        }
        // 已完成所有过站工序
        if (mesSfcBarcodeProcess.getNextProcessId().equals(0L)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码已完成所有工序过站");
        }
        //是否已不良
        if (StringUtils.isNotEmpty(mesSfcBarcodeProcess.getBarcodeStatus()) && mesSfcBarcodeProcess.getBarcodeStatus().equals((byte)0)){
            throw new BizErrorException("该产品条码已不良 不可继续");
        }

        long BarcodeProcess = System.currentTimeMillis();
        log.info("===========查询条码对应过站记录信息耗时===================:" + (BarcodeProcess-WorkOrderBarcode));

        //查询包装规格信息
        BasePackageSpecificationDto packageSpecificationDto = getPackageSpecification(mesSfcBarcodeProcess.getMaterialId(), dto.getProcessId());
        if(StringUtils.isEmpty(packageSpecificationDto)){
            throw new BizErrorException("未设置包箱规格 不可继续");
        }

        // 2、校验条码是否关联包箱
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", orderBarcodeDto.getWorkOrderBarcodeId());
        List<MesSfcProductCartonDetDto> productCartonDetDtos = mesSfcProductCartonDetService.findList(map);
        if (productCartonDetDtos != null && productCartonDetDtos.size() > 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012013);
        }

        long CartonDetDto = System.currentTimeMillis();
        log.info("===========查询包箱明细耗时===================:" + (CartonDetDto-BarcodeProcess));

        //产线
        BaseProLine proLine = baseFeignApi.selectProLinesDetail(mesPmWorkOrder.getProLineId()).getData();
        //工序
        BaseProcess  baseProcess = baseFeignApi.processDetail(dto.getProcessId()).getData();
        //工位
        BaseStation baseStation = baseFeignApi.findStationDetail(dto.getStationId()).getData();

        if(StringUtils.isEmpty(proLine,baseProcess,baseStation,mesSfcWorkOrderBarcodeDtos,mesPmWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);

        }
        // 3、获取工位工序的包箱
        MesSfcProductCarton sfcProductCarton = null;
        if(packageSpecificationDto.getPackageSpecificationQuantity().compareTo(new BigDecimal(1))==1) {
            map.clear();
            map.put("stationId", dto.getStationId());
            map.put("closeStatus", 0);
            List<MesSfcProductCartonDto> sfcProductCartonDtos = mesSfcProductCartonService.findList(map);

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
                MesPmWorkOrder mesPmWorkOrderById = pmFeignApi.workOrderDetail(sfcProductCarton.getWorkOrderId()).getData();
                if ("2".equals(dto.getPackType()) && !mesPmWorkOrder.getMaterialId().equals(mesPmWorkOrderById.getMaterialId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012019, mesPmWorkOrder.getMaterialId(), mesPmWorkOrderById.getMaterialId());
                }
            }

            long CartonDto = System.currentTimeMillis();
            log.info("===========查询包箱 判断同一工单 同一料号包箱耗时===================:" + (CartonDto - CartonDetDto));
        }

        // 5、是否要扫附件码
        if (dto.getAnnex()) {
            long CartonDto = System.currentTimeMillis();
            if(StringUtils.isEmpty(dto.getBarAnnexCode())){
                return false;
            }
            if (dto.getBarCode().equals(dto.getBarAnnexCode())){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "厂内码与附件码重复扫描");
            }
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
            List<MesPmWorkOrderProcessReWoDto> pmWorkOrderProcessReWoDtoList = pmFeignApi.findPmWorkOrderProcessReWoList(searchMesPmWorkOrderProcessReWo).getData();
            if (pmWorkOrderProcessReWoDtoList == null || pmWorkOrderProcessReWoDtoList.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012016);
            }
            MesPmWorkOrderProcessReWoDto pmWorkOrderProcessReWoDto = pmWorkOrderProcessReWoDtoList.get(0);
            List<MesPmWorkOrderMaterialRePDto> pmWorkOrderMaterialRePDtoList = pmWorkOrderProcessReWoDto.getList();
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

            // 查找该附件码是否存在系统中
            List<MesSfcWorkOrderBarcodeDto> sfcWorkOrderAnnexBarcodeDtos = mesSfcWorkOrderBarcodeService
                    .findList(SearchMesSfcWorkOrderBarcode.builder()
                            .barcode(dto.getBarAnnexCode())
                            .build());

            boolean isBarCode = false;
            if(sfcWorkOrderAnnexBarcodeDtos != null && sfcWorkOrderAnnexBarcodeDtos.size() > 0){
                if (sfcWorkOrderAnnexBarcodeDtos.get(0).getLabelCategoryId().equals(mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"))){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该附件码为厂内码类型，不可扫码");
                }
                for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto: sfcWorkOrderAnnexBarcodeDtos) {
                    if(workOrderBarcodeDto.getBarcode().equals(dto.getBarAnnexCode())){
                        isBarCode = true;
                        break;
                    }
                }
            }
            boolean isWork = false;
            if (!isBarCode) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该附件码不存在系统中");
            } else {
                // 条码
                for (MesSfcWorkOrderBarcodeDto barcodeDto : sfcWorkOrderAnnexBarcodeDtos) {
//                    if(barcodeDto.getMaterialId().toString().equals(mesPmWorkOrder.getMaterialId().toString())==false){
//                        throw new Exception("附件码对应物料ID和厂内码对应物料ID不一致");
//                    }
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
                        isWork = false;

                        /**
                         * 万宝特性要求
                         * 扫附件码时，通过销售条码查找销售编码与PO关系表
                         * 绑定关系
                         */
                        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                        searchSysSpecItem.setSpecCode("wanbaoPackingPO");
                        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
                        if (!specItems.isEmpty() && "1".equals(specItems.get(0).getParaValue())){
                            SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
                            searchOmSalesCodeReSpc.setSamePackageCodeStatus((byte) 1);
                            List<OmSalesCodeReSpcDto> codeReSpcDtos = omFeignApi.findAll(searchOmSalesCodeReSpc).getData();
                            // 同个销售编码
                            List<OmSalesCodeReSpcDto> currentDtos = new ArrayList<>();
                            for (OmSalesCodeReSpcDto omSalesCodeReSpcDto : codeReSpcDtos){
                                if(StringUtils.isNotEmpty(omSalesCodeReSpcDto.getSalesCode())) {
                                    if (dto.getBarAnnexCode().contains(omSalesCodeReSpcDto.getSalesCode()) && barcodeDto.getMaterialId().equals(omSalesCodeReSpcDto.getMaterialId())) {
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
                                mesSfcBarcodeProcess.setSalesCodeReSpcId(omSalesCodeReSpc.getSalesCodeReSpcId());
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

            long getAnnex = System.currentTimeMillis();
            log.info("===========扫描附件码耗时===================:" + (getAnnex-CartonDto));

            // 有附件码未作业或有附件码并且已作业，直接返回
            if (isWork||keyPartRelevanceDtos.size() + 1 < usageQty.intValue()) {
                return false;
            }
        }else {
            if (StringUtils.isNotEmpty(customerBarcode)){
                // 不扫附件码，只扫一次，是三星客户条码，需补充附件码关键物料条码表数据
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
                        .materialCode(mesSfcBarcodeProcess.getMaterialCode())
                        .materialName(mesSfcBarcodeProcess.getMaterialName())
                        .partBarcode(customerBarcode)
                        .operatorUserId(user.getUserId())
                        .operatorTime(new Date())
                        .orgId(user.getOrganizationId())
                        .createTime(new Date())
                        .createUserId(user.getUserId())
                        .isDelete((byte) 1)
                        .build());
            }
        }
        // 6、判断是否已有箱码，生成箱码
        if (sfcProductCarton == null) {
            // 未关闭包箱不存在，生成包箱号并初始化包箱数据
            long CartonStart = System.currentTimeMillis();
            String cartonCode = BarcodeUtils.generatorCartonCode(mesPmWorkOrder.getMaterialId(),
                    dto.getProcessId(),
                    mesSfcBarcodeProcess.getMaterialCode(),
                    mesPmWorkOrder.getWorkOrderId(),
                    "09");
            // 查询基础资料包装规格
            //BasePackageSpecificationDto packageSpecificationDto = getPackageSpecification(mesSfcBarcodeProcess.getMaterialId(), dto.getProcessId());
            // 添加包箱表数据
            sfcProductCarton = saveCarton(cartonCode, user.getUserId(), user.getOrganizationId(), dto.getStationId(), mesPmWorkOrder.getWorkOrderId(), packageSpecificationDto.getPackageSpecificationQuantity(), mesPmWorkOrder.getMaterialId());

            long CartonEnd = System.currentTimeMillis();
            log.info("===========产生箱码耗时===================:" + (CartonEnd-CartonStart));
        }
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
                .workOrderBarcodeId(orderBarcodeDto.getWorkOrderBarcodeId())
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
        // 更新下一工序，增加工序记录
        BarcodeUtils.updateProcess(updateProcessDto);

        // 8、判断是否包箱已满，关箱
        if(packageSpecificationDto.getPackageSpecificationQuantity().compareTo(new BigDecimal(1))==1) {
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
                            .packingQty(mesSfcBarcodeProcessList.size() >= sfcProductCarton.getNowPackageSpecQty().intValue() ?
                                    mesSfcBarcodeProcessList.size() + "" : mesSfcBarcodeProcessList.size() + "尾")
                            .build());
                }
                long close = System.currentTimeMillis();
                log.info("=========== 1包多总耗时:" + (close - start));

            /*// 完工入库
            List<Long> cartonIds = new ArrayList<>();
            cartonIds.add(sfcProductCarton.getProductCartonId());
            if (mesPmWorkOrder.getOutputProcessId().equals(dto.getProcessId())){
                this.beforeCartonAutoAsnOrder(cartonIds, user.getOrganizationId(), null);
            }*/
            }
        }
        else if(packageSpecificationDto.getPackageSpecificationQuantity().compareTo(new BigDecimal(1))==0){
            if (dto.getPrint()) {
                // 关箱后台才能打印条码
                BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                        .barcode(sfcProductCarton.getCartonCode())
                        .labelTypeCode("09")
                        .workOrderId(mesPmWorkOrder.getWorkOrderId())
                        .printName(dto.getPrintName() != null ? dto.getPrintName() : "测试")
                        .packingQty("1")
                        .build());
            }
            long close = System.currentTimeMillis();
            log.info("=========== 1包1总耗时===========:" + (close - start));
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
    public int updateCartonDescNum(Long productCartonId, BigDecimal cartonDescNum, String packType, Boolean print, String printName, Long processId) {
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
                        .packingQty(mesSfcBarcodeProcessList.size() >= mesSfcProductCarton.getNowPackageSpecQty().intValue() ?
                                mesSfcBarcodeProcessList.size()+"" : mesSfcBarcodeProcessList.size()+"尾")
                        .build());
            }

            /*// 获取该条码对应的工单信息
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcProductCarton.getWorkOrderId());
            List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (mesPmWorkOrderDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
            // 完工入库
            List<Long> cartonIds = new ArrayList<>();
            cartonIds.add(productCartonId);
            if (mesPmWorkOrderDto.getOutputProcessId().equals(processId)){
                this.beforeCartonAutoAsnOrder(cartonIds, user.getOrganizationId(), null);
            }*/
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
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder()
                    .cartonCode(productCartonDto.getCartonCode())
                    .build());

            // 关箱后才能打印条码
            BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                    .barcode(productCartonDto.getCartonCode())
                    .labelTypeCode("09")
                    .workOrderId(productCartonDto.getWorkOrderId())
                    .printName(dto.getPrintName() != null ? dto.getPrintName() : "测试")
                    .packingQty(mesSfcBarcodeProcessList.size() >= productCartonDto.getNowPackageSpecQty().intValue() ?
                            mesSfcBarcodeProcessList.size()+"" : mesSfcBarcodeProcessList.size()+"尾")
                    .build());
        }

        /*// 获取该条码对应的工单信息
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderId(mesSfcProductCarton.getWorkOrderId());
        List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if (mesPmWorkOrderDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
        }
        MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
        // 完工入库
        List<Long> cartonIds = new ArrayList<>();
        cartonIds.add(mesSfcProductCarton.getProductCartonId());
        if (mesPmWorkOrderDto.getOutputProcessId().equals(dto.getProcessId())){
            this.beforeCartonAutoAsnOrder(cartonIds, user.getOrganizationId(), null);
        }*/


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
        List<BasePackageSpecificationDto> packageSpecificationDtos = baseFeignApi.findByMaterialProcessNotDet(searchBasePackageSpecification).getData();
        if (packageSpecificationDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "打包工序尚未维护当前物料的包装规格数据");
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
        /*MesSfcProductCarton sfcProductCarton = MesSfcProductCarton.builder()
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

         */
        MesSfcProductCarton sfcProductCarton=new MesSfcProductCarton();
        sfcProductCarton.setCartonCode(cartonCode);
        sfcProductCarton.setNowPackageSpecQty(cartonNum);
        sfcProductCarton.setWorkOrderId(workOrderId);
        sfcProductCarton.setMaterialId(materialId);
        sfcProductCarton.setStationId(stationId);
        if(cartonNum.compareTo(new BigDecimal(1))==0){
            sfcProductCarton.setCloseStatus((byte)1);
        }
        else {
            sfcProductCarton.setCloseStatus((byte)0);
        }
        sfcProductCarton.setCloseCartonUserId(userId);
        sfcProductCarton.setCloseCartonTime(new Date());
        sfcProductCarton.setOrgId(orgId);
        sfcProductCarton.setCreateUserId(userId);
        sfcProductCarton.setCreateTime(new Date());
        sfcProductCarton.setIsDelete((byte)1);

        int saveRes = mesSfcProductCartonMapper.insertUseGeneratedKeys(sfcProductCarton);
        if (saveRes > 0) {
            return sfcProductCarton;
        } else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
    }

    /**
     * @param barcode
     * @date 2022-03-31
     * 品质完成条码走产线入库，在包箱作业处理业务
     * 1、将不合格的条码库存状态变为合格
     * 2、反写成品检验单检验结果
     * 3、反写检验单明细条码样本值
     * 4、拆分移位单明细
     */
    private void scanBarocodeByQmsFinish(String barcode, WmsInnerInventoryDet inventoryDet){
        if (inventoryDet.getInspectionOrderCode() == null){
            return;
        }

        // 将不合格的条码库存状态变为合格
        List<BaseInventoryStatus> inventoryStatusList = baseFeignApi.findList(new SearchBaseInventoryStatus()).getData();
        boolean flag = true;
        for (BaseInventoryStatus inventoryStatus : inventoryStatusList){
            if (inventoryStatus.getInventoryStatusName().equals("待检") && inventoryDet.getInventoryStatusId().equals(inventoryStatus.getInventoryStatusId())){
                return;
            }
            if (inventoryStatus.getInventoryStatusName().equals("合格")){
                inventoryDet.setInventoryStatusId(inventoryStatus.getInventoryStatusId());
                flag = false;
            }
        }
        if (flag){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "合格的库存状态不存在或已被删除，请检查");
        }
        innerFeignApi.update(inventoryDet);

        // 反写成品检验单检验结果、检验单明细条码样本值、拆分移位单明细
        wanbaoFeignApi.recheckByBarcode(barcode);
    }

    // endregion

}
