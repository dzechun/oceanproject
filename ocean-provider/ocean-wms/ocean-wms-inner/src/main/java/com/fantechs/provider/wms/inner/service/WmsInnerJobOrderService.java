package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */

public interface WmsInnerJobOrderService extends IService<WmsInnerJobOrder> {
    List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder);

    /**
     * 自动分配
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

    /**
     * 取消分配
     * @return
     */
    int cancelDistribution(String ids);

    /**
     * 整单确认
     * @param ids
     * @return
     */
    int allReceiving(String ids);

    /**
     * 单一确认
     * @return
     */
    int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets);

    Map<String,Object> checkBarcode(String barCode,Long jobOrderDetId);

    WmsInnerJobOrderDet scanStorageBackQty(String storageCode,Long jobOrderDetId,BigDecimal qty,String barcode);

    WmsInnerJobOrder packageAutoAdd(WmsInnerJobOrder wmsInnerJobOrder);

    /**
     * PDA激活关闭栈板
     * @param jobOrderId
     * @return
     */
    int activation(Long jobOrderId);

    /**
     * 移位单查询
     * @param map
     * @return
     */
    List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map);

    /**
     * 移位单批量删除
     * @param ids
     * @return
     */
    int batchDeleteByShiftWork(String ids);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addList(List<WmsInnerJobOrder> list);


    /**
     * 库容入库规则判断入库数量
     * @param materialId
     * @param storageId
     * @param qty
     * @return
     */
    Boolean storageCapacity(Long materialId,Long storageId,BigDecimal qty);

    /**
     * 重新处理质检移位单
     * @param
     * @return
     */
    int reCreateInnerJobShift(Long jobOrderId,BigDecimal qty);
}
