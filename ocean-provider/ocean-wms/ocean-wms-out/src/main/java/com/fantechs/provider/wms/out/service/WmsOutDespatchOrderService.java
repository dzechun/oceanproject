package com.fantechs.provider.wms.out.service;


import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */

public interface WmsOutDespatchOrderService extends IService<WmsOutDespatchOrder> {
    List<WmsOutDespatchOrderDto> findList(SearchWmsOutDespatchOrder searchWmsOutDespatchOrder);
    /**
     * 发运
     * @return
     */
    String add(WmsOutDespatchOrder wmsOutDespatchOrder);

    int forwarding(String ids);

    int finishTruckloading(String ids);

    List<WmsOutDespatchOrderReJoReDetDto> findDetList(SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet);
}
