package com.fantechs.provider.wanbao.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.mes.sfc.BatchSyncBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.SyncBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.SyncFindBarcodeDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrder;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wanbao.api.config.DynamicDataSourceHolder;
import com.fantechs.provider.wanbao.api.entity.*;
import com.fantechs.provider.wanbao.api.mapper.*;
import com.fantechs.provider.wanbao.api.service.SyncDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncDataServiceImpl implements SyncDataService {

    // region service??????

    @Resource
    private MiddleMaterialMapper middleMaterialMapper;

    @Resource
    private MiddleOrderMapper middleOrderMapper;

    @Resource
    private MiddleSaleOrderMapper middleSaleOrderMapper;

    @Resource
    private MiddleOutDeliveryOrderMapper middleOutDeliveryOrderMapper;

    @Resource
    private MiddleProductMapper middleProductMapper;

    @Resource
    private SyncDataMapper syncDataMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private PMFeignApi pmFeignApi;

    @Resource
    private OMFeignApi omFeignApi;

    @Resource
    private OutFeignApi outFeignApi;

    @Resource
    private SecurityFeignApi securityFeignApi;

    @Resource
    private SFCFeignApi sfcFeignApi;

    // endregion

    @Override
    public void syncMaterialData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        List<SysApiLog> logList = new ArrayList<>();
        if (specItems.isEmpty()) {
            // ????????????
            logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-????????????", "????????????????????????????????????????????????????????????", null));
            securityFeignApi.batchAdd(logList);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATE_PATTERN));
        }else if ("1".equals(jsonObject.get("all"))) {
            map.put("date", jsonObject.get("syncDate"));
        }
        // ????????????
        List<MiddleMaterial> middleMaterials = middleMaterialMapper.findMaterialData(map);
        log.info("????????????????????????????????????????????????" + middleMaterials.size());

        if (!middleMaterials.isEmpty()) {
            long start = System.currentTimeMillis();
            List<MiddleMaterial> list = new ArrayList<>();
            for (MiddleMaterial item : middleMaterials){
                if (StringUtils.isEmpty(item.getVoltage())){
                    // ????????????
                    logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-????????????", item.getMaterialCode() + "??????????????????????????????????????????", null));
                    continue;
                }
                String substring = item.getVoltage().substring(item.getVoltage().length() - 1);
                if (substring.equals("???") || substring.equals("~")){
                    item.setVoltage(item.getVoltage().substring(0, item.getVoltage().length() - 1));
                }
                list.add(item);
            }

            // 1??????????????????
            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncMaterial().getData();
            // ????????????
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();
            // ??????????????????
            List<BaseTabDto> baseTabDtos = wanbaoBaseBySyncDto.getBaseTabDtoList();
            // ??????????????????
            List<BaseBarcodeRuleSetDto> ruleSetDtos = wanbaoBaseBySyncDto.getBarcodeRuleSetDtoList();
            if (ruleSetDtos.isEmpty()) {
                // ????????????
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-????????????", "???????????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }

            Long ruleSetId = 0L;
            for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos) {
                if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))) {
                    ruleSetId = ruleSetDto.getBarcodeRuleSetId();
                    break;
                }
            }
            List<Long> addMaterialIds = new ArrayList<>();
            for (MiddleMaterial dto : list) {
                long MaterialCount = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).count();
                if (MaterialCount <= 0) {
                    // ??????????????????
                    if (dto.getProductModelId() == null) {
                        BaseProductModel baseProductModel = new BaseProductModel();
                        baseProductModel.setProductModelName(dto.getProductModelCode());
                        baseProductModel.setProductModelCode(dto.getProductModelCode());
                        baseProductModel.setProductModelDesc(dto.getMaterialCode());
                        baseProductModel.setOrganizationId(sysUser.getOrganizationId());
                        baseProductModel.setIsDelete((byte) 1);
                        baseProductModel.setCreateTime(new Date());
                        baseProductModel.setCreateUserId(sysUser.getUserId());
                        baseProductModel.setModifiedTime(new Date());
                        baseProductModel.setModifiedUserId(sysUser.getUserId());
                        baseProductModel.setStatus(1);
                        Long id = baseFeignApi.addForReturnId(baseProductModel).getData();
                        dto.setProductModelId(id.toString());
                    }

                    //????????????
                    BaseMaterial material = new BaseMaterial();
                    BeanUtil.copyProperties(dto, material);
                    material.setOrganizationId(sysUser.getOrganizationId());
                    material.setStatus((byte) 1);
                    material.setBarcodeRuleSetId(ruleSetId);
                    BaseMaterial baseMaterial = baseFeignApi.saveByApi(material).getData();
                    dto.setMaterialId(baseMaterial.getMaterialId().toString());
                    dto.setMiddleMaterialId(UUIDUtils.getUUID());
                    addMaterialIds.add(baseMaterial.getMaterialId());
                    //??????????????????
                    BaseTab tab = new BaseTab();
                    BeanUtil.copyProperties(dto, tab);
                    // FG?????? SA?????????
                    if (dto.getMaterialProperty().equals("FG")) {
                        tab.setMaterialProperty((byte) 1);
                    } else {
                        tab.setMaterialProperty((byte) 0);
                    }
                    tab.setOrgId(sysUser.getOrganizationId());
                    baseFeignApi.addTab(tab);

                    // 2??????????????????
//                    DynamicDataSourceHolder.putDataSouce("secondary");
//                    middleMaterialMapper.save(dto);
//                    DynamicDataSourceHolder.removeDataSource();
                } else {
                    // ????????????
                    BaseMaterial material = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).findFirst().get();
                    material.setMaterialName(dto.getMaterialName());
                    material.setMaterialDesc(dto.getMaterialDesc());
                    material.setBarcodeRuleSetId(ruleSetId);
                    dto.setMaterialId(material.getMaterialId().toString());
                    baseFeignApi.update(material);

                    //??????????????????
                    for (BaseTabDto item : baseTabDtos) {
                        if (item.getMaterialId().equals(material.getMaterialId())) {
                            BaseTab tab = new BaseTab();
                            BeanUtil.copyProperties(item, tab);
                            // FG?????? SA?????????
                            if (dto.getMaterialProperty().equals("FG")) {
                                tab.setMaterialProperty((byte) 1);
                            } else {
                                tab.setMaterialProperty((byte) 0);
                            }
//                            tab.setVoltage(dto.getVoltage());
                            if (StringUtils.isNotEmpty(dto.getProductModelId())) {
                                tab.setProductModelId(Long.valueOf(dto.getProductModelId()));
                            }
                            baseFeignApi.updateTab(tab);
                            break;
                        }
                    }
                }
            }

            // ????????????????????????????????????
            if (!addMaterialIds.isEmpty()){
                baseFeignApi.updateByMaterial(addMaterialIds);
            }

            // ????????????
            logList.add(build(sysUser.getOrganizationId(), (byte) 1, "???????????????-????????????", JSON.toJSONString(middleMaterials), new BigDecimal(System.currentTimeMillis() - start)));
        }
        if (!logList.isEmpty()) {
            securityFeignApi.batchAdd(logList);
        }
    }

    @Override
    public void syncOrderData(String workOrderCode) {
        long current0 = System.currentTimeMillis();
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // ????????????
        List<SysApiLog> logList = new ArrayList<>();
        if (specItems.isEmpty()) {
            // ????????????
            logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "????????????????????????????????????????????????????????????", null));
            securityFeignApi.batchAdd(logList);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if (workOrderCode != null) {
            map.put("workOrderCode", workOrderCode);
        } else {
            if ("0".equals(jsonObject.get("all"))) {
                map.put("date", DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATE_PATTERN));
            }else if ("1".equals(jsonObject.get("all"))) {
                map.put("date", jsonObject.get("syncDate"));
            }
        }
        List<MiddleOrder> workOrders = middleOrderMapper.findOrderData(map);
        long current1 = System.currentTimeMillis();
        long res1 = current1 - current0;
        log.info("????????????????????????????????????????????????" + workOrders.size() + "========== ??????oracle??????:" + res1);
        if (!workOrders.isEmpty()) {
            // ????????????
            long start = System.currentTimeMillis();

            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncOrder().getData();
            // ????????????
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();
            // ????????????
            List<BaseProLine> proLines = wanbaoBaseBySyncDto.getProLineList();
            // ????????????
            List<BaseRoute> baseRoutes = wanbaoBaseBySyncDto.getRouteList();
            if (baseRoutes.isEmpty()) {
                // ????????????
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "?????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }
            // ??????????????????
            List<BaseBarcodeRuleSetDto> ruleSetDtos = wanbaoBaseBySyncDto.getBarcodeRuleSetDtoList();
            if (ruleSetDtos.isEmpty()) {
                // ????????????
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "???????????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }
            // ????????????????????????
            List<BaseRouteProcess> routeProcessList = wanbaoBaseBySyncDto.getRouteProcessList();
            if (routeProcessList.isEmpty()) {
                // ????????????
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "???????????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }
            long current22 = System.currentTimeMillis();
            long res22 = current22 - start;
            log.info("========== ????????????????????????:" + res22);

            // ????????????????????????
            DynamicDataSourceHolder.putDataSouce("five");
            map.clear();
            map.put("orgId", sysUser.getOrganizationId());
            List<OmSalesOrder> salesOrders = syncDataMapper.findAllSalesOrder(map);
            long current33 = System.currentTimeMillis();
            long res33 = current33 - current22;
            log.info("========== ????????????????????????:" + res33);
            // ????????????
            List<MesPmWorkOrder> workOrderDtos = syncDataMapper.findAllWorkOrder(map);
            long current44 = System.currentTimeMillis();
            long res44 = current44 - current33;
            log.info("========== ??????????????????:" + res44);
            Long ruleSetId = 0L;
            for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos) {
                if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))) {
                    ruleSetId = ruleSetDto.getBarcodeRuleSetId();
                    break;
                }
            }
            long current2 = System.currentTimeMillis();
            long res2 = current2 - current1;
            log.info("========== ??????????????????:" + res2);
            List<MesPmWorkOrder> addList = new ArrayList<>();
            List<MesPmWorkOrder> updateList = new ArrayList<>();
            for (MiddleOrder order : workOrders) {
                if (order.getWorkOrderCode() != null && !order.getWorkOrderCode().contains("ZA")
                        && !order.getWorkOrderCode().contains("ZC") && !order.getWorkOrderCode().contains("ZD")
                        && !order.getWorkOrderCode().contains("ZL")) {
                    continue;
                }

                MesPmWorkOrder workOrder = new MesPmWorkOrder();
                BeanUtil.copyProperties(order, workOrder);
                workOrder.setBarcodeRuleSetId(ruleSetId);
                workOrder.setIsDelete((byte) 1);
                workOrder.setOrgId(sysUser.getOrganizationId());
                workOrder.setScheduledQty(workOrder.getWorkOrderQty());
                workOrder.setProductionQty(BigDecimal.ZERO);
                workOrder.setWorkOrderStatus((byte) 1);
                workOrder.setCreateTime(new Date());
                workOrder.setCreateUserId(sysUser.getUserId());
                workOrder.setModifiedUserId(sysUser.getUserId());
                workOrder.setModifiedTime(new Date());

                // 2021-11-18
                // ???????????????????????????????????????????????????????????????????????????????????????????????????
                Long routeId = 0L;
                for (BaseRoute route : baseRoutes) {
                    if (route.getRouteCode().equals("A1") && order.getWorkOrderCode().startsWith("ZA")) {
                        routeId = route.getRouteId();
                        break;
                    } else if (route.getRouteCode().equals("A2") && (order.getWorkOrderCode().startsWith("ZC") || order.getWorkOrderCode().startsWith("ZD"))) {
                        routeId = route.getRouteId();
                        break;
                    } else if (route.getRouteCode().equals("A16") && order.getWorkOrderCode().startsWith("ZL")) {
                        routeId = route.getRouteId();
                        break;
                    }
                }

                List<BaseRouteProcess> baseRouteProcess = new ArrayList<>();
                for (BaseRouteProcess routeProcess : routeProcessList) {
                    if (routeProcess.getRouteId().equals(routeId)) {
                        baseRouteProcess.add(routeProcess);
                    }
                }
                if (baseRouteProcess.isEmpty()) {
                    // ????????????
                    logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "???????????????????????????????????????????????????????????????", null));
                    continue;
                }
                workOrder.setRouteId(routeId);
                if (baseRouteProcess.get(0) != null) {
                    workOrder.setPutIntoProcessId(baseRouteProcess.get(0).getProcessId());
                    workOrder.setOutputProcessId(baseRouteProcess.get(baseRouteProcess.size() - 1).getProcessId());
                }

                // ????????????
                for (OmSalesOrder item : salesOrders) {
                    if (item.getSalesOrderCode().equals(order.getSalesOrderCode())) {
                        workOrder.setSalesOrderId(item.getSalesOrderId());
                        break;
                    }
                }

                // ??????
                if (StringUtils.isEmpty(order.getMaterialCode())) {
                    logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", order.getWorkOrderCode() + "????????????????????????????????????????????????????????????????????????", null));
                    continue;
                }
                for (BaseMaterial item : baseMaterials) {
                    if (item.getMaterialCode().equals(order.getMaterialCode())) {
                        workOrder.setMaterialId(item.getMaterialId());
                        break;
                    }
                }

                if (StringUtils.isEmpty(workOrder.getMaterialId())) {
                    // ????????????
                    logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", order.getWorkOrderCode() + "????????????????????????????????????????????????" + order.getMaterialCode() + "??????????????????????????????", null));
                    continue;
                }

                // ??????
                for (BaseProLine item : proLines) {
                    if (order.getWorkOrderCode().startsWith("ZL")) {
                        if (!item.getProCode().equals("A16")) {
                            continue;
                        }
                        workOrder.setProLineId(item.getProLineId());
                    } else if (order.getOption1().contains("ZA") && "A".equals(item.getProCode())) {
                        workOrder.setProLineId(item.getProLineId());
                        break;
                    } else if (order.getOption1().contains("ZC") && "C".equals(item.getProCode())) {
                        workOrder.setProLineId(item.getProLineId());
                        break;
                    } else if (order.getOption1().contains("ZD") && "D".equals(item.getProCode())) {
                        workOrder.setProLineId(item.getProLineId());
                        break;
                    }
                }

                // ????????????????????????
                for (MesPmWorkOrder item : workOrderDtos) {
                    if (item.getWorkOrderCode().equals(order.getWorkOrderCode())) {
                        workOrder.setWorkOrderId(item.getWorkOrderId());
                        order.setWorkOrderId(item.getWorkOrderId().toString());
                        break;
                    }
                }
                if (StringUtils.isNotEmpty(workOrder.getWorkOrderId())) {
                    // ????????????
                    updateList.add(workOrder);
                } else {
                    Map<Long, List<MesPmWorkOrder>> listMap = workOrderDtos.stream().filter(item -> item.getMaterialId().equals(workOrder.getMaterialId()) && item.getLogicId() != null).collect(Collectors.groupingBy(MesPmWorkOrder::getLogicId));
                    // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (listMap.size() == 1){
                        listMap.forEach((key, value) -> {
                            workOrder.setLogicId(key);
                        });
                    }
                    // ????????????
                    addList.add(workOrder);
                    // ???????????????
//                    DynamicDataSourceHolder.putDataSouce("secondary");
//                    middleOrderMapper.save(order);
//                    DynamicDataSourceHolder.removeDataSource();
                }
            }
            long current3 = System.currentTimeMillis();
            long res3 = current3 - current2;
            log.info("========== ??????????????????????????????:" + res3);
            // ???????????????
            if (!addList.isEmpty()) {
                log.info("========== ???????????????pmFeignApi.addList - start");
                pmFeignApi.addList(addList);
                log.info("========== ???????????????pmFeignApi.addList - end");
            }

            if (!updateList.isEmpty()) {
                log.info("========== ???????????????pmFeignApi.batchUpdate - start");
                pmFeignApi.batchUpdate(updateList);
                log.info("========== ???????????????pmFeignApi.batchUpdate - end");
            }
            logList.add(build(sysUser.getOrganizationId(), (byte) 1, "???????????????-??????????????????", JSON.toJSONString(workOrders), new BigDecimal(System.currentTimeMillis() - start)));
            if (!logList.isEmpty()) {
                log.info("========== ???????????????securityFeignApi.batchAdd - start");
                securityFeignApi.batchAdd(logList);
                log.info("========== securityFeignApi.batchAdd - end");
            }

            long current4 = System.currentTimeMillis();
            long res5 = current4 - current0;
            log.info("========== ???????????????:" + res5);
        }
    }

    @Override
    public void syncSaleOrderData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // ????????????
        List<SysApiLog> logList = new ArrayList<>();
        if (specItems.isEmpty()) {
            // ????????????
            logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "????????????????????????????????????????????????????????????", null));
            securityFeignApi.batchAdd(logList);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATE_PATTERN));
        }else if ("1".equals(jsonObject.get("all"))) {
            map.put("date", jsonObject.get("syncDate"));
        }

        // ?????????????????????????????????????????????
        middleSaleOrderMapper.setPolicy();
        List<MiddleSaleOrder> salesOrders = middleSaleOrderMapper.findSaleOrderData(map);
        log.info("=========== salesOrders: " + JSON.toJSONString(salesOrders));
        if (!salesOrders.isEmpty()) {
            // ????????????
            long start = System.currentTimeMillis();
            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncSaleOrder().getData();
            // ????????????
            List<BaseSupplier> baseSuppliers = wanbaoBaseBySyncDto.getBaseSupplierList();
            // ????????????
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();
            // ??????????????????
            List<BaseBarcodeRuleSetDto> ruleSetDtos = wanbaoBaseBySyncDto.getBarcodeRuleSetDtoList();
            if (ruleSetDtos.isEmpty()) {
                // ????????????
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "?????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }
            // ????????????
            DynamicDataSourceHolder.putDataSouce("five");
            map.clear();
            map.put("orgId", sysUser.getOrganizationId());
            List<OmSalesOrder> salesOrderDtos = syncDataMapper.findAllSalesOrder(map);

            List<MiddleSaleOrder> list = new ArrayList<>();
            Map<String, List<MiddleSaleOrder>> collect = salesOrders.stream().collect(Collectors.groupingBy(MiddleSaleOrder::getSalesOrderCode));
            collect.forEach((key, value) -> {
                Map<BigDecimal, List<MiddleSaleOrder>> listMap = value.stream().sorted(Comparator.comparing(MiddleSaleOrder::getModifiedTime))
                        .collect(Collectors.groupingBy(MiddleSaleOrder::getCustomerOrderLineNumber));
                List<MiddleSaleOrder> orders = new ArrayList<>();
                listMap.forEach((itemkey, itemvalue) -> {
                    orders.add(itemvalue.get(0));
                });

                OmSalesOrderDto omSalesOrder = new OmSalesOrderDto();
                BeanUtil.copyProperties(orders.get(0), omSalesOrder);
                for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos) {
                    if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))) {
                        omSalesOrder.setBarcodeRuleSetId(ruleSetDto.getBarcodeRuleSetId());
                        break;
                    }
                }

                List<OmSalesOrderDetDto> omSalesOrderDetDtoList = new ArrayList<>();
                for (MiddleSaleOrder saleOrder : orders) {
                    // ????????????
                    if (StringUtils.isEmpty(saleOrder.getSupplierCode())) {
                        // ????????????
                        logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "???????????????????????????????????????????????????????????????", null));
                        break;
                    }
                    for (BaseSupplier supplier : baseSuppliers) {
                        if (saleOrder.getSupplierCode().equals(supplier.getSupplierCode())) {
                            saleOrder.setSupplierId(supplier.getSupplierId().toString());
                            omSalesOrder.setSupplierId(supplier.getSupplierId());
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(saleOrder.getSupplierId())) {
                        // ??????????????????????????????????????????
                        BaseSupplier baseSupplier = new BaseSupplier();
                        baseSupplier.setSupplierCode(saleOrder.getSupplierCode());
                        baseSupplier.setSupplierName(saleOrder.getSupplierName());
                        baseSupplier.setSupplierType((byte) 2);
                        baseSupplier.setStatus((byte) 1);
                        Long supplierId = baseFeignApi.saveForReturnID(baseSupplier).getData();
                        saleOrder.setSupplierId(supplierId.toString());
                        omSalesOrder.setSupplierId(supplierId);
                        baseSupplier.setSupplierId(supplierId);
                        baseSuppliers.add(baseSupplier);
                    }

                    // ??????
                    if (StringUtils.isEmpty(saleOrder.getMaterialCode())) {
                        // ????????????
                        logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", saleOrder.getSalesOrderCode() + "???????????????????????????????????????????????????????????????", null));
                        break;
                    }
                    for (BaseMaterial material : baseMaterials) {
                        if (saleOrder.getMaterialCode().equals(material.getMaterialCode())) {
                            saleOrder.setMaterialId(material.getMaterialId().toString());
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(saleOrder.getMaterialId())) {
                        // ????????????
                        logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", saleOrder.getSalesOrderCode() + "?????????????????????????????????????????????????????????" + saleOrder.getMaterialCode() + "??????????????????????????????", null));
                        break;
                    }
                    OmSalesOrderDetDto detDto = new OmSalesOrderDetDto();
                    BeanUtil.copyProperties(saleOrder, detDto);
                    // ????????????????????????
                    for (OmSalesOrder dto : salesOrderDtos) {
                        if (dto.getSalesOrderCode().equals(saleOrder.getSalesOrderCode())) {
                            omSalesOrder.setSalesOrderId(dto.getSalesOrderId());
                            saleOrder.setSaleOrderId(dto.getSalesOrderId().toString());
                            map.put("salesOrderId", dto.getSalesOrderId());
                            List<OmSalesOrderDetDto> orderDetDtoList = syncDataMapper.findAllSalesorderDet(map);
                            for (OmSalesOrderDetDto oldDetDto : orderDetDtoList) {
                                if (oldDetDto.getSalesCode().equals(saleOrder.getSalesCode())) {
                                    detDto.setSalesOrderDetId(oldDetDto.getSalesOrderDetId());
                                }
                            }
                            break;
                        }
                    }

                    omSalesOrderDetDtoList.add(detDto);
                }
                // ???????????????
                if (!omSalesOrderDetDtoList.isEmpty()) {
                    omSalesOrder.setSalesUserName(omSalesOrder.getMakeOrderUserName());
                    omSalesOrder.setOmSalesOrderDetDtoList(omSalesOrderDetDtoList);
                    if (StringUtils.isNotEmpty(omSalesOrder.getSalesOrderId())) {
                        omFeignApi.update(omSalesOrder);
                    } else {
                        list.addAll(orders);
                        omSalesOrder.setOrderStatus((byte) 1);
                        omSalesOrder.setStatus((byte) 1);
                        omFeignApi.add(omSalesOrder);
                    }
                }
            });

//            if (!list.isEmpty()){
//                // ???????????????
//                DynamicDataSourceHolder.putDataSouce("secondary");
//                for (MiddleSaleOrder item : list){
//                    item.setSaleOrderId(UUIDUtils.getUUID());
//                    middleSaleOrderMapper.save(item);
//                }
//                DynamicDataSourceHolder.removeDataSource();
//            }

            logList.add(build(sysUser.getOrganizationId(), (byte) 1, "???????????????-??????????????????", JSON.toJSONString(salesOrders), new BigDecimal(System.currentTimeMillis() - start)));
        }
        if (!logList.isEmpty()) {
            securityFeignApi.batchAdd(logList);
        }
    }

    @Override
    public void syncOutDeliveryData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        List<SysApiLog> logList = new ArrayList<>();
        if (specItems.isEmpty()) {
            // ????????????
            logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-???????????????", "?????????????????????????????????????????????????????????", null));
            securityFeignApi.batchAdd(logList);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATE_PATTERN));
        }else if ("1".equals(jsonObject.get("all"))) {
            map.put("date", jsonObject.get("syncDate"));
        }

        // ?????????????????????????????????????????????
        middleOutDeliveryOrderMapper.setPolicy();
        List<MiddleOutDeliveryOrder> deliveryOrders = middleOutDeliveryOrderMapper.findOutDeliveryData(map);
        DynamicDataSourceHolder.putDataSouce("fourth");
        List<MiddleOutDeliveryOrder> orderFormIMS = middleOutDeliveryOrderMapper.findOutDeliveryDataFormIMS(map);
        DynamicDataSourceHolder.removeDataSource();
        log.info("???????????????????????????????????????????????????" + deliveryOrders.size() + "IMS????????????????????????" + orderFormIMS.size());
        if (!orderFormIMS.isEmpty() && orderFormIMS.size() > 0) {
            deliveryOrders.addAll(orderFormIMS);
        }
        if (!deliveryOrders.isEmpty()) {
            // ????????????
            long start = System.currentTimeMillis();

            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncOutDelivery().getData();
            // ??????
            List<BaseSupplier> baseSuppliers = wanbaoBaseBySyncDto.getBaseSupplierList();
            // ??????
            List<BaseMaterialOwnerDto> ownerDtos = wanbaoBaseBySyncDto.getMaterialOwnerDtoList();
            // ????????????
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();

            SearchWmsOutDeliveryOrder outDeliveryOrder = new SearchWmsOutDeliveryOrder();
            outDeliveryOrder.setPageSize(999999);
            List<WmsOutDeliveryOrderDto> deliveryOrderDtos = outFeignApi.findList(outDeliveryOrder).getData();

            // ???????????????
            List<MiddleOutDeliveryOrder> list = new ArrayList<>();
            Map<String, List<MiddleOutDeliveryOrder>> listMap = deliveryOrders.stream().collect(Collectors.groupingBy(MiddleOutDeliveryOrder::getDeliveryOrderCode));
            listMap.forEach((key, value) -> {
                WmsOutDeliveryOrder order = new WmsOutDeliveryOrder();
                BeanUtil.copyProperties(value.get(0), order);
                order.setOrderTypeId(1L); // ????????????
                order.setOrgId(sysUser.getOrganizationId());
                order.setMaterialOwnerId(ownerDtos.get(0).getMaterialOwnerId());
                order.setIfCreatedJobOrder((byte) 0);

                // ????????????
                for (BaseSupplier supplier : baseSuppliers) {
                    if (supplier.getSupplierCode().equals(value.get(0).getCustomerCode())) {
                        order.setSupplierId(supplier.getSupplierId());
                        break;
                    }
                }
                if (StringUtils.isEmpty(order.getSupplierId())) {
                    // ??????????????????????????????????????????
                    BaseSupplier baseSupplier = new BaseSupplier();
                    baseSupplier.setSupplierCode(value.get(0).getCustomerCode());
                    baseSupplier.setSupplierName(value.get(0).getCustomerName());
                    baseSupplier.setSupplierType((byte) 2);
                    baseSupplier.setStatus((byte) 1);
                    Long supplierId = baseFeignApi.saveForReturnID(baseSupplier).getData();
                    baseSupplier.setSupplierId(supplierId);
                    baseSuppliers.add(baseSupplier);
                    order.setSupplierId(supplierId);
                }
                List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = new ArrayList<>();
                for (MiddleOutDeliveryOrder item : value) {
                    WmsOutDeliveryOrderDetDto orderDet = new WmsOutDeliveryOrderDetDto();
                    BeanUtil.copyProperties(item, orderDet);
                    log.info("============= json??????" + JSON.toJSONString(orderDet));

                    // ??????
                    if (StringUtils.isEmpty(item.getMaterialCode())) {
                        logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-???????????????", item.getDeliveryOrderCode() + "?????????????????????????????????????????????????????????????????????", null));
                        continue;
                    }
                    for (BaseMaterial material : baseMaterials) {
                        if (material.getMaterialCode().equals(item.getMaterialCode())) {
                            orderDet.setMaterialId(material.getMaterialId());
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(orderDet.getMaterialId())) {
                        // ????????????
                        logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-???????????????", item.getDeliveryOrderCode() + "?????????????????????????????????????????????????????????" + item.getMaterialCode() + "????????????????????????", null));
                        continue;
                    }
                    wmsOutDeliveryOrderDetList.add(orderDet);
                }

                boolean flag = true;
                for (WmsOutDeliveryOrderDto dto : deliveryOrderDtos) {
                    if (dto.getDeliveryOrderCode().equals(order.getDeliveryOrderCode())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (!wmsOutDeliveryOrderDetList.isEmpty()) {
                        order.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetList);
                        outFeignApi.add(order);
                        list.addAll(value);
                    }
                } else {
                    outFeignApi.update(order);
                }
            });

//            if (!list.isEmpty()){
//                // ???????????????
//                DynamicDataSourceHolder.putDataSouce("secondary");
//                for (MiddleOutDeliveryOrder item : list){
//                    item.setDeliveryOrderId(UUIDUtils.getUUID());
//                    middleOutDeliveryOrderMapper.save(item);
//                }
//                DynamicDataSourceHolder.removeDataSource();
//            }

            logList.add(build(sysUser.getOrganizationId(), (byte) 1, "???????????????-???????????????", JSON.toJSONString(listMap), new BigDecimal(System.currentTimeMillis() - start)));
        }
        if (!logList.isEmpty()) {
            securityFeignApi.batchAdd(logList);
        }
    }

    @Override
    public void syncBarcodeData(boolean flag) {
        long start = System.currentTimeMillis();
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // ????????????
        List<SysApiLog> logList = new ArrayList<>();
        if (StringUtils.isEmpty(specItems)) {
            // ????????????
            logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "?????????????????????????????????????????????????????????", null));
            securityFeignApi.batchAdd(logList);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        // flag = true??????????????????=false???????????????
        if (flag) {
            JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
            if ("0".equals(jsonObject.get("all"))) {
                map.put("date", DateUtil.format(DateUtil.offset(new Date(), DateField.HOUR, -1), DatePattern.NORM_DATETIME_FORMAT));
            }else if ("1".equals(jsonObject.get("all"))) {
                map.put("date", jsonObject.get("syncDate"));
            }
            log.info("=========== jsonObject: " + JSON.toJSONString(jsonObject));
        }
        long time1 = System.currentTimeMillis();
        log.info("=========== ???????????????: " + (time1-start));
        // ????????????
        DynamicDataSourceHolder.putDataSouce("thirdary");
        List<MiddleProduct> barcodeDatas = middleProductMapper.findBarcodeData(map);
        log.info("============== PQMS?????????: " + barcodeDatas.size());
        DynamicDataSourceHolder.removeDataSource();
        long time2 = System.currentTimeMillis();
        log.info("=========== ????????????PQMS??????: " + (time2-time1));
        if (!barcodeDatas.isEmpty()) {
            log.info("============ ????????????PQMS?????? ============");
            WanbaoBaseBySyncDto syncDto = baseFeignApi.findBySyncBarcodeData().getData();
            // ??????
            BaseProLine proLine = new BaseProLine();
            List<BaseProLine> proLines = syncDto.getProLineList();
            for (BaseProLine entity : proLines) {
                if (entity.getProCode().equals("A")) {
                    proLine = entity;
                    break;
                }
            }
            if (StringUtils.isEmpty(proLine.getProLineId())) {
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "??????PQMS???????????????????????????A???????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }

            // ????????????
            List<BaseLabelCategoryDto> categoryDtoList = syncDto.getLabelCategoryDtoList();
            Long labelCategoryId = null;
            for (BaseLabelCategoryDto entity : categoryDtoList) {
                if (entity.getLabelCategoryCode().equals("01")) {
                    labelCategoryId = entity.getLabelCategoryId();
                    break;
                }
            }
            if (labelCategoryId == null){
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "??????PQMS???????????????????????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }

            // ????????????
            BaseProductProcessRoute productProcessRoute = new BaseProductProcessRoute();
            List<BaseProductProcessRoute> productProcessRouteList = syncDto.getProcessRouteList();
            for (BaseProductProcessRoute entity : productProcessRouteList) {
                if (proLine.getProLineId().equals(entity.getProLineId())) {
                    productProcessRoute = entity;
                    break;
                }
            }
            if (StringUtils.isEmpty(productProcessRoute.getProductProcessRouteId())) {
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "??????PQMS????????????????????????A??????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }
            List<BaseRouteProcess> routeProcessList = syncDto.getRouteProcessList();
            List<BaseRouteProcess> list = new ArrayList<>();
            for (BaseRouteProcess entity : routeProcessList) {
                if (productProcessRoute.getRouteId().equals(entity.getRouteId())) {
                    list.add(entity);
                }
            }
            if (list.isEmpty()) {
                logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "??????PQMS????????????????????????A???????????????????????????????????????????????????????????????", null));
                securityFeignApi.batchAdd(logList);
                return;
            }
            BaseRouteProcess routeProcess = list.stream().sorted(Comparator.comparing(BaseRouteProcess::getOrderNum)).findFirst().get();
            long time3 = System.currentTimeMillis();
            log.info("=========== ??????base??????: " + (time3-time2));
            // ??????
            DynamicDataSourceHolder.putDataSouce("five");
            map.clear();
            map.put("orgId", sysUser.getOrganizationId());
            List<MesPmWorkOrder> workOrders = syncDataMapper.findAllWorkOrder(map);
            long time4 = System.currentTimeMillis();
            log.info("=========== ??????om??????: " + (time4-time3));
            // ??????
            List<String> barcodeList = barcodeDatas.stream().map(MiddleProduct::getBarcode).collect(Collectors.toList());
            SyncFindBarcodeDto findBarcodeDto = sfcFeignApi.syncFindBarcode(labelCategoryId, barcodeList).getData();
            // ???????????????
            List<SyncBarcodeProcessDto> sfcBarcodeProcesses = findBarcodeDto.getBarcodeProcesses();

            long time5 = System.currentTimeMillis();
            log.info("=========== ??????sfc??????: " + (time5-time4));
            // ????????????
            List<MesSfcBarcodeProcess> updateBarcodeProcess = new ArrayList<>();
            for (MiddleProduct middleProduct : barcodeDatas) {
                if (StringUtils.isEmpty(middleProduct) || StringUtils.isEmpty(middleProduct.getCustomerBarcode()) || StringUtils.isEmpty(middleProduct.getBarcode())) {
                    continue;
                }

                // ????????????
                MesPmWorkOrder workOrder = new MesPmWorkOrder();
                for (MesPmWorkOrder entity : workOrders) {
                    if (entity.getWorkOrderCode().equals(middleProduct.getWorkOrderCode())) {
                        workOrder = entity;
                        break;
                    }
                }
                if (StringUtils.isEmpty(workOrder.getWorkOrderCode())) {
                    logList.add(build(sysUser.getOrganizationId(), (byte) 0, "???????????????-??????????????????", "??????PQMS???????????????PQMS??????" + middleProduct.getBarcode() + "????????????" + middleProduct.getWorkOrderCode() + "??????????????????????????????????????????", null));
                    continue;
                }

                // ????????????
                // ???????????????????????????????????????
                for (SyncBarcodeProcessDto entity : sfcBarcodeProcesses) {
                    if (entity.getBarcode().equals(middleProduct.getBarcode()) && !middleProduct.getCustomerBarcode().equals(entity.getCustomerBarcode())) {
                        entity.setCustomerBarcode(middleProduct.getCustomerBarcode());
                        MesSfcBarcodeProcess process = new MesSfcBarcodeProcess();
                        BeanUtil.copyProperties(entity, process);
                        updateBarcodeProcess.add(process);
                    }
                }
            }
            long time6 = System.currentTimeMillis();
            log.info("=========== ??????????????????: " + (time6-time5));
            // ????????????
            if (updateBarcodeProcess.size() > 0) {
                BatchSyncBarcodeDto batchSyncBarcodeDto = new BatchSyncBarcodeDto();
                batchSyncBarcodeDto.setUpdateList(updateBarcodeProcess);
                sfcFeignApi.batchSyncBarcode(batchSyncBarcodeDto);
            }
            long time7 = System.currentTimeMillis();
            log.info("=========== ????????????????????????: " + (time7-time6));

            // ???????????????
//           DynamicDataSourceHolder.putDataSouce("secondary");
//           for(MiddleProduct middleProduct : barcodeDatas){
//                middleProductMapper.save(middleProduct);
//           }
//           DynamicDataSourceHolder.removeDataSource();

            List<String> collect = barcodeDatas.stream().map(i -> i.getBarcode() + "||" + i.getCustomerBarcode()).collect(Collectors.toList());
            logList.add(build(sysUser.getOrganizationId(), (byte) 1, "???????????????-??????????????????", JSON.toJSONString(collect), new BigDecimal(System.currentTimeMillis() - start)));
        }
        if (!logList.isEmpty()) {
            securityFeignApi.batchAdd(logList);
        }
        long time8 = System.currentTimeMillis();
        log.info("=========== ?????????: " + (time8-start));
    }


    private SysApiLog build(Long orgId, Byte callResult, String apiUrl, String responseData, BigDecimal consumeTime){
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setRequestTime(new Date());
        apiLog.setThirdpartySysName("?????????????????????");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult(callResult);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl(apiUrl);
        apiLog.setOrgId(orgId);
        apiLog.setResponseData(responseData);
        if (StringUtils.isNotEmpty(consumeTime)){
            apiLog.setConsumeTime(consumeTime);
        }
        return apiLog;
    }
}
