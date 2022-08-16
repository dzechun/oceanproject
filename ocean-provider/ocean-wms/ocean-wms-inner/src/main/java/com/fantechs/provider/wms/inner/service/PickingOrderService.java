package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 拣货单
 * @Author mr.lei
 * @Date 2021/5/10
 */
public interface PickingOrderService {
    /**
     * 拣货单PDA确认
     * @return
     */
    WmsInnerJobOrderDet scanAffirmQty(String barCode,String storageCode,BigDecimal qty, Long jobOrderDetId);

    /**
     * 拣货单自动分配
     * @param ids
     * @return
     */
    int autoDistribution(String ids);

    /**
     * 手动分配
     * @param list
     * @return
     */
    int handDistribution(List<WmsInnerJobOrderDet> list);

    int cancelDistribution(String ids);

    /**
     * 整单确认
     * @param ids
     * @return
     */
    int allReceiving(String ids);


    /**
     * 单一确认
     * @param wmsInPutawayOrderDets
     * @return
     */
    int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets);

    List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder);

    int retrographyStatus(WmsInnerJobOrderDet wmsInnerJobOrderDet);

    /**
     * 快速发运
     * @param outDeliveryOrderId
     * @return
     */
    int autoOutOrder(Long outDeliveryOrderId,Byte orderTypeId);

    Map<String ,Object> checkBarcode(String barCode, Long jobOrderDetId);

    int sealOrder(List<Long> outDeliveryOrderIds,Byte type);

    /**
     * 万宝条码校验
     * @param barCode
     * @param jobOrderDetId
     * @return
     */
    BigDecimal chechkBarcodeToWanbao(String barCode,Long jobOrderDetId);
}
