package com.fantechs.provider.wanbao.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wanbao.api.config.DynamicDataSourceHolder;
import com.fantechs.provider.wanbao.api.entity.MiddleMaterial;
import com.fantechs.provider.wanbao.api.entity.MiddleOrder;
import com.fantechs.provider.wanbao.api.mapper.MiddleMaterialMapper;
import com.fantechs.provider.wanbao.api.mapper.MiddleOrderMapper;
import com.fantechs.provider.wanbao.api.mapper.MiddleSaleOrderMapper;
import com.fantechs.provider.wanbao.api.service.SyncDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

    // 同步当天数据
    private static int OFFSET = -10;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncMaterialData() {
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        // 同步数据
        List<MiddleMaterial> middleMaterials = middleMaterialMapper.findMaterialData(map);
        log.info("获取万宝物料数据，当前获取数量：" + middleMaterials.size());

        if (!middleMaterials.isEmpty()){
            // 记录日志

            
            // 1、保存平台库
            // 产品型号集合
            List<BaseProductModel> productModels = baseFeignApi.findList(new SearchBaseProductModel()).getData();
            log.info("产品型号集合：" + productModels.size());
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(new SearchBaseMaterial()).getData();
            log.info("物料集合：" + productModels.size());
            // 物料页签集合
            List<BaseTabDto> baseTabDtos = baseFeignApi.findTabList(new SearchBaseTab()).getData();
            log.info("物料页签集合：" + productModels.size());

            for (MiddleMaterial dto : middleMaterials){
                long productModelCount = productModels.stream().filter(item -> item.getProductModelCode().equals(dto.getProductModelCode())).count();
                if (productModelCount <= 0){
                    // 新增产品型号
                    BaseProductModel productModel = new BaseProductModel();
                    productModel.setProductModelCode(dto.getProductModelCode());
                    productModel.setProductModelName(dto.getProductModelCode());
                    productModel = baseFeignApi.addForReturn(productModel).getData();
                    dto.setProductModelId(productModel.getProductModelId().toString());
                    productModels.add(productModel);
                }else {
                    BaseProductModel productModel = productModels.stream().filter(item -> item.getProductModelCode().equals(dto.getProductModelCode())).findFirst().get();
                    dto.setProductModelId(productModel.getProductModelId().toString());
                }
                long MaterialCount = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).count();
                if (MaterialCount <= 0){
                    //新增物料
                    BaseMaterial material = new BaseMaterial();
                    BeanUtil.copyProperties(dto, material);
                    BaseMaterial baseMaterial = baseFeignApi.saveByApi(material).getData();
                    dto.setMaterialId(baseMaterial.getMaterialId().toString());

                    //新增物料页签
                    BaseTab tab = new BaseTab();
                    BeanUtil.copyProperties(dto, tab);
                    // FG成品 SA半成品
                    if (dto.getMaterialProperty().equals("FG")){
                        tab.setMaterialProperty((byte) 1);
                    }else {
                        tab.setMaterialProperty((byte) 0);
                    }
                    baseFeignApi.addTab(tab);
                }else {
                    // 修改物料
                    BaseMaterial material = baseMaterials.stream().filter(item -> item.getMaterialCode().equals(dto.getMaterialCode())).findFirst().get();
                    material.setMaterialName(dto.getMaterialName());
                    material.setMaterialDesc(dto.getMaterialDesc());
                    dto.setMaterialId(material.getMaterialId().toString());
                    baseFeignApi.update(material);

                    //修改物料页签
                    BaseTabDto tabDto = baseTabDtos.stream().filter(item -> item.getMaterialId().equals(dto.getMaterialId())).findFirst().get();
                    BaseTab tab = new BaseTab();
                    BeanUtil.copyProperties(tabDto, tab);
                    // FG成品 SA半成品
                    if (dto.getMaterialProperty().equals("FG")){
                        tab.setMaterialProperty((byte) 1);
                    }else {
                        tab.setMaterialProperty((byte) 0);
                    }
                    tab.setVoltage(dto.getVoltage());
                    baseFeignApi.updateTab(tab);
                }
            }
            // 2、保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            middleMaterialMapper.insertList(middleMaterials);
            DynamicDataSourceHolder.removeDataSource();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncOrderData() {
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.offset(new Date(), DateField.MINUTE, OFFSET);
        map.put("date", date);
        List<MiddleOrder> workOrders = middleOrderMapper.findOrderData(map);

        if (!workOrders.isEmpty()){
            // 记录日志

            // 获取所有销售订单
            List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findList(new SearchOmSalesOrderDto()).getData();
            // 物料集合
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(new SearchBaseMaterial()).getData();
            // 工单集合
            List<MesPmWorkOrderDto> workOrderDtos = pmFeignApi.findWorkOrderList(new SearchMesPmWorkOrder()).getData();
            // 产线集合
            List<BaseProLine> proLines = baseFeignApi.selectProLines(new SearchBaseProLine()).getData();

            List<MesPmWorkOrder> addList = new ArrayList<>();
            List<MesPmWorkOrder> updateList = new ArrayList<>();
            for (MiddleOrder order : workOrders){
                if (StringUtils.isNotEmpty(order.getSalesOrderCode()) && StringUtils.isNotEmpty(order.getMaterialCode())
                        && StringUtils.isNotEmpty(order.getWorkOrderCode()) && StringUtils.isNotEmpty(order.getProName())){
                    MesPmWorkOrder workOrder = new MesPmWorkOrder();
                    BeanUtil.copyProperties(order, workOrder);

                    // 销售订单
                    for (OmSalesOrderDto item : salesOrderDtos){
                        if(item.getSalesOrderCode().equals(order.getSalesOrderCode())){
                            workOrder.setSalesOrderId(item.getSalesOrderId());
                            break;
                        }
                    }
                    if(StringUtils.isEmpty(workOrder.getSalesOrderId())){
                        // 记录日志
                        SysApiLog apiLog = new SysApiLog();
                        apiLog.setCreateTime(new Date());
                        apiLog.setThirdpartySysName("万宝数据库对接");
                        apiLog.setCallType((byte) 1);
                        apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号没有销售订单号，不同步此条订单数据");
                        securityFeignApi.add(apiLog);
                        break;
                    }

                    // 物料
                    for (BaseMaterial item : baseMaterials){
                        if(item.getMaterialCode().equals(order.getMaterialCode())){
                            workOrder.setMaterialId(item.getMaterialId());
                            break;
                        }
                    }

                    if(StringUtils.isEmpty(workOrder.getMaterialId())){
                        // 记录日志
                        SysApiLog apiLog = new SysApiLog();
                        apiLog.setCreateTime(new Date());
                        apiLog.setThirdpartySysName("万宝数据库对接");
                        apiLog.setCallType((byte) 1);
                        apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号没有物料，不同步此条订单数据");
                        securityFeignApi.add(apiLog);
                        break;
                    }

                    // 产线
                    for (BaseProLine item : proLines){
                        if(item.getProName().equals(order.getProName())){
                            workOrder.setProLineId(item.getProLineId());
                            break;
                        }
                    }

                    if(StringUtils.isEmpty(workOrder.getProLineId())){
                        // 记录日志
                        SysApiLog apiLog = new SysApiLog();
                        apiLog.setCreateTime(new Date());
                        apiLog.setThirdpartySysName("万宝数据库对接");
                        apiLog.setCallType((byte) 1);
                        apiLog.setResponseData(order.getWorkOrderCode() + "此订单编号没有禅心，不同步此条订单数据");
                        securityFeignApi.add(apiLog);
                        break;
                    }

                    // 判断订单是否存在
                    Long workOrderId = null;
                    for (MesPmWorkOrderDto item : workOrderDtos){
                        if(item.getWorkOrderCode().equals(order.getWorkOrderCode())){
                            workOrderId = item.getWorkOrderId();
                            break;
                        }
                    }
                    if(workOrderId != null){
                        // 修改订单
                        updateList.add(workOrder);
                    }else {
                        // 新增订单
                        addList.add(workOrder);
                    }
                }
            }
            // 保存平台库
            pmFeignApi.addList(addList);
            pmFeignApi.batchUpdate(updateList);

            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            middleOrderMapper.insertList(workOrders);
            DynamicDataSourceHolder.removeDataSource();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncSaleOrderData() {
        /*// 执行查询前调用函数执行存储过程
        middleSaleOrderMapper.setPolicy();
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.offset(new Date(), DateField.MINUTE, OFFSET);
        map.put("date", date);
        List<MiddleSaleOrder> salesOrders = middleSaleOrderMapper.findSaleOrderData(map);

        if (!salesOrders.isEmpty()){
            // 客户集合
            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(new SearchBaseSupplier()).getData();

            for (MiddleSaleOrder saleOrder : salesOrders){

                for (BaseSupplier supplier : baseSuppliers){
                    if (saleOrder)
                }
            }

            // 保存平台库
            omFeignApi.addList(salesOrders);
            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            middleSaleOrderMapper.insertList(salesOrders);
            DynamicDataSourceHolder.removeDataSource();
        }*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncOutDeliveryData() {
        /*// 执行查询前调用函数执行存储过程
        middleSaleOrderMapper.setPolicy();
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.offset(new Date(), DateField.MINUTE, OFFSET);
        map.put("date", date);
        List<WmsOutDeliveryOrder> deliveryOrders = middleMaterialMapper.findOutDeliveryData(map);

        if (!deliveryOrders.isEmpty()){
            // 保存平台库
            outFeignApi.addList(deliveryOrders);
            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            middleMaterialMapper.batchSaveOutDeliveryData(deliveryOrders);
            DynamicDataSourceHolder.removeDataSource();
        }*/
    }
}
