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

    WmsInnerJobOrderDet scanStorageBackQty(String storageCode,Long jobOrderDetId,BigDecimal qty);

    int scanInStorage(Long storageId ,Long jobOrderDetId);

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

    List<WmsInnerJobOrderDto> pdaFindShiftList(Map<String, Object> map);

    /**
     * 移位单批量删除
     * @param ids
     * @return
     */
    int batchDeleteByShiftWork(String ids);
}
