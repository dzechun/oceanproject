package com.fantechs.provider.wanbao.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wanbao.api.config.DynamicDataSourceHolder;
import com.fantechs.provider.wanbao.api.entity.MiddleMaterial;
import com.fantechs.provider.wanbao.api.entity.MiddleOrder;
import com.fantechs.provider.wanbao.api.entity.MiddleOutDeliveryOrder;
import com.fantechs.provider.wanbao.api.entity.MiddleSaleOrder;
import com.fantechs.provider.wanbao.api.mapper.MiddleMaterialMapper;
import com.fantechs.provider.wanbao.api.mapper.MiddleOrderMapper;
import com.fantechs.provider.wanbao.api.mapper.MiddleOutDeliveryOrderMapper;
import com.fantechs.provider.wanbao.api.mapper.MiddleSaleOrderMapper;
import com.fantechs.provider.wanbao.api.service.SyncDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private BaseFeignApi baseFeignApi;

    @Resource
    private PMFeignApi pmFeignApi;

    @Resource
    private OMFeignApi omFeignApi;

    @Resource
    private OutFeignApi outFeignApi;

    @Resource
    private SecurityFeignApi securityFeignApi;

    // endregion

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
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
            log.info("产品型号集合：" + productModels.size());
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
            log.info("物料集合：" + baseMaterials.size());
            // 物料页签集合
            List<BaseTabDto> baseTabDtos = baseFeignApi.findTabAll().getData();
            log.info("物料页签集合：" + baseTabDtos.size());

            for (MiddleMaterial dto : middleMaterials) {
                if (dto.getProductModelCode() != null) {
                    for (BaseProductModel item : productModels) {
                        if (item.getProductModelCode().equals(dto.getProductModelCode())) {
                            dto.setProductModelId(item.getProductModelId().toString());
                            break;
                        }
                    }
                }
                if (StringUtils.isEmpty(dto.getProductModelId())) {
                    BaseProductModel baseProductModel = new BaseProductModel();
                    baseProductModel.setProductModelName(dto.getProductModelCode());
                    baseProductModel.setProductModelCode(dto.getProductModelCode());
                    baseProductModel.setStatus(1);
                    String id = baseFeignApi.addForReturnId(baseProductModel).getData();
                    dto.setProductModelId(id);
                }
                long MaterialCount = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).count();
                if (MaterialCount <= 0) {
                    //新增物料
                    BaseMaterial material = new BaseMaterial();
                    BeanUtil.copyProperties(dto, material);
                    material.setOrganizationId(sysUser.getOrganizationId());
                    material.setStatus((byte) 1);
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
                    dto.setMaterialId(material.getMaterialId().toString());
                    baseFeignApi.update(material);

                    //修改物料页签
                    for (BaseTabDto item : baseTabDtos){
                        if (item.getMaterialId().equals(dto.getMaterialId())){
                            BaseTab tab = new BaseTab();
                            BeanUtil.copyProperties(item, tab);
                            // FG成品 SA半成品
                            if (dto.getMaterialProperty().equals("FG")) {
                                tab.setMaterialProperty((byte) 1);
                            } else {
                                tab.setMaterialProperty((byte) 0);
                            }
                            tab.setVoltage(dto.getVoltage());
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
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
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
            // 默认工艺路线
            List<BaseRoute> baseRoutes = baseFeignApi.findRouteList(new SearchBaseRoute()).getData();
            if(baseRoutes.isEmpty()){
                // 记录日志
                apiLog.setResponseData("平台默认工艺路线不存在，不同步工单数据");
                securityFeignApi.add(apiLog);
                return;
            }

            List<BaseRouteProcess> routeProcessList = baseFeignApi.findConfigureRout(baseRoutes.get(0).getRouteId()).getData();


            List<MesPmWorkOrder> addList = new ArrayList<>();
            List<MesPmWorkOrder> updateList = new ArrayList<>();
            for (MiddleOrder order : workOrders) {
                MesPmWorkOrder workOrder = new MesPmWorkOrder();
                BeanUtil.copyProperties(order, workOrder);
                workOrder.setRouteId(baseRoutes.get(0).getRouteId());
                workOrder.setPutIntoProcessId(routeProcessList.get(0).getProcessId());
                workOrder.setOutputProcessId(routeProcessList.get(routeProcessList.size()-1).getProcessId());
                // 销售订单
                if (StringUtils.isEmpty(order.getSalesOrderCode())) {
                    apiLog.setResponseData(order.getWorkOrderCode() + "此生产订单数据中销售订单号为空，不同步此条订单数据");
                    securityFeignApi.add(apiLog);
                    continue;
                }
                for (OmSalesOrderDto item : salesOrderDtos) {
                    if (item.getSalesOrderCode().equals(order.getSalesOrderCode())) {
                        workOrder.setSalesOrderId(item.getSalesOrderId());
                        break;
                    }
                }
                if (StringUtils.isEmpty(workOrder.getSalesOrderId())) {
                    // 记录日志
                    apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号在系统中没有匹配的销售订单号" + order.getSalesOrderCode() + "，不同步此条订单数据");
                    securityFeignApi.add(apiLog);
                    continue;
                }

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
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
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

        if (!salesOrders.isEmpty()) {
            // 记录日志
            long start = System.currentTimeMillis();
            // 客户集合
            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierAll().getData();
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
            // 销售订单
            List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findSalesOrderAll().getData();
            List<MiddleSaleOrder> list = new ArrayList<>();
            Map<String, List<MiddleSaleOrder>> collect = salesOrders.stream().collect(Collectors.groupingBy(MiddleSaleOrder::getSalesOrderCode));
            collect.forEach((key, value) -> {
                OmSalesOrderDto omSalesOrder =  new OmSalesOrderDto();
                List<OmSalesOrderDetDto> omSalesOrderDetDtoList = new ArrayList<>();

                for (MiddleSaleOrder saleOrder : value) {
                    BeanUtil.copyProperties(saleOrder, omSalesOrder);
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
                    omSalesOrder.setOmSalesOrderDetDtoList(omSalesOrderDetDtoList);
                    if (StringUtils.isNotEmpty(omSalesOrder.getSalesOrderId())) {
                        omFeignApi.update(omSalesOrder);
                    } else {
                        list.addAll(value);
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
            securityFeignApi.add(apiLog);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
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

        if (!deliveryOrders.isEmpty()){
            // 记录日志
            long start = System.currentTimeMillis();

            // 收货人
//            List<BaseConsignee> baseConsignees = baseFeignApi.findConsigneeAll().getData();
            // 客户
//            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierAll().getData();
            // 货主
//            List<BaseMaterialOwnerDto> ownerDtos = baseFeignApi.findMaterialOwnerAll().getData();

            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();

            // 保存平台库
            List<MiddleOutDeliveryOrder> list = new ArrayList<>();
            Map<String, List<MiddleOutDeliveryOrder>> listMap = deliveryOrders.stream().collect(Collectors.groupingBy(MiddleOutDeliveryOrder::getDeliveryOrderCode));
            listMap.forEach((key, value) ->{
                WmsOutDeliveryOrder order = new WmsOutDeliveryOrder();
                BeanUtil.copyProperties(value.get(0), order);
                order.setOrderTypeId(1L); // 销售出库
                List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = new ArrayList<>();
                for (MiddleOutDeliveryOrder item : deliveryOrders){
                    WmsOutDeliveryOrderDetDto orderDet = new WmsOutDeliveryOrderDetDto();
                    BeanUtil.copyProperties(item, orderDet);

                    // 物料
                    if (StringUtils.isEmpty(item.getMaterialCode())) {
                        apiLog.setResponseData(item.getWorkOrderCode() + "此出货通知书数据中物料编码为空，不同步此条数据");
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
                outFeignApi.add(order);
                list.addAll(value);
            });

            if (!list.isEmpty()){
                // 保存中间库
                DynamicDataSourceHolder.putDataSouce("secondary");
                for (MiddleOutDeliveryOrder item : list){
                    item.setDeliveryOrderId(UUIDUtils.getUUID());
                    middleOutDeliveryOrderMapper.save(item);
                }
                DynamicDataSourceHolder.removeDataSource();
            }

            apiLog.setRequestTime(new Date());
            apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));
            securityFeignApi.add(apiLog);
        }
    }
}
