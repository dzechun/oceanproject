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
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrder;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
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
    private AuthFeignApi securityFeignApi;

    @Resource
    private SFCFeignApi sfcFeignApi;

    // endregion

    @Override
    public void syncMaterialData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

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
            securityFeignApi.add(apiLog);
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

            // 1、保存平台库
            // 产品型号集合
            List<BaseProductModel> productModels = baseFeignApi.findProductModelAll().getData();
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
            // 物料页签集合
            List<BaseTabDto> baseTabDtos = baseFeignApi.findTabAll().getData();

            // 条码规则集合
            SearchBaseBarcodeRuleSet barcodeRuleSet = new SearchBaseBarcodeRuleSet();
            barcodeRuleSet.setPageSize(9999);
            List<BaseBarcodeRuleSetDto> ruleSetDtos = baseFeignApi.findBarcodeRuleSetList(barcodeRuleSet).getData();
            if(ruleSetDtos.isEmpty()){
                // 记录日志
                apiLog.setResponseData("平台默认条码规则集合不存在，不同步工单数据");
                securityFeignApi.add(apiLog);
                return;
            }

            Long ruleSetId = 0L;
            for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos){
                if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))){
                    ruleSetId = ruleSetDto.getBarcodeRuleSetId();
                    break;
                }
            }

            for (MiddleMaterial dto : middleMaterials) {
                if (dto.getProductModelCode() != null) {
                    for (BaseProductModel item : productModels) {
                        if (item.getProductModelCode().equals(dto.getProductModelCode())) {
                            dto.setProductModelId(item.getProductModelId().toString());
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(dto.getProductModelId())) {
                        BaseProductModel baseProductModel = new BaseProductModel();
                        baseProductModel.setProductModelName(dto.getProductModelCode());
                        baseProductModel.setProductModelCode(dto.getProductModelCode());
                        baseProductModel.setProductModelDesc(dto.getMaterialCode());
                        baseProductModel.setStatus(1);
                        Long id = baseFeignApi.addForReturnId(baseProductModel).getData();
                        dto.setProductModelId(id.toString());
                    }
                }
                long MaterialCount = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).count();
                if (MaterialCount <= 0) {
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
                    for (BaseTabDto item : baseTabDtos){
                        if (item.getMaterialId().equals(material.getMaterialId())){
                            BaseTab tab = new BaseTab();
                            BeanUtil.copyProperties(item, tab);
                            // FG成品 SA半成品
                            if (dto.getMaterialProperty().equals("FG")) {
                                tab.setMaterialProperty((byte) 1);
                            } else {
                                tab.setMaterialProperty((byte) 0);
                            }
                            tab.setVoltage(dto.getVoltage());
                            if(StringUtils.isNotEmpty(dto.getProductModelId())){
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
            securityFeignApi.add(apiLog);
        }
    }

    @Override
    public void syncOrderData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // 记录日志
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
            securityFeignApi.add(apiLog);
            return;
        }
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        }
        List<MiddleOrder> workOrders = middleOrderMapper.findOrderData(map);
        log.info("获取万宝工单数据，当前获取数量：" + workOrders.size());
        if (!workOrders.isEmpty()) {
            // 记录日志
            long start = System.currentTimeMillis();

            // 获取所有销售订单
            List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findSalesOrderAll().getData();
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
            // 工单集合
            List<MesPmWorkOrderDto> workOrderDtos = pmFeignApi.findWorkOrderAll().getData();
            // 产线集合
            List<BaseProLine> proLines = baseFeignApi.findProLineAll().getData();
            // 工艺路线
            SearchBaseRoute baseRoute = new SearchBaseRoute();
            baseRoute.setPageSize(9999);
            List<BaseRoute> baseRoutes = baseFeignApi.findRouteList(baseRoute).getData();
            if(baseRoutes.isEmpty()){
                // 记录日志
                apiLog.setResponseData("平台默认工艺路线不存在，不同步工单数据");
                securityFeignApi.add(apiLog);
                return;
            }
            // 条码规则集合
            SearchBaseBarcodeRuleSet barcodeRuleSet = new SearchBaseBarcodeRuleSet();
            barcodeRuleSet.setPageSize(9999);
            List<BaseBarcodeRuleSetDto> ruleSetDtos = baseFeignApi.findBarcodeRuleSetList(barcodeRuleSet).getData();
            if(ruleSetDtos.isEmpty()){
                // 记录日志
                apiLog.setResponseData("平台默认条码规则集合不存在，不同步工单数据");
                securityFeignApi.add(apiLog);
                return;
            }

            // 通过程序配置项取工艺路线、规则集合
            Long routeId = 0L;
            for (BaseRoute route : baseRoutes){
                if (route.getRouteCode().equals(jsonObject.get("routeCode"))){
                    routeId = route.getRouteId();
                    break;
                }
            }
            // 工艺路线工序关系
            List<BaseRouteProcess> routeProcessList = baseFeignApi.findConfigureRout(routeId).getData();

            Long ruleSetId = 0L;
            for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos){
                if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))){
                    ruleSetId = ruleSetDto.getBarcodeRuleSetId();
                    break;
                }
            }

            List<MesPmWorkOrder> addList = new ArrayList<>();
            List<MesPmWorkOrder> updateList = new ArrayList<>();
            for (MiddleOrder order : workOrders) {
                MesPmWorkOrder workOrder = new MesPmWorkOrder();
                BeanUtil.copyProperties(order, workOrder);
                workOrder.setRouteId(routeId);
                workOrder.setPutIntoProcessId(routeProcessList.get(0).getProcessId());
                workOrder.setOutputProcessId(routeProcessList.get(routeProcessList.size()-1).getProcessId());
                workOrder.setBarcodeRuleSetId(ruleSetId);
                workOrder.setIsDelete((byte) 1);
                workOrder.setOrgId(sysUser.getOrganizationId());
                workOrder.setScheduledQty(workOrder.getWorkOrderQty());
                workOrder.setProductionQty(BigDecimal.ZERO);
                workOrder.setWorkOrderStatus((byte) 1);
                // 销售订单
//                if (StringUtils.isEmpty(order.getSalesOrderCode())) {
//                    apiLog.setResponseData(order.getWorkOrderCode() + "此生产订单数据中销售订单号为空，不同步此条订单数据");
//                    securityFeignApi.add(apiLog);
//                    continue;
//                }
                for (OmSalesOrderDto item : salesOrderDtos) {
                    if (item.getSalesOrderCode().equals(order.getSalesOrderCode())) {
                        workOrder.setSalesOrderId(item.getSalesOrderId());
                        break;
                    }
                }
//                if (StringUtils.isEmpty(workOrder.getSalesOrderId())) {
//                    // 记录日志
//                    apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号在系统中没有匹配的销售订单号" + order.getSalesOrderCode() + "，不同步此条订单数据");
//                    securityFeignApi.add(apiLog);
//                    continue;
//                }

                // 物料
                if (StringUtils.isEmpty(order.getMaterialCode())) {
                    apiLog.setResponseData(order.getWorkOrderCode() + "此生产订单数据中物料编码为空，不同步此条订单数据");
                    securityFeignApi.add(apiLog);
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
                    securityFeignApi.add(apiLog);
                    continue;
                }

                // 产线
                if (StringUtils.isEmpty(order.getProCode())) {
                    // 记录日志
                    apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号的产线编码为空，不同步此条订单数据");
                    securityFeignApi.add(apiLog);
                    continue;
                }
                for (BaseProLine item : proLines) {
                    if (item.getProCode().equals(order.getProCode())) {
                        workOrder.setProLineId(item.getProLineId());
                        break;
                    }
                }
                if (StringUtils.isEmpty(workOrder.getProLineId())) {
                    // 记录日志
                    apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号在系统中没有匹配的产线" + order.getProCode() + "，不同步此条订单数据");
                    securityFeignApi.add(apiLog);
                    continue;
                }

                // 判断订单是否存在
                for (MesPmWorkOrderDto item : workOrderDtos) {
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
            // 保存平台库
            if (!addList.isEmpty()){
                pmFeignApi.addList(addList);
            }
            if (!updateList.isEmpty()){
                pmFeignApi.batchUpdate(updateList);
            }

            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            securityFeignApi.add(apiLog);
        }
    }

    @Override
    public void syncSaleOrderData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // 记录日志
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
            securityFeignApi.add(apiLog);
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
        log.info("获取万宝销售订单数据，当前获取数量：" + salesOrders.size());
        if (!salesOrders.isEmpty()) {
            // 记录日志
            long start = System.currentTimeMillis();
            // 客户集合
            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierAll().getData();
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
            // 销售订单
            List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findSalesOrderAll().getData();
            // 条码规则集合
            SearchBaseBarcodeRuleSet barcodeRuleSet = new SearchBaseBarcodeRuleSet();
            barcodeRuleSet.setPageSize(9999);
            List<BaseBarcodeRuleSetDto> ruleSetDtos = baseFeignApi.findBarcodeRuleSetList(barcodeRuleSet).getData();
            if(ruleSetDtos.isEmpty()){
                // 记录日志
                apiLog.setResponseData("平台默认条码规则集合不存在，不同步工单数据");
                securityFeignApi.add(apiLog);
                return;
            }

            List<MiddleSaleOrder> list = new ArrayList<>();
            Map<String, List<MiddleSaleOrder>> collect = salesOrders.stream().collect(Collectors.groupingBy(MiddleSaleOrder::getSalesOrderCode));
            collect.forEach((key, value) -> {
                Map<BigDecimal, List<MiddleSaleOrder>> listMap = value.stream().sorted(Comparator.comparing(MiddleSaleOrder::getModifiedTime))
                        .collect(Collectors.groupingBy(MiddleSaleOrder::getCustomerOrderLineNumber));
                List<MiddleSaleOrder> orders = new ArrayList<>();
                listMap.forEach((itemkey, itemvalue) -> {
                    orders.add(itemvalue.get(0));
                });



                OmSalesOrderDto omSalesOrder =  new OmSalesOrderDto();
                BeanUtil.copyProperties(orders.get(0), omSalesOrder);
                for (BaseBarcodeRuleSetDto ruleSetDto : ruleSetDtos){
                    if (ruleSetDto.getBarcodeRuleSetCode().equals(jsonObject.get("ruleCode"))){
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
                        securityFeignApi.add(apiLog);
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
                        // 记录日志
                        apiLog.setResponseData(saleOrder.getSalesOrderCode() + "此销售订单编号在系统中不存在此客户编码" + saleOrder.getSupplierCode() + "，不同步此条订单数据");
                        securityFeignApi.add(apiLog);
                        break;
                    }

                    // 物料
                    if (StringUtils.isEmpty(saleOrder.getMaterialCode())) {
                        // 记录日志
                        apiLog.setResponseData(saleOrder.getSalesOrderCode() + "此销售订单物料编码为空，不同步此条订单数据");
                        securityFeignApi.add(apiLog);
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
                        securityFeignApi.add(apiLog);
                        break;
                    }
                    // 判断订单是否存在
                    for (OmSalesOrderDto dto : salesOrderDtos) {
                        if (dto.getSalesOrderCode().equals(saleOrder.getSalesOrderCode())) {
                            omSalesOrder.setSalesOrderId(dto.getSalesOrderId());
                            saleOrder.setSaleOrderId(dto.getSalesOrderId().toString());
                            break;
                        }
                    }
                    OmSalesOrderDetDto detDto = new OmSalesOrderDetDto();
                    BeanUtil.copyProperties(saleOrder, detDto);
                    omSalesOrderDetDtoList.add(detDto);
                }
                // 保存平台库
                if(!omSalesOrderDetDtoList.isEmpty()){
                    omSalesOrder.setSalesUserName(omSalesOrder.getMakeOrderUserName());
                    omSalesOrder.setOmSalesOrderDetDtoList(omSalesOrderDetDtoList);
                    if (StringUtils.isNotEmpty(omSalesOrder.getSalesOrderId())) {
                        omFeignApi.update(omSalesOrder);
                    } else {
                        list.addAll(orders);
                        omSalesOrder.setOrderStatus((byte) 1);
                        omSalesOrder.setStatus((byte) 1);
                        log.info("omSalesOrder=========" + JSON.toJSONString(omSalesOrder));
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
            securityFeignApi.add(apiLog);
        }
    }

    @Override
    public void syncOutDeliveryData() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("万宝数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 0);
        apiLog.setApiModule("ocean-wanbao-api");
        apiLog.setApiUrl("查询数据库-同步出库单");
        apiLog.setOrgId(sysUser.getOrganizationId());

        if (specItems.isEmpty()) {
            // 记录日志
            apiLog.setResponseData("获取平台同步数据配置项不存在，不同步数据");
            securityFeignApi.add(apiLog);
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
        log.info("获取万宝出库单数据，当前获取数量：" + deliveryOrders.size());
        if (!deliveryOrders.isEmpty()){
            // 记录日志
            long start = System.currentTimeMillis();

            // 收货人
//            List<BaseConsignee> baseConsignees = baseFeignApi.findConsigneeAll().getData();
            // 客户
            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierAll().getData();
            // 货主
            List<BaseMaterialOwnerDto> ownerDtos = baseFeignApi.findMaterialOwnerAll().getData();
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();

            SearchWmsOutDeliveryOrder outDeliveryOrder = new SearchWmsOutDeliveryOrder();
            outDeliveryOrder.setPageSize(999999);
            List<WmsOutDeliveryOrderDto> deliveryOrderDtos = outFeignApi.findList(outDeliveryOrder).getData();

            // 保存平台库
            List<MiddleOutDeliveryOrder> list = new ArrayList<>();
            Map<String, List<MiddleOutDeliveryOrder>> listMap = deliveryOrders.stream().collect(Collectors.groupingBy(MiddleOutDeliveryOrder::getDeliveryOrderCode));
            listMap.forEach((key, value) ->{
                WmsOutDeliveryOrder order = new WmsOutDeliveryOrder();
                BeanUtil.copyProperties(value.get(0), order);
                order.setOrderTypeId(1L); // 销售出库
                order.setOrgId(sysUser.getOrganizationId());
                order.setMaterialOwnerId(ownerDtos.get(0).getMaterialOwnerId());

                // 客户编码
                if (StringUtils.isEmpty(value.get(0).getCustomerCode())) {
                    // 记录日志
                    apiLog.setResponseData(value.get(0).getCustomerCode() + "此销售订单客户编码为空，不同步此条出库单订单数据");
                    securityFeignApi.add(apiLog);
                }
                for (BaseSupplier supplier : baseSuppliers) {
                    if (supplier.getSupplierCode().equals(value.get(0).getCustomerCode())) {
                        order.setSupplierId(supplier.getSupplierId());
                        break;
                    }
                }
                if (StringUtils.isEmpty(order.getSupplierId())) {
                    // 记录日志
                    apiLog.setResponseData(value.get(0).getDeliveryOrderCode() + "此销售订单编号在系统中不存在此客户编码" + value.get(0).getCustomerCode() + "，不同步此条订单数据");
                    securityFeignApi.add(apiLog);
                }else {
                    List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = new ArrayList<>();
                    for (MiddleOutDeliveryOrder item : value){
                        WmsOutDeliveryOrderDetDto orderDet = new WmsOutDeliveryOrderDetDto();
                        BeanUtil.copyProperties(item, orderDet);
                        log.info("============= json数据" + JSON.toJSONString(orderDet));

                        // 物料
                        if (StringUtils.isEmpty(item.getMaterialCode())) {
                            apiLog.setResponseData(item.getDeliveryOrderCode() + "此出货通知书数据中物料编码为空，不同步此条数据");
                            securityFeignApi.add(apiLog);
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
                            securityFeignApi.add(apiLog);
                            continue;
                        }
                        wmsOutDeliveryOrderDetList.add(orderDet);
                    }

                    boolean flag = true;
                    for (WmsOutDeliveryOrderDto dto : deliveryOrderDtos){
                        if (dto.getDeliveryOrderCode().equals(order.getDeliveryOrderCode())){
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        if (!wmsOutDeliveryOrderDetList.isEmpty()){
                            order.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetList);
                            outFeignApi.add(order);
                            list.addAll(value);
                        }
                    }else {
                        outFeignApi.update(order);
                    }
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
            securityFeignApi.add(apiLog);
        }
    }

    @Override
    public void syncBarcodeData() {

        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        // 记录日志
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
            securityFeignApi.add(apiLog);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
        if ("0".equals(jsonObject.get("all"))) {
            map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        }
        // 执行查询
        DynamicDataSourceHolder.putDataSouce("thirdary");
        List<MiddleProduct> barcodeDatas = middleProductMapper.findBarcodeData(map);
        log.info("------barcodeDatas----------"+barcodeDatas);
        DynamicDataSourceHolder.removeDataSource();

        if (StringUtils.isNotEmpty(barcodeDatas)) {
            // 记录日志
           long start = System.currentTimeMillis();
           for(MiddleProduct middleProduct : barcodeDatas){
               if(StringUtils.isEmpty(middleProduct) || StringUtils.isEmpty(middleProduct.getCustomerBarcode()) || StringUtils.isEmpty(middleProduct.getBarcode())){
                   continue;
               }
               SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
               searchMesSfcBarcodeProcess.setBarcode(middleProduct.getBarcode());
               List<MesSfcBarcodeProcess> mesSfcBarcodeProcess = sfcFeignApi.findBarcode(searchMesSfcBarcodeProcess).getData();
               if(StringUtils.isNotEmpty(mesSfcBarcodeProcess) && StringUtils.isEmpty(mesSfcBarcodeProcess.get(0).getCustomerBarcode())){
                       mesSfcBarcodeProcess.get(0).setCustomerBarcode(middleProduct.getCustomerBarcode());
                       sfcFeignApi.update(mesSfcBarcodeProcess.get(0));
               }else{
                   apiLog.setResponseData(middleProduct.getBarcode() + "，此产品条码未匹配到对应的过站数据，不同步此条数据");
                   securityFeignApi.add(apiLog);
                   continue;
               }

           }

           // 保存中间库
           DynamicDataSourceHolder.putDataSouce("secondary");
           for(MiddleProduct middleProduct : barcodeDatas){
                middleProductMapper.save(middleProduct);
           }
           DynamicDataSourceHolder.removeDataSource();

            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            securityFeignApi.add(apiLog);
        }



    }

}
