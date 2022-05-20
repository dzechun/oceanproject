package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingListDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.dto.wms.in.BarPODto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
//import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.wanbao.WanbaoFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
//import com.fantechs.common.base.utils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MesSfcPalletWorkServiceImpl implements MesSfcPalletWorkService {

    // region Service 注入

    @Resource
    MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
    @Resource
    MesSfcProductPalletService mesSfcProductPalletService;
    @Resource
    MesSfcProductPalletDetService mesSfcProductPalletDetService;
    @Resource
    MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    MesSfcProductCartonDetService mesSfcProductCartonDetService;
    @Resource
    MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;
    @Resource
    PMFeignApi pmFeignApi;
    @Resource
    BaseFeignApi baseFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    InFeignApi inFeignApi;
    @Resource
    SecurityFeignApi securityFeignApi;
    @Resource
    OMFeignApi omFeignApi;
    @Resource
    WanbaoFeignApi wanbaoFeignApi;
    @Resource
    InnerFeignApi innerFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;

    // endregion

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {
        long curretTime = System.currentTimeMillis();
        Long startTime = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        // 清洗条码
        Map<String, Object> cleanBarcodeMap = this.cleanBarcode(requestPalletWorkScanDto.getBarcode());
        String barcode = cleanBarcodeMap.get("barcode").toString();
        Long workOrderBarcodeId = Long.parseLong(cleanBarcodeMap.get("workOrderBarcodeId").toString());
        Long workOrderId = Long.parseLong(cleanBarcodeMap.get("workOrderId").toString());
        log.info("=============== 清洗条码耗时：" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // 2022-03-08 判断是否质检完成之后走产线入库
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setBarcode(barcode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (!inventoryDetDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此条码已入库，不可重复扫码，请检查是否品质重新入库");
        }
        log.info("=============== 判断是否质检完成之后走产线入库耗时：" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // 条码过站流程表
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", workOrderBarcodeId);
        List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
        if (processServiceList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在，清重新扫码");
        }
        if (!processServiceList.get(0).getProLineId().equals(requestPalletWorkScanDto.getProLineId())) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "配置产线与条码产线不一致");
        }
        MesSfcBarcodeProcessDto barcodeProcessDto = processServiceList.get(0);
        if (barcodeProcessDto.getNextProcessId().equals(0)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码已完成过站，不可重复扫码");
        }
        if (!barcodeProcessDto.getNextProcessId().equals(requestPalletWorkScanDto.getProcessId())) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "PDA配置工序与条码工序不匹配");
        }

        // 工单信息
        MesPmWorkOrder pmWorkOrder = pmFeignApi.workOrderDetail(workOrderId).getData();
        if (StringUtils.isEmpty(pmWorkOrder)) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码所属离散任务不存在或已被删除，不可作业");
        }
        if (!pmWorkOrder.getProLineId().equals(requestPalletWorkScanDto.getProLineId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码产线跟该工位产线不匹配");
        }
        if (pmWorkOrder.getWorkOrderStatus() > 3) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012032);
        }

        // 条码表
        MesSfcWorkOrderBarcode workOrderBarcode = mesSfcWorkOrderBarcodeService.selectByKey(workOrderBarcodeId);
        log.info("=============== 条码过站流程表/工单信息/条码表耗时：" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // 更新过站/过站记录信息
        long start = System.currentTimeMillis();

        // 工艺路线与工艺
        ResponseEntity<List<BaseRouteProcess>> responseEntity = baseFeignApi.findConfigureRout(barcodeProcessDto.getRouteId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // 判断当前工序是否为产出工序,若是产出工序则不用判断下一工序
        if (!barcodeProcessDto.getNextProcessId().equals(pmWorkOrder.getOutputProcessId())) {
            // 若入参当前扫码工序ID跟过站表下一工序ID不一致
            // 则判断过站表下一工序是否必过工序
            if (!requestPalletWorkScanDto.getProcessId().equals(barcodeProcessDto.getNextProcessId())) {
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> barcodeProcessDto.getNextProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012009, barcodeProcessDto.getNextProcessName());
                }
                BaseRouteProcess routeProcess = routeProcessOptional.get();
                if (routeProcess.getIsMustPass() == 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012010, barcodeProcessDto.getNextProcessName());
                }
            }
        }
        ResponseEntity<BaseProcess> processResponseEntity = baseFeignApi.processDetail(requestPalletWorkScanDto.getProcessId());
        if (processResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012012, requestPalletWorkScanDto.getProcessId());
        }
        long one = System.currentTimeMillis();
        log.info("============== 判断当前工序:" + (one - start));

        BaseProcess baseProcess = processResponseEntity.getData();
        // 更新当前工序
        barcodeProcessDto.setProcessId(requestPalletWorkScanDto.getProcessId());
        barcodeProcessDto.setProcessCode(baseProcess.getProcessCode());
        barcodeProcessDto.setProcessName(baseProcess.getProcessName());
        // 更新工位、工段、产线
        if (StringUtils.isNotEmpty(requestPalletWorkScanDto.getStationId())) {
            BaseStation baseStation = baseFeignApi.findStationDetail(requestPalletWorkScanDto.getStationId()).getData();
            if (baseStation == null) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "没有找到对应的工位");
            }
            barcodeProcessDto.setStationId(baseStation.getStationId());
            barcodeProcessDto.setStationCode(baseStation.getStationCode());
            barcodeProcessDto.setStationName(baseStation.getStationName());
        }
        long two = System.currentTimeMillis();
        log.info("============== 更新工位:" + (two - one));

        // 更新工位
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if (baseWorkshopSection == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);
        }
        barcodeProcessDto.setSectionId(baseProcess.getSectionId());//工段id
        barcodeProcessDto.setSectionCode(baseWorkshopSection.getSectionCode());//工段code
        barcodeProcessDto.setSectionName(baseWorkshopSection.getSectionName());//工段名称
        long two1 = System.currentTimeMillis();
        log.info("============== 更新工位:" + (two1 - two));

        //过站次数累加注释
        //条码在当前工序有几条过站记录 记录工序过站次数开始 2021-10-18
        Map<String, Object> mapExist = new HashMap<>();
        mapExist.put("barcode", barcode);
        mapExist.put("stationId", requestPalletWorkScanDto.getStationId());
        mapExist.put("processId", requestPalletWorkScanDto.getProcessId());
        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = mesSfcBarcodeProcessRecordService.findList(mapExist);
        barcodeProcessDto.setPassStationCount(mesSfcBarcodeProcessRecordDtoList.size() + 1);

        if (barcodeProcessDto.getNextProcessId().equals(pmWorkOrder.getOutputProcessId())) {
            // 产出工序置空下一道工序关信息
            barcodeProcessDto.setProductionTime(new Date());
            barcodeProcessDto.setNextProcessId(0L);
            barcodeProcessDto.setNextProcessName("");
            barcodeProcessDto.setNextProcessCode("");

            // 判断当前工序是否为产出工序，且是该条码在工单工序第一次过站，工单产出 +1
            pmWorkOrder.setOutputQty(pmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(pmWorkOrder.getOutputQty()) : BigDecimal.ONE);
            if (pmWorkOrder.getOutputQty().compareTo(pmWorkOrder.getWorkOrderQty()) == 0) {
                // 产出数量等于工单数量，工单完工
                pmWorkOrder.setWorkOrderStatus((byte) 6);
                pmWorkOrder.setActualEndTime(new Date());
                pmWorkOrder.setModifiedTime(new Date());
                pmWorkOrder.setModifiedUserId(user.getUserId());
            }
            pmFeignApi.updatePmWorkOrder(pmWorkOrder);
            long six = System.currentTimeMillis();
            log.info("============== 变更工单:" + (six - two1));
            /**
             * 日期：20211109
             * 更新条码状态  条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
             */
            workOrderBarcode.setBarcodeStatus((byte) 2);
            mesSfcWorkOrderBarcodeService.update(workOrderBarcode);
            long six1 = System.currentTimeMillis();
            log.info("============== 更新条码状态:" + (six1 - six));

            /**
             * 20211215 bgkun
             * 如果有附件码，变更销售订单条码状态
             */
            this.updateAttachmentCodeStatus(workOrderBarcodeId);

            long six2 = System.currentTimeMillis();
            log.info("============== 变更附件码条码状态:" + (six2 - six1));
        } else {
            Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                    .filter(i -> requestPalletWorkScanDto.getProcessId().equals(i.getProcessId()))
                    .findFirst();
            if (!routeProcessOptional.isPresent()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012011, barcodeProcessDto.getNextProcessId());
            }

            BaseRouteProcess routeProcess = routeProcessOptional.get();
            processResponseEntity = baseFeignApi.processDetail(routeProcess.getNextProcessId());
            if (processResponseEntity.getCode() != 0) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
            }
            baseProcess = processResponseEntity.getData();

            // 设置下一工序
            barcodeProcessDto.setNextProcessId(routeProcess.getNextProcessId());
            barcodeProcessDto.setNextProcessCode(baseProcess.getProcessCode());
            barcodeProcessDto.setNextProcessName(baseProcess.getProcessName());
        }

        long three = System.currentTimeMillis();
        log.info("============== 设置next工序:" + (three - two));

        // 生成栈板码
        startTime = System.currentTimeMillis();
        String palletCode = this.createPalletCode(barcodeProcessDto.getMaterialId(), barcodeProcessDto.getMaterialCode(), requestPalletWorkScanDto.getProcessId(), barcodeProcessDto.getProCode(), workOrderId);
        log.info("=============== 通过规则生成栈板耗时：" + (System.currentTimeMillis() - startTime));

        //客户条码
        barcodeProcessDto.setInProcessTime(new Date());
        barcodeProcessDto.setOutProcessTime(new Date());
        barcodeProcessDto.setOperatorUserId(user.getUserId());
        barcodeProcessDto.setModifiedUserId(user.getUserId());
        barcodeProcessDto.setModifiedTime(new Date());
        barcodeProcessDto.setPalletCode(palletCode);
        int update = mesSfcBarcodeProcessService.update(barcodeProcessDto);
        if (update < 1) {
            throw new RuntimeException("更新过站表下一工序失败！");
        }

        long four = System.currentTimeMillis();
        log.info("============== 增加过站记录:" + (four - three));
        log.info("============ 更新过站信息耗时：" + (System.currentTimeMillis() - startTime));

        // 增加过站记录 2022-05-20
        MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(barcodeProcessDto, mesSfcBarcodeProcessRecord);
        mesSfcBarcodeProcessRecord.setOperatorUserId(user.getUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(user.getUserId());
        mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);

        // 万宝项目一包一，生成栈板
        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
        mesSfcProductPallet.setPalletCode(palletCode);
        mesSfcProductPallet.setNowPackageSpecQty(BigDecimal.ONE);
        mesSfcProductPallet.setWorkOrderId(workOrderId);
        mesSfcProductPallet.setMaterialId(barcodeProcessDto.getMaterialId());
        mesSfcProductPallet.setStationId(requestPalletWorkScanDto.getStationId());
        mesSfcProductPallet.setCloseStatus((byte) 1);
        mesSfcProductPallet.setClosePalletUserId(user.getUserId());
        mesSfcProductPallet.setClosePalletTime(new Date());
        mesSfcProductPallet.setModifiedUserId(user.getUserId());
        mesSfcProductPallet.setModifiedTime(new Date());
        mesSfcProductPallet.setOrgId(user.getOrganizationId());
        mesSfcProductPallet.setCreateUserId(user.getUserId());
        mesSfcProductPallet.setCreateTime(new Date());
        mesSfcProductPallet.setMoveStatus((byte) 0);
        mesSfcProductPalletService.save(mesSfcProductPallet);
        log.info("================= 新增产品栈板信息耗时：" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // 生成栈板明细
        MesSfcProductPalletDet mesSfcProductPalletDet = new MesSfcProductPalletDet();
        mesSfcProductPalletDet.setProductPalletId(mesSfcProductPallet.getProductPalletId());
        mesSfcProductPalletDet.setWorkOrderBarcodeId(workOrderBarcodeId);
        mesSfcProductPalletDet.setOrgId(user.getOrganizationId());
        mesSfcProductPalletDet.setCreateUserId(user.getUserId());
        mesSfcProductPalletDet.setCreateTime(new Date());
        mesSfcProductPalletDetService.save(mesSfcProductPalletDet);
        log.info("================= 新增产品栈板明细表耗时：" + (System.currentTimeMillis() - startTime));

        // 构建返回值
        PalletWorkScanDto palletWorkScanDto = new PalletWorkScanDto();
        palletWorkScanDto.setPalletCode(palletCode);
        palletWorkScanDto.setProductPalletId(mesSfcProductPallet.getProductPalletId());
        palletWorkScanDto.setWorkOrderCode(pmWorkOrder.getWorkOrderCode());
        palletWorkScanDto.setMaterialCode(barcodeProcessDto.getMaterialCode());
        palletWorkScanDto.setMaterialDesc(barcodeProcessDto.getMaterialName());
        palletWorkScanDto.setWorkOrderQty(pmWorkOrder.getWorkOrderQty());
        palletWorkScanDto.setProductionQty(pmWorkOrder.getProductionQty());
        palletWorkScanDto.setClosePalletNum(BigDecimal.ONE);
        palletWorkScanDto.setNowPackageSpecQty(BigDecimal.ONE);
        palletWorkScanDto.setScanCartonNum(1);

        log.info("====== 栈板总耗时：" + (System.currentTimeMillis() - curretTime));
        return palletWorkScanDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int workByManualOperation(PalletWorkByManualOperationDto dto) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", dto.getStackingCode());
        List<WanbaoStackingDto> stackingList = mesSfcProductPalletService.findStackingList(map);
        if (stackingList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码不存在");
        }
        try {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (dto.getWanbaoBarcodeDtos() == null || dto.getWanbaoBarcodeDtos().isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "条码集合为空，请扫码");
            }
            List<String> barcodeList = dto.getWanbaoBarcodeDtos().stream().map(WanbaoBarcodeDto::getBarcode).collect(Collectors.toList());
            SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
            searchMesSfcWorkOrderBarcode.setBarcodeList(barcodeList);
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
            if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在或已被删除");
            }

            // 校验同一配置作业
            map.clear();
            map.put("barcodeList", barcodeList);
            byte palletType = dto.getPalletType();
            if (palletType == 2) {
                String count = mesSfcBarcodeProcessService.countBarcodeListForPOGroup(map);
                if (count != null && Integer.parseInt(count) != 1) {
                    palletType = (byte) 3;
                }
            }
            if (palletType == 3) {
                String count = mesSfcBarcodeProcessService.countBarcodeListForSalesOrder(map);
                if (count != null && Integer.parseInt(count) != 1) {
                    palletType = (byte) 1;
                }
            }
            if (palletType == 1) {
                // 同料号
                String count = mesSfcBarcodeProcessService.countBarcodeListForMaterial(map);
                if (count != null && Integer.parseInt(count) > 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此批条码不属于同个物料，不可提交");
                }
            }

            // 保存条码堆垛关系
            List<WanbaoStackingDet> stackingDets = new ArrayList<>();
            for (WanbaoBarcodeDto wanbaoBarcodeDto : dto.getWanbaoBarcodeDtos()) {
                WanbaoStackingDet stackingDet = new WanbaoStackingDet();
                BeanUtil.copyProperties(wanbaoBarcodeDto, stackingDet);
                stackingDet.setStackingId(stackingList.get(0).getStackingId());
                for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto : mesSfcWorkOrderBarcodeDtoList) {
                    if (workOrderBarcodeDto.getBarcode().equals(wanbaoBarcodeDto.getBarcode())) {
                        wanbaoBarcodeDto.setWorkOrderId(workOrderBarcodeDto.getWorkOrderId());
                        break;
                    }
                }
                stackingDets.add(stackingDet);
            }
            wanbaoFeignApi.batchAdd(stackingDets);

            // 完工入库以及上架作业
            this.beforePalletAutoAsnOrder(stackingList.get(0).getStackingId(), user.getOrganizationId(), dto.getWanbaoBarcodeDtos(), barcodeList, mesSfcWorkOrderBarcodeDtoList);

            // A产线，发送堆垛数据至MQ
            if (stackingList.get(0).getProCode().equals("A") && (dto.getIsReadHead() == null || !dto.getIsReadHead())) {
                WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
                wanbaoStackingMQDto.setCode(0);
                wanbaoStackingMQDto.setStackingCode(stackingList.get(0).getStackingCode());
                String stackingLine = stackingList.get(0).getStackingCode().substring(stackingList.get(0).getStackingCode().length() - 1);
                wanbaoStackingMQDto.setStackingLine(stackingLine);
                // 发送堆垛号至mq
                rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            }
        } catch (Exception e) {
            if (!stackingList.isEmpty() && stackingList.get(0).getProCode().equals("A")) {
                WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
                wanbaoStackingMQDto.setCode(500);
                // 发送堆垛号至mq
                rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            }
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
        return 1;
    }


    @Override
    public ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId) {
        String customerBarcode = null;
        if (barcode.length() != 23) {
            // 判断是否三星客户条码
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoCheckBarcode");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty()) {
                Example example = new Example(MesSfcBarcodeProcess.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike("customerBarcode", barcode + "%");
                List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
                if (!mesSfcBarcodeProcesses.isEmpty()) {
                    customerBarcode = barcode;
                    barcode = mesSfcBarcodeProcesses.get(0).getBarcode();
                }
            }
        }
        ScanByManualOperationDto dto = new ScanByManualOperationDto();

        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcode);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码不存在系统或已被删除");
        }
        BaseLabelCategory labelCategory = baseFeignApi.findLabelCategoryDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()).getData();
        if (labelCategory.getLabelCategoryCode().equals("01")) {
            // 2022-03-08 判断是否质检完成之后走产线入库
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setBarcode(barcode);
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (!inventoryDetDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此条码已入库，不可重复扫码，请检查是否品质重新入库");
            }
            // 产品条码
            Map<String, Object> map = new HashMap<>();
            map.put("barcodeCode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            map.clear();
            map.put("barcode", barcode);
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if (processServiceList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在，清重新扫码");
            }
            if (!processServiceList.get(0).getProLineId().equals(proLineId)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "配置产线与条码产线不一致");
            }
            if (processServiceList.get(0).getNextProcessId() != 0L) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前条码未完成所有过站，请重新扫码");
            }
            if (!mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                if (customerBarcode != null) {
                    dto.setCustomerBarcode(customerBarcode);
                } else {
                    for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList) {
                        BaseLabelCategory keyPartLabelCategory = baseFeignApi.findLabelCategoryDetail(keyPartRelevanceDto.getLabelCategoryId()).getData();
                        if (keyPartLabelCategory.getLabelCategoryCode().equals("02")) {
                            // 销售订单条码
                            dto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                        } else if (keyPartLabelCategory.getLabelCategoryCode().equals("03")) {
                            // 客户条码
                            dto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                        }
                    }
                }
                dto.setProName(mesSfcKeyPartRelevanceDtoList.get(0).getProName());
            } else {
                BaseProLine baseProLine = baseFeignApi.selectProLinesDetail(proLineId).getData();
                dto.setProName(baseProLine.getProName());
            }
            dto.setBarcode(barcode);
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> orderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (orderDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码所属离散任务不存在或已被删除，不可作业");
            }
            dto.setMaterialCode(orderDtos.get(0).getMaterialCode());
            dto.setMaterialDesc(orderDtos.get(0).getMaterialName());
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
            }
            if (labelCategory.getLabelCategoryCode().equals("02")) {
                // 销售订单条码
                dto.setSalesBarcode(barcode);
            } else if (labelCategory.getLabelCategoryCode().equals("03")) {
                // 客户条码
                dto.setCustomerBarcode(barcode);
            }
            // 2022-03-08 判断是否质检完成之后走产线入库
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setBarcode(mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (!inventoryDetDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此条码已入库，不可重复扫码，请检查是否品质重新入库");
            }
            map.clear();
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if (processServiceList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在，清重新扫码");
            }
            if (!processServiceList.get(0).getProLineId().equals(proLineId)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "配置产线与条码产线不一致");
            }
            if (processServiceList.get(0).getNextProcessId() != 0L) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前条码未完成所有过站，请重新扫码");
            }
            dto.setBarcode(mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            dto.setProName(mesSfcKeyPartRelevanceDtoList.get(0).getProName());
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> orderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (orderDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码所属离散任务不存在或已被删除，不可作业");
            }
            dto.setMaterialCode(orderDtos.get(0).getMaterialCode());
            dto.setMaterialDesc(orderDtos.get(0).getMaterialName());
            map.clear();
            map.put("barcodeCode", dto.getBarcode());
            mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList) {
                BaseLabelCategory keyPartLabelCategory = baseFeignApi.findLabelCategoryDetail(keyPartRelevanceDto.getLabelCategoryId()).getData();
                if (labelCategory.getLabelCategoryCode().equals("02") && keyPartLabelCategory.getLabelCategoryCode().equals("03")) {
                    // 客户条码
                    dto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                } else if (labelCategory.getLabelCategoryCode().equals("03") && keyPartLabelCategory.getLabelCategoryCode().equals("02")) {
                    // 销售订单条码
                    dto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                }
            }
        }
        return dto;
    }

    @Override
    public List<PalletWorkScanDto> palletWorkScan(Long stationId) {

        List<PalletWorkScanDto> palletWorkScanDtoList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("closeStatus", 0);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtoList = mesSfcProductPalletService.findList(map);
        for (MesSfcProductPalletDto mesSfcProductPalletDto : mesSfcProductPalletDtoList) {
            PalletWorkScanDto palletWorkScanDto = new PalletWorkScanDto();
            BeanUtils.copyProperties(mesSfcProductPalletDto, palletWorkScanDto);
            map.clear();
            map.put("workOrderId", mesSfcProductPalletDto.getWorkOrderId());
            map.put("closeStatus", 1);
            List<MesSfcProductPalletDto> mesSfcProductPalletDtos = mesSfcProductPalletService.findList(map);
            palletWorkScanDto.setClosePalletNum(BigDecimal.valueOf(mesSfcProductPalletDtos.size()));
            int palletCartons = findPalletCarton(mesSfcProductPalletDto.getProductPalletId()).size();
            palletWorkScanDto.setScanCartonNum(palletCartons);
            // 栈板下的箱码
            List<String> cartonCodeList = this.findPalletCarton(mesSfcProductPalletDto.getProductPalletId());
            palletWorkScanDto.setCartonCodeList(cartonCodeList);
            palletWorkScanDtoList.add(palletWorkScanDto);
        }

        return palletWorkScanDtoList;
    }

    @Override
    public List<String> findPalletCarton(Long productPalletId) {

        List<String> cartonCodeList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productPalletId", productPalletId);
        map.put("groupBy", "cartonCode");
        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtoList = mesSfcProductPalletDetService.findList(map);
        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : mesSfcProductPalletDetDtoList) {
            cartonCodeList.add(mesSfcProductPalletDetDto.getCartonCode());
        }

        return cartonCodeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int submitNoFullPallet(List<Long> palletIdList, byte printBarcode, String printName, Long processId) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(MesSfcProductPallet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("productPalletId", palletIdList);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductPallet> productPallets = mesSfcProductPalletService.selectByExample(example);
        List<Long> palletIds = new ArrayList<>();
        for (MesSfcProductPallet item : productPallets) {
            item.setCloseStatus((byte) 1);
            item.setModifiedUserId(user.getUserId());
            item.setModifiedTime(new Date());
            mesSfcProductPalletService.update(item);
            // 是否打印栈板码
            if (printBarcode == 1) {
                PrintCarCodeDto printCarCodeDto = new PrintCarCodeDto();
                printCarCodeDto.setWorkOrderId(item.getWorkOrderId());
                printCarCodeDto.setLabelTypeCode("10");
                printCarCodeDto.setBarcode(item.getPalletCode());
                printCarCodeDto.setPrintName(printName != null ? printName : "测试");
                BarcodeUtils.printBarCode(printCarCodeDto);
            }
            palletIds.add(item.getProductPalletId());
        }
        return productPallets.size();
    }

    @Override
    public Boolean updatePalletType(Long stationId) {
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcProductPallet.builder()
                .stationId(stationId)
                .closeStatus((byte) 0)
                .build()));
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateNowPackageSpecQty(Long productPalletId, Double nowPackageSpecQty, Boolean print, String printName, Long processId) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        int palletCartons = findPalletCarton(productPalletId).size();
        if (nowPackageSpecQty < palletCartons) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "修改栈板包装规格数量不能小于栈板已绑定包箱数量");
        }
        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
        mesSfcProductPallet.setProductPalletId(productPalletId);
        mesSfcProductPallet.setNowPackageSpecQty(BigDecimal.valueOf(nowPackageSpecQty));
        if (nowPackageSpecQty == palletCartons) {
            mesSfcProductPallet.setCloseStatus((byte) 1);
            mesSfcProductPallet.setClosePalletUserId(user.getUserId());
            mesSfcProductPallet.setClosePalletTime(new Date());

            // 2022-03-01 改为不自动提交栈板，有人工堆垛作业触发
            /*// 获取该条码对应的工单信息
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcProductPallet.getWorkOrderId());
            List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (mesPmWorkOrderDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
            if (mesPmWorkOrderDto.getOutputProcessId().equals(processId)){
                // 生成完工入库单
                List<Long> palletIds = new ArrayList<>();
                palletIds.add(productPalletId);
                this.beforePalletAutoAsnOrder(palletIds, user.getOrganizationId(), null);
            }*/
        }
        mesSfcProductPallet.setModifiedTime(new Date());
        mesSfcProductPallet.setModifiedUserId(user.getUserId());
        int update = mesSfcProductPalletService.update(mesSfcProductPallet);
        if (print && update > 0) {
            // 关箱后才能打印条码
            BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                    .barcode(mesSfcProductPallet.getPalletCode())
                    .labelTypeCode("09")
                    .workOrderId(mesSfcProductPallet.getWorkOrderId())
                    .printName(printName != null ? printName : "测试")
                    .build());
        }
        return update;
    }

    @Override
    public int updateMoveStatus(Long productPalletId) {
        MesSfcProductPallet sfcProductPallet = mesSfcProductPalletService.selectByKey(productPalletId);
        if (sfcProductPallet == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012030);
        }
        sfcProductPallet.setMoveStatus((byte) 1);
        return mesSfcProductPalletService.update(sfcProductPallet);
    }

    @Override
    public Boolean testStacking(String code) {
        try {
            if (code.equals("500")) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "报错啦！！！");
            }
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(0);
            wanbaoStackingMQDto.setStackingCode("391-1066920304213");
            wanbaoStackingMQDto.setStackingLine("1");
            // 发送堆垛号至mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            return true;
        } catch (Exception e) {
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(500);
            // 发送堆垛号至mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
    }

    /**
     * 堆码作业发送MQ
     *
     * @param stackCode
     */
    @Override
    public void sendMQByStacking(String stackCode) {
        try {
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(0);
            wanbaoStackingMQDto.setStackingCode(stackCode);
            String stackingLine = stackCode.substring(stackCode.length() - 1);
            wanbaoStackingMQDto.setStackingLine(stackingLine);
            // 发送堆垛号至mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
        } catch (Exception e) {
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(500);
            // 发送堆垛号至mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
    }

    /**
     * 万宝-堆垛作业（A线）
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public int workByAuto(WanbaoAutoStackingListDto dto) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (dto.getList() == null || dto.getList().isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "条码集合为空，请扫码");
        }
        List<String> barcodeList = dto.getList().stream().map(WanbaoAutoStackingDto::getBarcode).collect(Collectors.toList());
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcodeList(barcodeList);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在或已被删除");
        }

        List<WanbaoBarcodeDto> list = new ArrayList<>();
        for (WanbaoAutoStackingDto stackingDet : dto.getList()) {
            WanbaoBarcodeDto wanbaoBarcodeDto = new WanbaoBarcodeDto();
            BeanUtil.copyProperties(stackingDet, wanbaoBarcodeDto);
            for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto : mesSfcWorkOrderBarcodeDtoList) {
                if (workOrderBarcodeDto.getBarcode().equals(wanbaoBarcodeDto.getBarcode())) {
                    wanbaoBarcodeDto.setWorkOrderId(workOrderBarcodeDto.getWorkOrderId());
                    break;
                }
            }
            list.add(wanbaoBarcodeDto);
        }

        // 完工入库以及上架作业
        this.beforePalletAutoAsnOrder(dto.getStackingId(), user.getOrganizationId(), list, barcodeList, mesSfcWorkOrderBarcodeDtoList);

        return 1;
    }

    /**
     * 校验条码同PO/销售明细/物料
     *
     * @param barcodeList
     * @return
     */
    @Override
    public boolean checkBarCode(List<String> barcodeList) {
        Map<String, Object> map = new HashMap<>();
        map.put("barcodeList", barcodeList);
        String count = mesSfcBarcodeProcessService.countBarcodeListForPOGroup(map);
        if (StringUtils.isNotEmpty(count) && Integer.parseInt(count) > 1) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码与堆垛上其他条码不属于同个PO，请重新扫码");
        }
        log.info("======== count1:" + count);
        count = mesSfcBarcodeProcessService.countBarcodeListForSalesOrder(map);
        log.info("======== count2:" + count);
        if (StringUtils.isNotEmpty(count) && Integer.parseInt(count) > 1) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码与堆垛上其他条码不属于同销售编码，请重新扫码");
        }
        count = mesSfcBarcodeProcessService.countBarcodeListForMaterial(map);
        log.info("======== count3:" + count);
        if (StringUtils.isNotEmpty(count) && Integer.parseInt(count) > 1) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码与堆垛上其他条码不属于同物料，请重新扫码");
        }
        return true;
    }

    // region 私有方法

    /**
     * 生成完工入库单
     *
     * @param stackingId        堆垛
     * @param orgId             组织
     * @param wanbaoBarcodeDtos 条码
     */
    private void beforePalletAutoAsnOrder(Long stackingId, Long orgId, List<WanbaoBarcodeDto> wanbaoBarcodeDtos, List<String> barcodeList, List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList) {

        /**
         * 2022-03-12 bgkun 人工栈板作业提交完工入库以及后续操作
         */
        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("barcodeList", barcodeList);
        map.put("orgId", orgId);
        List<PalletAutoAsnDto> autoAsnDtos = mesSfcProductPalletDetService.findListGroupByWorkOrder(map);
        for (PalletAutoAsnDto palletAutoAsnDto : autoAsnDtos) {
            long count = mesSfcWorkOrderBarcodeDtoList.stream().filter(item -> item.getWorkOrderId().equals(palletAutoAsnDto.getSourceOrderId())).count();
            palletAutoAsnDto.setPackingQty(BigDecimal.valueOf(count));
            palletAutoAsnDto.setActualQty(BigDecimal.valueOf(count));
            palletAutoAsnDto.setStackingId(stackingId);
            List<BarPODto> barPODtos = new ArrayList<>();
            wanbaoBarcodeDtos.forEach(item -> {
                if (item.getWorkOrderId().equals(palletAutoAsnDto.getSourceOrderId())) {
                    BarPODto barPODto = new BarPODto();
                    barPODto.setBarCode(item.getBarcode());
                    barPODto.setPOCode(palletAutoAsnDto.getSamePackageCode());
                    barPODto.setCutsomerBarcode(item.getCustomerBarcode());
                    barPODto.setSalesBarcode(item.getSalesBarcode());
                    barPODtos.add(barPODto);
                }
            });
            palletAutoAsnDto.setBarCodeList(barPODtos);
            //完工入库
            SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
            searchBaseMaterialOwner.setAsc((byte) 1);
            ResponseEntity<List<BaseMaterialOwnerDto>> baseMaterialOwnerDtos = baseFeignApi.findList(searchBaseMaterialOwner);
            if (StringUtils.isNotEmpty(baseMaterialOwnerDtos.getData())) {
                palletAutoAsnDto.setMaterialOwnerId(baseMaterialOwnerDtos.getData().get(0).getMaterialOwnerId());
                palletAutoAsnDto.setShipperName(baseMaterialOwnerDtos.getData().get(0).getMaterialOwnerName());
            } else {
                throw new BizErrorException("未查询到货主信息");
            }
            palletAutoAsnDto.setProductionDate(new Date());

            //2021-07-30 增加包装单位 by Dylan
            SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
            searchBasePackageSpecification.setMaterialId(palletAutoAsnDto.getMaterialId());
            searchBasePackageSpecification.setOrgId(orgId);
            ResponseEntity<List<BasePackageSpecificationDto>> basePackgeSpecifications = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification);
            if (StringUtils.isNotEmpty(basePackgeSpecifications.getData())) {
                List<BaseMaterialPackageDto> baseMaterialPackageDtos = basePackgeSpecifications.getData().get(0).getBaseMaterialPackages();
                if (StringUtils.isNotEmpty(baseMaterialPackageDtos)) {
                    BaseMaterialPackageDto baseMaterialPackageDto = baseMaterialPackageDtos.get(0);
                    palletAutoAsnDto.setPackingUnitName(baseMaterialPackageDto.getPackingUnitName());
                }
            }
            inFeignApi.palletAutoAsnOrder(palletAutoAsnDto);
        }
    }

    /**
     * 修改附件码状态
     *
     * @param workOrderBarcodeId
     */
    private void updateAttachmentCodeStatus(Long workOrderBarcodeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", workOrderBarcodeId);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
        if (!keyPartRelevanceDtos.isEmpty() && keyPartRelevanceDtos.size() > 0) {
            List<MesSfcWorkOrderBarcode> barcodes = new ArrayList<>();
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos) {
                if (keyPartRelevanceDto.getPartBarcode() != null) {
                    SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
                    searchMesSfcWorkOrderBarcode.setBarcode(keyPartRelevanceDto.getPartBarcode());
                    List<MesSfcWorkOrderBarcodeDto> barcodeDtos = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                    if (!barcodeDtos.isEmpty()) {
                        MesSfcWorkOrderBarcode barcode = new MesSfcWorkOrderBarcode();
                        barcode.setWorkOrderBarcodeId(barcodeDtos.get(0).getWorkOrderBarcodeId());
                        barcode.setBarcodeStatus((byte) 2);
                        barcodes.add(barcode);
                    }
                }
            }
            if (barcodes.size() > 0) {
                mesSfcWorkOrderBarcodeService.batchUpdate(barcodes);
            }
        }
    }

    /**
     * 清洗条码并返回产内码、工单ID、条码ID
     *
     * @param barcode
     * @return
     */
    private Map<String, Object> cleanBarcode(String barcode) {
        Map<String, Object> map = new HashMap<>();
        long start = System.currentTimeMillis();
        if (barcode.length() != 23) {
            // 判断是否三星客户条码
            Example example = new Example(MesSfcBarcodeProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("customerBarcode", barcode + "%");
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
            if (!mesSfcBarcodeProcesses.isEmpty()) {
                map.put("barcode", barcode);
                map.put("workOrderBarcodeId", mesSfcBarcodeProcesses.get(0).getWorkOrderBarcodeId().toString());
                map.put("workOrderId", mesSfcBarcodeProcesses.get(0).getWorkOrderId().toString());
                log.info("=========== 查询三星UK码耗时：" + (System.currentTimeMillis() - start));
                return map;
            }
        }

        if (barcode.contains("391-") || barcode.contains("391D")){
            // 销售条码
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findListByPallet(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
            }
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            map.put("workOrderBarcodeId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId().toString());
            map.put("workOrderId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId().toString());
            log.info("=========== 查询销售条码耗时：" + (System.currentTimeMillis() - start));
            return map;
        }

        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcode);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码不存在系统或已被删除");
        }
        log.info("=========== 查询条码耗时：" + (System.currentTimeMillis() - start));
        long time = System.currentTimeMillis();
        BaseLabelCategory labelCategory = baseFeignApi.findLabelCategoryDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()).getData();
        log.info("=========== 查询标签类型耗时：" + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        if (labelCategory.getLabelCategoryCode().equals("01")) {
            // 产内码
            map.put("barcode", mesSfcWorkOrderBarcodeDtoList.get(0).getBarcode());
            map.put("workOrderBarcodeId", mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId().toString());
            map.put("workOrderId", mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId().toString());
        } else {
            // 附件码
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findListByPallet(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
            }
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            map.put("workOrderBarcodeId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId().toString());
            map.put("workOrderId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId().toString());
            log.info("=========== 查询附件码耗时：" + (System.currentTimeMillis() - time));
        }
        return map;
    }

    /**
     * 生成栈板码
     * @param materialId
     * @param materialCode
     * @param processId
     * @param proCode
     * @param workOrderId
     * @return
     */
    private String createPalletCode(Long materialId, String materialCode, Long processId, String proCode, Long workOrderId){
        SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
        searchBasePackageSpecification.setMaterialId(materialId);
        searchBasePackageSpecification.setProcessId(processId);
        List<BasePackageSpecificationDto> basePackageSpecificationDtoList = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
        if (basePackageSpecificationDtoList.isEmpty() || basePackageSpecificationDtoList.get(0).getBaseMaterialPackages().isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该产品条码没有设置在该工序的包装规格");
        }
        // 获取该条码规则对应的最大条码数
        String lastBarCode = null;
        String barcodeRule = "";
        // 获取条码规则配置集合
        List<BaseBarcodeRuleSpec> barcodeRuleSpecList = new LinkedList<>();
        for (BaseMaterialPackage baseMaterialPackage : basePackageSpecificationDtoList.get(0).getBaseMaterialPackages()) {
            if (processId.equals(baseMaterialPackage.getProcessId())) {
                BaseBarcodeRule baseBarcodeRule = baseFeignApi.baseBarcodeRuleDetail(baseMaterialPackage.getBarcodeRuleId()).getData();
                barcodeRule = baseBarcodeRule.getBarcodeRule();
                boolean hasKey = redisUtil.hasKey(baseBarcodeRule.getBarcodeRule());
                if (hasKey) {
                    // 从redis获取上次生成条码
                    lastBarCode = redisUtil.get(baseBarcodeRule.getBarcodeRule()).toString();
                }

                SearchBaseBarcodeRuleSpec baseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
                baseBarcodeRuleSpec.setBarcodeRuleId(baseMaterialPackage.getBarcodeRuleId());
                barcodeRuleSpecList = baseFeignApi.findSpec(baseBarcodeRuleSpec).getData();
                if (barcodeRuleSpecList.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012023);
                }
                break;
            }
        }
        // 判断条码规则配置格式是否有[p],[L],[C]
        Map<String, Object> baseBarcodeRuleSpecMap = new HashMap<>();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : barcodeRuleSpecList) {
            if ("[P]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[P]"))) {
                baseBarcodeRuleSpecMap.put("[P]", materialCode);
            }
            if ("[L]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[L]"))) {
                baseBarcodeRuleSpecMap.put("[L]", proCode);
            }
            if ("[C]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[C]"))) {
                SearchBaseMaterialSupplier searchBaseMaterialSupplier = new SearchBaseMaterialSupplier();
                searchBaseMaterialSupplier.setMaterialId(materialId);
                List<BaseMaterialSupplierDto> baseMaterialSupplierDtoList = baseFeignApi.findBaseMaterialSupplierList(searchBaseMaterialSupplier).getData();
                if (baseMaterialSupplierDtoList.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到对应的客户料号");
                }
                baseBarcodeRuleSpecMap.put("[C]", baseMaterialSupplierDtoList.get(0).getMaterialSupplierCode());
            }
        }
        //获取最大流水号
        String maxCode = baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
        String palletCode = baseFeignApi.newGenerateCode(barcodeRuleSpecList, maxCode, baseBarcodeRuleSpecMap, workOrderId.toString()).getData();
        redisUtil.set(barcodeRule, palletCode);
        if (StringUtils.isEmpty(palletCode)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012008.getCode(), "生成栈板码出错");
        }
        return palletCode;
    }

    // endregion
}
