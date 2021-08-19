package com.fantechs.provider.wanbao.api.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wanbao.api.config.DynamicDataSourceHolder;
import com.fantechs.provider.wanbao.api.mapper.SyncDataMapper;
import com.fantechs.provider.wanbao.api.service.SyncDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SyncDataServiceImpl implements SyncDataService {

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

    // 同步十分钟前至今的数据
    private static int OFFSET = -10;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncMaterialData() {
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.offset(new Date(), DateField.MINUTE, OFFSET);
        map.put("date", date);
        List<BaseMaterial> baseMaterials = syncDataMapper.findMaterialData(map);

        if (!baseMaterials.isEmpty()){
            // 保存平台库
            baseFeignApi.addList(baseMaterials);
            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            syncDataMapper.batchSaveMaterialMiddle(baseMaterials);
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
        List<MesPmWorkOrder> workOrders = syncDataMapper.findOrderData(map);

        if (!workOrders.isEmpty()){
            // 保存平台库
            pmFeignApi.addList(workOrders);
            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            syncDataMapper.batchSaveOrderData(workOrders);
            DynamicDataSourceHolder.removeDataSource();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncSaleOrderData() {
        // 执行查询前调用函数执行存储过程
        syncDataMapper.setPolicy();
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.offset(new Date(), DateField.MINUTE, OFFSET);
        map.put("date", date);
        List<OmSalesOrder> salesOrders = syncDataMapper.findSaleOrderData(map);

        if (!salesOrders.isEmpty()){
            // 保存平台库
            omFeignApi.addList(salesOrders);
            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            syncDataMapper.batchSaveSaleOrderData(salesOrders);
            DynamicDataSourceHolder.removeDataSource();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void syncOutDeliveryData() {
        // 执行查询前调用函数执行存储过程
        syncDataMapper.setPolicy();
        // 获取十分钟前时间
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.offset(new Date(), DateField.MINUTE, OFFSET);
        map.put("date", date);
        List<WmsOutDeliveryOrder> deliveryOrders = syncDataMapper.findOutDeliveryData(map);

        if (!deliveryOrders.isEmpty()){
            // 保存平台库
            outFeignApi.addList(deliveryOrders);
            // 保存中间库
            DynamicDataSourceHolder.putDataSouce("secondary");
            syncDataMapper.batchSaveOutDeliveryData(deliveryOrders);
            DynamicDataSourceHolder.removeDataSource();
        }
    }
}
