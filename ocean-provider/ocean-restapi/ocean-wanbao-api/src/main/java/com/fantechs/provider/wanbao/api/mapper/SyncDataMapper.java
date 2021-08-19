package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SyncDataMapper {

    void setPolicy();

    /**
     * 万宝-物料基础信息查询
     * @return
     */
    List<BaseMaterial> findMaterialData(Map<String, Object> map);

    /**
     * 万宝-工单信息查询
     * @return
     */
    List<MesPmWorkOrder> findOrderData(Map<String, Object> map);

    /**
     * 万宝-销售订单信息查询
     * @return
     */
    List<OmSalesOrder> findSaleOrderData(Map<String, Object> map);

    /**
     * 万宝-出货通知单信息查询
     * @return
     */
    List<WmsOutDeliveryOrder> findOutDeliveryData(Map<String, Object> map);

    /**
     * 万宝-批量插入物料
     * @param baseMaterials
     * @return
     */
    int batchSaveMaterialMiddle(List<BaseMaterial> baseMaterials);

    /**
     * 万宝-批量插入工单
     * @param workOrders
     * @return
     */
    int batchSaveOrderData(List<MesPmWorkOrder> workOrders);

    /**
     * 万宝-批量插入销售订单
     * @param salesOrders
     * @return
     */
    int batchSaveSaleOrderData(List<OmSalesOrder> salesOrders);

    /**
     * 万宝-批量插入出货通知单
     * @param outDeliveryOrders
     * @return
     */
    int batchSaveOutDeliveryData(List<WmsOutDeliveryOrder> outDeliveryOrders);
}