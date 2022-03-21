package com.fantechs.provider.wanbao.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.fantechs.common.base.general.dto.mes.sfc.BatchSyncBarcodeSaveDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.SyncFindBarcodeDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
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

    // region service注入

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
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("万宝数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 0);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl("查询数据库-同步物料");
        apiLog.setOrgId(sysUser.getOrganizationId());

        if (specItems.isEmpty()) {
            // 记录日志
            apiLog.setResponseData("获取平台同步数据配置项不存在，不同步数据");
            logList.add(apiLog);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        }
        // 同步数据
        List<MiddleMaterial> middleMaterials = middleMaterialMapper.findMaterialData(map);
        log.info("获取万宝物料数据，当前获取数量：" + middleMaterials.size());

        if (!middleMaterials.isEmpty()) {
            long start = System.currentTimeMillis();
            Map<String, List<MiddleMaterial>> collect = middleMaterials.stream().collect(Collectors.groupingBy(MiddleMaterial::getMaterialCode));
            List<MiddleMaterial> list = new ArrayList<>();
            collect.forEach((key, value) -> {
                list.add(value.get(0));
            });

            // 1、保存平台库
            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncMaterial().getData();
            // 物料集合
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();
            // 物料页签集合
            List<BaseTabDto> baseTabDtos = wanbaoBaseBySyncDto.getBaseTabDtoList();
            // 条码规则集合
            List<BaseBarcodeRuleSetDto> ruleSetDtos = wanbaoBaseBySyncDto.getBarcodeRuleSetDtoList();
            if (ruleSetDtos.isEmpty()) {
                // 记录日志
                apiLog.setResponseData("平台默认条码规则集合不存在，不同步工单数据");
                logList.add(apiLog);
                return;
            }

            Long ruleSetId = 0L;
            for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos) {
                if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))) {
                    ruleSetId = ruleSetDto.getBarcodeRuleSetId();
                    break;
                }
            }

            for (MiddleMaterial dto : list) {
                long MaterialCount = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).count();
                if (MaterialCount <= 0) {
                    // 新增客户型号
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

                    //新增物料
                    BaseMaterial material = new BaseMaterial();
                    BeanUtil.copyProperties(dto, material);
                    material.setOrganizationId(sysUser.getOrganizationId());
                    material.setStatus((byte) 1);
                    material.setBarcodeRuleSetId(ruleSetId);
                    BaseMaterial baseMaterial = baseFeignApi.saveByApi(material).getData();
                    dto.setMaterialId(baseMaterial.getMaterialId().toString());
                    dto.setMiddleMaterialId(UUIDUtils.getUUID());

                    //新增物料页签
                    BaseTab tab = new BaseTab();
                    BeanUtil.copyProperties(dto, tab);
                    // FG成品 SA半成品
                    if (dto.getMaterialProperty().equals("FG")) {
                        tab.setMaterialProperty((byte) 1);
                    } else {
                        tab.setMaterialProperty((byte) 0);
                    }
                    tab.setOrgId(sysUser.getOrganizationId());
                    baseFeignApi.addTab(tab);

                    // 2、保存中间库
//                    DynamicDataSourceHolder.putDataSouce("secondary");
//                    middleMaterialMapper.save(dto);
//                    DynamicDataSourceHolder.removeDataSource();
                } else {
                    // 修改物料
                    BaseMaterial material = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).findFirst().get();
                    material.setMaterialName(dto.getMaterialName());
                    material.setMaterialDesc(dto.getMaterialDesc());
                    material.setBarcodeRuleSetId(ruleSetId);
                    dto.setMaterialId(material.getMaterialId().toString());
                    baseFeignApi.update(material);

                    //修改物料页签
                    for (BaseTabDto item : baseTabDtos) {
                        if (item.getMaterialId().equals(material.getMaterialId())) {
                            BaseTab tab = new BaseTab();
                            BeanUtil.copyProperties(item, tab);
                            // FG成品 SA半成品
                            if (dto.getMaterialProperty().equals("FG")) {
                                tab.setMaterialProperty((byte) 1);
                            } else {
                                tab.setMaterialProperty((byte) 0);
                            }
                            tab.setVoltage(dto.getVoltage());
                            if (StringUtils.isNotEmpty(dto.getProductModelId())) {
                                tab.setProductModelId(Long.valueOf(dto.getProductModelId()));
                            }
                            baseFeignApi.updateTab(tab);
                            break;
                        }
                    }
                }
            }

            // 记录日志
            apiLog.setResponseData("万宝数据库-同步物料");
            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            logList.add(apiLog);
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

        // 记录日志
        List<SysApiLog> logList = new ArrayList<>();
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("万宝数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 0);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl("查询数据库-同步生产订单");
        apiLog.setOrgId(sysUser.getOrganizationId());

        if (specItems.isEmpty()) {
            // 记录日志
            apiLog.setResponseData("获取平台同步数据配置项不存在，不同步数据");
            logList.add(apiLog);
            logList.add(apiLog);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if (workOrderCode != null) {
            map.put("workOrderCode", workOrderCode);
        } else {
            if ("0".equals(jsonObject.get("all"))) {
                map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
            }
        }
        List<MiddleOrder> workOrders = middleOrderMapper.findOrderData(map);
        long current1 = System.currentTimeMillis();
        long res1 = current1 - current0;
        log.info("获取万宝工单数据，当前获取数量：" + workOrders.size() + "========== 查询oracle耗时:" + res1);
        if (!workOrders.isEmpty()) {
            // 记录日志
            long start = System.currentTimeMillis();

            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncOrder().getData();
            // 物料集合
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();
            // 产线集合
            List<BaseProLine> proLines = wanbaoBaseBySyncDto.getProLineList();
            // 工艺路线
            List<BaseRoute> baseRoutes = wanbaoBaseBySyncDto.getRouteList();
            if (baseRoutes.isEmpty()) {
                // 记录日志
                apiLog.setResponseData("平台默认工艺路线不存在，不同步工单数据");
                logList.add(apiLog);
                return;
            }
            // 条码规则集合
            List<BaseBarcodeRuleSetDto> ruleSetDtos = wanbaoBaseBySyncDto.getBarcodeRuleSetDtoList();
            if (ruleSetDtos.isEmpty()) {
                // 记录日志
                apiLog.setResponseData("平台默认条码规则集合不存在，不同步工单数据");
                logList.add(apiLog);
                return;
            }
            // 工艺路线工序关系
            List<BaseRouteProcess> routeProcessList = wanbaoBaseBySyncDto.getRouteProcessList();
            if (routeProcessList.isEmpty()) {
                // 记录日志
                apiLog.setResponseData("工艺路线工序关系数据不存在，不同步工单数据");
                logList.add(apiLog);
                return;
            }
            long current22 = System.currentTimeMillis();
            long res22 = current22 - start;
            log.info("========== 查询基础数据耗时:" + res22);

            // 获取所有销售订单
            List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findSalesOrderAll().getData();
            long current33 = System.currentTimeMillis();
            long res33 = current33 - current22;
            log.info("========== 查询销售订单耗时:" + res33);
            // 工单集合
            List<MesPmWorkOrder> workOrderDtos = pmFeignApi.findWorkOrderAll().getData();
            long current44 = System.currentTimeMillis();
            long res44 = current44 - current33;
            log.info("========== 查询工单耗时:" + res44);
            Long ruleSetId = 0L;
            for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos) {
                if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))) {
                    ruleSetId = ruleSetDto.getBarcodeRuleSetId();
                    break;
                }
            }
            long current2 = System.currentTimeMillis();
            long res2 = current2 - current1;
            log.info("========== 查询数据耗时:" + res2);
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
                workOrder.setOption3(order.getSalesCode());

                // 2021-11-18
                // 欢欢确定万宝同步工单时，工艺路线按产线匹配，产线由工单编码前缀确定
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
                    // 记录日志
                    apiLog.setResponseData("工艺路线工序关系数据不存在，不同步工单数据");
                    logList.add(apiLog);
                    continue;
                }
                workOrder.setRouteId(routeId);
                if (baseRouteProcess.get(0) != null) {
                    workOrder.setPutIntoProcessId(baseRouteProcess.get(0).getProcessId());
                    workOrder.setOutputProcessId(baseRouteProcess.get(baseRouteProcess.size() - 1).getProcessId());
                }

                // 销售订单
                for (OmSalesOrderDto item : salesOrderDtos) {
                    if (item.getSalesOrderCode().equals(order.getSalesOrderCode())) {
                        workOrder.setSalesOrderId(item.getSalesOrderId());
                        // 记录销售编码
                        workOrder.setSalesCode(item.getSalesCode());
                        break;
                    }
                }

                // 物料
                if (StringUtils.isEmpty(order.getMaterialCode())) {
                    apiLog.setResponseData(order.getWorkOrderCode() + "此生产订单数据中物料编码为空，不同步此条订单数据");
                    logList.add(apiLog);
                    continue;
                }
                for (BaseMaterial item : baseMaterials) {
                    if (item.getMaterialCode().equals(order.getMaterialCode())) {
                        workOrder.setMaterialId(item.getMaterialId());
                        break;
                    }
                }

                if (StringUtils.isEmpty(workOrder.getMaterialId())) {
                    // 记录日志
                    apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号在系统中没有匹配的物料" + order.getMaterialCode() + "，不同步此条订单数据");
                    logList.add(apiLog);
                    continue;
                }

                // 产线
                for (BaseProLine item : proLines) {
                    if (order.getWorkOrderCode().startsWith("ZL")) {
                        if (!item.getProCode().equals("A16")) {
                            continue;
                        }
                        workOrder.setProLineId(item.getProLineId());
                    } else if (item.getProName().equals(order.getProName())) {
                        workOrder.setProLineId(item.getProLineId());
                        break;
                    }
                }

                // 判断订单是否存在
                for (MesPmWorkOrder item : workOrderDtos) {
                    if (item.getWorkOrderCode().equals(order.getWorkOrderCode())) {
                        workOrder.setWorkOrderId(item.getWorkOrderId());
                        order.setWorkOrderId(item.getWorkOrderId().toString());
                        break;
                    }
                }
                if (StringUtils.isNotEmpty(workOrder.getWorkOrderId())) {
                    // 修改订单
                    updateList.add(workOrder);
                } else {
                    // 新增订单
                    addList.add(workOrder);
                    // 保存中间库
//                    DynamicDataSourceHolder.putDataSouce("secondary");
//                    middleOrderMapper.save(order);
//                    DynamicDataSourceHolder.removeDataSource();
                }
            }
            long current3 = System.currentTimeMillis();
            long res3 = current3 - current2;
            log.info("========== 数据梳理以及筛选耗时:" + res3);
            // 保存平台库
            if (!addList.isEmpty()) {
                pmFeignApi.addList(addList);
            }
            if (!updateList.isEmpty()) {
                pmFeignApi.batchUpdate(updateList);
            }
            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            logList.add(apiLog);
            if (!logList.isEmpty()) {
                securityFeignApi.batchAdd(logList);
            }

            long current4 = System.currentTimeMillis();
            long res5 = current4 - current0;
            log.info("========== 同步总耗时:" + res5);
        }
    }

    @Override
    public void syncSaleOrderData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // 记录日志
        List<SysApiLog> logList = new ArrayList<>();
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("万宝数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 0);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl("查询数据库-同步销售订单");
        apiLog.setOrgId(sysUser.getOrganizationId());

        if (specItems.isEmpty()) {
            // 记录日志
            apiLog.setResponseData("获取平台同步数据配置项不存在，不同步数据");
            logList.add(apiLog);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        }

        // 执行查询前调用函数执行存储过程
        middleSaleOrderMapper.setPolicy();
        List<MiddleSaleOrder> salesOrders = middleSaleOrderMapper.findSaleOrderData(map);
        if (!salesOrders.isEmpty()) {
            // 记录日志
            long start = System.currentTimeMillis();
            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncSaleOrder().getData();
            // 客户集合
            List<BaseSupplier> baseSuppliers = wanbaoBaseBySyncDto.getBaseSupplierList();
            // 物料集合
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();
            // 条码规则集合
            List<BaseBarcodeRuleSetDto> ruleSetDtos = wanbaoBaseBySyncDto.getBarcodeRuleSetDtoList();
            if (ruleSetDtos.isEmpty()) {
                // 记录日志
                apiLog.setResponseData("平台默认条码规则集合不存在，不同步工单数据");
                logList.add(apiLog);
                return;
            }
            // 销售订单
            List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findSalesOrderAll().getData();

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
                    // 客户编码
                    if (StringUtils.isEmpty(saleOrder.getSupplierCode())) {
                        // 记录日志
                        apiLog.setResponseData(saleOrder.getSalesOrderCode() + "此销售订单客户编码为空，不同步此条订单数据");
                        logList.add(apiLog);
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
                        // 平台没有此客户信息，自动新增
                        BaseSupplier baseSupplier = new BaseSupplier();
                        baseSupplier.setSupplierCode(saleOrder.getSupplierCode());
                        baseSupplier.setSupplierName(saleOrder.getSupplierName());
                        baseSupplier.setSupplierType((byte) 2);
                        baseSupplier.setStatus((byte) 1);
                        Long supplierId = baseFeignApi.saveForReturnID(baseSupplier).getData();
                        saleOrder.setSupplierId(supplierId.toString());
                        omSalesOrder.setSupplierId(supplierId);
                    }

                    // 物料
                    if (StringUtils.isEmpty(saleOrder.getMaterialCode())) {
                        // 记录日志
                        apiLog.setResponseData(saleOrder.getSalesOrderCode() + "此销售订单物料编码为空，不同步此条订单数据");
                        logList.add(apiLog);
                        break;
                    }
                    for (BaseMaterial material : baseMaterials) {
                        if (saleOrder.getMaterialCode().equals(material.getMaterialCode())) {
                            saleOrder.setMaterialId(material.getMaterialId().toString());
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(saleOrder.getMaterialId())) {
                        // 记录日志
                        apiLog.setResponseData(saleOrder.getSalesOrderCode() + "此销售订单编号在系统中不存在此物料编码" + saleOrder.getMaterialCode() + "，不同步此条订单数据");
                        logList.add(apiLog);
                        break;
                    }
                    OmSalesOrderDetDto detDto = new OmSalesOrderDetDto();
                    BeanUtil.copyProperties(saleOrder, detDto);
                    // 判断订单是否存在
                    for (OmSalesOrderDto dto : salesOrderDtos) {
                        if (dto.getSalesOrderCode().equals(saleOrder.getSalesOrderCode())) {
                            omSalesOrder.setSalesOrderId(dto.getSalesOrderId());
                            saleOrder.setSaleOrderId(dto.getSalesOrderId().toString());
                            for (OmSalesOrderDetDto oldDetDto : dto.getOmSalesOrderDetDtoList()) {
                                if (oldDetDto.getSalesCode().equals(saleOrder.getSalesCode())) {
                                    detDto.setSalesOrderDetId(oldDetDto.getSalesOrderDetId());
                                }
                            }
                            break;
                        }
                    }

                    omSalesOrderDetDtoList.add(detDto);
                }
                // 保存平台库
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
//                // 保存中间库
//                DynamicDataSourceHolder.putDataSouce("secondary");
//                for (MiddleSaleOrder item : list){
//                    item.setSaleOrderId(UUIDUtils.getUUID());
//                    middleSaleOrderMapper.save(item);
//                }
//                DynamicDataSourceHolder.removeDataSource();
//            }

            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            logList.add(apiLog);
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
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("万宝数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 0);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl("查询数据库-同步出库单");//出货通知单
        apiLog.setOrgId(sysUser.getOrganizationId());

        if (specItems.isEmpty()) {
            // 记录日志
            apiLog.setResponseData("获取平台同步数据配置项不存在，不同步数据");
            logList.add(apiLog);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        }

        // 执行查询前调用函数执行存储过程
        middleOutDeliveryOrderMapper.setPolicy();
        List<MiddleOutDeliveryOrder> deliveryOrders = middleOutDeliveryOrderMapper.findOutDeliveryData(map);
        DynamicDataSourceHolder.putDataSouce("fourth");
        List<MiddleOutDeliveryOrder> orderFormIMS = middleOutDeliveryOrderMapper.findOutDeliveryDataFormIMS(map);
        DynamicDataSourceHolder.removeDataSource();
        log.info("获取万宝出库单数据，当前获取数量：" + deliveryOrders.size() + "IMS系统出库单数量：" + orderFormIMS.size());
        if (!orderFormIMS.isEmpty() && orderFormIMS.size() > 0) {
            deliveryOrders.addAll(orderFormIMS);
        }
        if (!deliveryOrders.isEmpty()) {
            // 记录日志
            long start = System.currentTimeMillis();

            WanbaoBaseBySyncDto wanbaoBaseBySyncDto = baseFeignApi.findBySyncOutDelivery().getData();
            // 客户
            List<BaseSupplier> baseSuppliers = wanbaoBaseBySyncDto.getBaseSupplierList();
            // 货主
            List<BaseMaterialOwnerDto> ownerDtos = wanbaoBaseBySyncDto.getMaterialOwnerDtoList();
            // 物料集合
            List<BaseMaterialDto> baseMaterials = wanbaoBaseBySyncDto.getMaterialDtoList();

            SearchWmsOutDeliveryOrder outDeliveryOrder = new SearchWmsOutDeliveryOrder();
            outDeliveryOrder.setPageSize(999999);
            List<WmsOutDeliveryOrderDto> deliveryOrderDtos = outFeignApi.findList(outDeliveryOrder).getData();

            // 保存平台库
            List<MiddleOutDeliveryOrder> list = new ArrayList<>();
            Map<String, List<MiddleOutDeliveryOrder>> listMap = deliveryOrders.stream().collect(Collectors.groupingBy(MiddleOutDeliveryOrder::getDeliveryOrderCode));
            listMap.forEach((key, value) -> {
                WmsOutDeliveryOrder order = new WmsOutDeliveryOrder();
                BeanUtil.copyProperties(value.get(0), order);
                order.setOrderTypeId(1L); // 销售出库
                order.setOrgId(sysUser.getOrganizationId());
                order.setMaterialOwnerId(ownerDtos.get(0).getMaterialOwnerId());
                order.setIfCreatedJobOrder((byte) 0);

                // 客户编码
                if (StringUtils.isEmpty(value.get(0).getCustomerCode())) {
                    // 记录日志
                    apiLog.setResponseData(value.get(0).getCustomerCode() + "此销售订单客户编码为空，不同步此条出库单订单数据");
                    logList.add(apiLog);
                }
                for (BaseSupplier supplier : baseSuppliers) {
                    if (supplier.getSupplierCode().equals(value.get(0).getCustomerCode())) {
                        order.setSupplierId(supplier.getSupplierId());
                        break;
                    }
                }
                if (StringUtils.isEmpty(order.getSupplierId())) {
                    // 平台没有此客户信息，自动新增
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
                    log.info("============= json数据" + JSON.toJSONString(orderDet));

                    // 物料
                    if (StringUtils.isEmpty(item.getMaterialCode())) {
                        apiLog.setResponseData(item.getDeliveryOrderCode() + "此出货通知书数据中物料编码为空，不同步此条数据");
                        logList.add(apiLog);
                        continue;
                    }
                    for (BaseMaterial material : baseMaterials) {
                        if (material.getMaterialCode().equals(item.getMaterialCode())) {
                            orderDet.setMaterialId(material.getMaterialId());
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(orderDet.getMaterialId())) {
                        // 记录日志
                        apiLog.setResponseData(item.getDeliveryOrderCode() + "此出货通知书编号在系统中没有匹配的物料" + item.getMaterialCode() + "，不同步此条数据");
                        logList.add(apiLog);
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
//                // 保存中间库
//                DynamicDataSourceHolder.putDataSouce("secondary");
//                for (MiddleOutDeliveryOrder item : list){
//                    item.setDeliveryOrderId(UUIDUtils.getUUID());
//                    middleOutDeliveryOrderMapper.save(item);
//                }
//                DynamicDataSourceHolder.removeDataSource();
//            }

            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            logList.add(apiLog);
        }
        if (!logList.isEmpty()) {
            securityFeignApi.batchAdd(logList);
        }
    }

    @Override
    public void syncBarcodeData(boolean flag) {

        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // 记录日志
        List<SysApiLog> logList = new ArrayList<>();
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("万宝数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 0);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl("查询数据库-同步产品条码");
        apiLog.setOrgId(sysUser.getOrganizationId());

        if (StringUtils.isEmpty(specItems)) {
            // 记录日志
            apiLog.setResponseData("获取平台同步数据配置项不存在，不同步数据");
            logList.add(apiLog);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        // flag = true时条件查询，=false时查询所有
        if (flag) {
            JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
            if ("0".equals(jsonObject.get("all"))) {
                map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
            } else {
                map.put("date", "2021-09-01");
            }
        }
        // 执行查询
        DynamicDataSourceHolder.putDataSouce("thirdary");
        List<MiddleProduct> barcodeDatas = middleProductMapper.findBarcodeData(map);
        log.info("============== 同步PQMS数据barcodeDatas: " + barcodeDatas.size());
        DynamicDataSourceHolder.removeDataSource();

        if (!barcodeDatas.isEmpty()) {
            log.info("============ 开始同步PQMS数据");
            WanbaoBaseBySyncDto syncDto = baseFeignApi.findBySyncBarcodeData().getData();
            // 产线
            BaseProLine proLine = new BaseProLine();
            List<BaseProLine> proLines = syncDto.getProLineList();
            for (BaseProLine entity : proLines) {
                if (entity.getProCode().equals("A")) {
                    proLine = entity;
                    break;
                }
            }
            if (StringUtils.isEmpty(proLine.getProLineId())) {
                apiLog.setResponseData("同步PQMS数据失败，系统中为A的编码产线不存在或已被删除");
                logList.add(apiLog);
                return;
            }

            // 标签类别
            List<BaseLabelCategoryDto> categoryDtoList = syncDto.getLabelCategoryDtoList();
            Long labelCategoryId = null;
            for (BaseLabelCategoryDto entity : categoryDtoList) {
                if (entity.getLabelCategoryCode().equals("01")) {
                    labelCategoryId = entity.getLabelCategoryId();
                    break;
                }
            }
            if (labelCategoryId == null){
                apiLog.setResponseData("同步PQMS数据失败，系统中厂内码标签类别数据不存在或已被删除");
                logList.add(apiLog);
                return;
            }

            // 工艺路线
            BaseProductProcessRoute productProcessRoute = new BaseProductProcessRoute();
            List<BaseProductProcessRoute> productProcessRouteList = syncDto.getProcessRouteList();
            for (BaseProductProcessRoute entity : productProcessRouteList) {
                if (proLine.getProLineId().equals(entity.getProLineId())) {
                    productProcessRoute = entity;
                    break;
                }
            }
            if (StringUtils.isEmpty(productProcessRoute.getProductProcessRouteId())) {
                apiLog.setResponseData("同步PQMS数据失败，系统中A线的产品工艺路线数据不存在或已被删除");
                logList.add(apiLog);
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
                apiLog.setResponseData("同步PQMS数据失败，系统中A线的产品工艺路线与工序数据不存在或已被删除");
                logList.add(apiLog);
                return;
            }
            BaseRouteProcess routeProcess = list.stream().sorted(Comparator.comparing(BaseRouteProcess::getOrderNum)).findFirst().get();

            // 工单
            List<MesPmWorkOrder> workOrders = pmFeignApi.findWorkOrderAll().getData();

            // 条码
            SyncFindBarcodeDto findBarcodeDto = sfcFeignApi.syncFindBarcode(labelCategoryId).getData();
            List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtos = findBarcodeDto.getWorkOrderBarcodes();

            // 条码流程表
            List<MesSfcBarcodeProcess> sfcBarcodeProcesses = findBarcodeDto.getBarcodeProcesses();

            log.info("============ sfcBarcodeProcesses: " + sfcBarcodeProcesses.size());
            log.info("============ workOrderBarcodeDtos: " + workOrderBarcodeDtos.size());
            // 记录日志
            long start = System.currentTimeMillis();
            List<MesSfcBarcodeProcess> updateBarcodeProcess = new ArrayList<>();
            List<BatchSyncBarcodeSaveDto> saveList = new ArrayList<>();
            for (MiddleProduct middleProduct : barcodeDatas) {
                if (StringUtils.isEmpty(middleProduct) || StringUtils.isEmpty(middleProduct.getCustomerBarcode()) || StringUtils.isEmpty(middleProduct.getBarcode())) {
                    continue;
                }

                // 匹配工单
                MesPmWorkOrder workOrder = new MesPmWorkOrder();
                for (MesPmWorkOrder entity : workOrders) {
                    if (entity.getWorkOrderCode().equals(middleProduct.getWorkOrderCode())) {
                        workOrder = entity;
                        break;
                    }
                }
                if (StringUtils.isEmpty(workOrder.getWorkOrderCode())) {
                    apiLog.setResponseData("同步PQMS数据失败，PQMS条码" + middleProduct.getBarcode() + "的工单：" + middleProduct.getWorkOrderCode() + "在系统中不存在，不同步此条码");
                    logList.add(apiLog);
                    continue;
                }

                // 匹配条码
                boolean hasBarcode = false;
                boolean isUsed = false;
                for (MesSfcWorkOrderBarcodeDto dto : workOrderBarcodeDtos) {
                    if (dto.getBarcode().equals(middleProduct.getBarcode())) {
                        if (dto.getBarcodeStatus().equals((byte) 1) || dto.getBarcodeStatus().equals((byte) 2)) {
                            isUsed = true;
                        }
                        hasBarcode = true;
                        break;
                    }
                }

                if (!hasBarcode) {
                    // 保存条码表
                    MesSfcWorkOrderBarcode workOrderBarcode = new MesSfcWorkOrderBarcode();
                    workOrderBarcode.setBarcode(middleProduct.getBarcode());
                    workOrderBarcode.setLabelCategoryId(labelCategoryId);
                    workOrderBarcode.setOption1("2");
                    workOrderBarcode.setWorkOrderId(workOrder.getWorkOrderId());
                    workOrderBarcode.setWorkOrderCode(workOrder.getWorkOrderCode());
                    workOrderBarcode.setBarcodeStatus((byte) 0);
                    workOrderBarcode.setPrintTime(new Date());
                    workOrderBarcode.setCreateTime(new Date());
                    workOrderBarcode.setCreateUserId(sysUser.getUserId());
                    workOrderBarcode.setModifiedTime(new Date());
                    workOrderBarcode.setModifiedUserId(sysUser.getUserId());
                    workOrderBarcode.setOrgId(sysUser.getOrganizationId());
                    workOrderBarcode.setCreateBarcodeTime(new Date());
                    workOrderBarcode.setIsDelete((byte) 1);

                    // 保存条码流程表
                    MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                    mesSfcBarcodeProcess.setWorkOrderId(workOrder.getWorkOrderId());
                    mesSfcBarcodeProcess.setWorkOrderCode(workOrder.getWorkOrderCode());
                    mesSfcBarcodeProcess.setBarcodeType((byte) 2);
                    mesSfcBarcodeProcess.setBarcode(middleProduct.getBarcode());
                    mesSfcBarcodeProcess.setProLineId(proLine.getProLineId());
                    mesSfcBarcodeProcess.setProcessCode(routeProcess.getProcessCode());
                    mesSfcBarcodeProcess.setMaterialId(workOrder.getMaterialId());
                    mesSfcBarcodeProcess.setMaterialCode(middleProduct.getMaterialCode());
                    mesSfcBarcodeProcess.setMaterialName(productProcessRoute.getMaterialName());
                    mesSfcBarcodeProcess.setRouteId(productProcessRoute.getRouteId());
                    mesSfcBarcodeProcess.setRouteCode(productProcessRoute.getRouteCode());
                    mesSfcBarcodeProcess.setRouteName(productProcessRoute.getRouteName());
                    mesSfcBarcodeProcess.setProcessId(routeProcess.getProcessId());
                    mesSfcBarcodeProcess.setProcessName(routeProcess.getProcessName());
                    mesSfcBarcodeProcess.setNextProcessId(routeProcess.getProcessId());
                    mesSfcBarcodeProcess.setNextProcessName(routeProcess.getProcessName());
                    mesSfcBarcodeProcess.setSectionId(routeProcess.getSectionId());
                    mesSfcBarcodeProcess.setSectionName(routeProcess.getSectionName());
                    mesSfcBarcodeProcess.setCreateTime(new Date());
                    mesSfcBarcodeProcess.setCreateUserId(sysUser.getUserId());
                    mesSfcBarcodeProcess.setModifiedTime(new Date());
                    mesSfcBarcodeProcess.setModifiedUserId(sysUser.getUserId());
                    mesSfcBarcodeProcess.setOrgId(sysUser.getOrganizationId());
                    mesSfcBarcodeProcess.setIsDelete((byte) 1);

                    BatchSyncBarcodeSaveDto batchSyncBarcodeSaveDto = new BatchSyncBarcodeSaveDto();
                    batchSyncBarcodeSaveDto.setBarcodeProcess(mesSfcBarcodeProcess);
                    batchSyncBarcodeSaveDto.setWorkOrderBarcode(workOrderBarcode);
                    saveList.add(batchSyncBarcodeSaveDto);
                } else {
                    // 修改条码流程表三星客户条码
                    for (MesSfcBarcodeProcess entity : sfcBarcodeProcesses) {
                        if (entity.getBarcode().equals(middleProduct.getBarcode()) && !isUsed) {
                            entity.setCustomerBarcode(middleProduct.getCustomerBarcode());
                            updateBarcodeProcess.add(entity);
                        }
                    }
                }
            }
            // 批量处理
            log.info("============ updateBarcodeProcess: " + updateBarcodeProcess.size());
            log.info("============ saveList: " + saveList.size());
            if (updateBarcodeProcess.size() > 0 || saveList.size() > 0) {
                BatchSyncBarcodeDto batchSyncBarcodeDto = new BatchSyncBarcodeDto();
                batchSyncBarcodeDto.setUpdateList(updateBarcodeProcess);
                batchSyncBarcodeDto.setList(saveList);
                sfcFeignApi.batchSyncBarcode(batchSyncBarcodeDto);
            }

            // 保存中间库
//           DynamicDataSourceHolder.putDataSouce("secondary");
//           for(MiddleProduct middleProduct : barcodeDatas){
//                middleProductMapper.save(middleProduct);
//           }
//           DynamicDataSourceHolder.removeDataSource();

            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            logList.add(apiLog);
        }
        if (!logList.isEmpty()) {
            securityFeignApi.batchAdd(logList);
        }
    }

}
