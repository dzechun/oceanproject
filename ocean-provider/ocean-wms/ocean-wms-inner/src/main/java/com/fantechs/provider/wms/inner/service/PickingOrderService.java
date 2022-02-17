package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;

import java.math.BigDecimal;
import java.text.ParseException;
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
    int handDistribution(List<WmsInnerJobOrderDetDto> list);

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

    int closeDocuments(String id);

    WmsInnerInventoryDetDto scan(Long storageId, Long materialId, String barcode,Integer ifPda);

    int pdaSubmit(WmsInnerPdaJobOrderDet wmsInnerPdaJobOrderDet);

    List<WmsInnerJobOrderDetDto> pdaSave(List<WmsInnerPdaInventoryDetDto> list);

    Map<String, Object> importExcel(List<WmsInnerJobOrderImport> wmsInnerJobOrderImportList) throws ParseException;
}
