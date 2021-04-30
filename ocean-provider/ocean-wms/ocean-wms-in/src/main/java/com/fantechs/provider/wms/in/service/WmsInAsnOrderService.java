package com.fantechs.provider.wms.in.service;


import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/04/29.
 */

public interface WmsInAsnOrderService extends IService<WmsInAsnOrder> {
    List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder);

    /**
     * 整单收货
     * @param ids
     * @param inventoryStatusId
     * @return
     */
    int allReceiving(String ids,Long inventoryStatusId);

    /**
     * 单一收货
     * @param wmsInAsnOrderDet
     * @return
     */
    int singleReceiving(WmsInAsnOrderDet wmsInAsnOrderDet);
}
