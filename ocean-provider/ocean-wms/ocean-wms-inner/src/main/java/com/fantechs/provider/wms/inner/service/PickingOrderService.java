package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;

import java.util.List;

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
    int scanAffirmQty(Long jobOrderDetId,String barCode);

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
}
