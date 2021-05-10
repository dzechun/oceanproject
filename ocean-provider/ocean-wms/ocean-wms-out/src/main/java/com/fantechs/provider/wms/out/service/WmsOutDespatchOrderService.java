package com.fantechs.provider.wms.out.service;


import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */

public interface WmsOutDespatchOrderService extends IService<WmsOutDespatchOrder> {
    List<WmsOutDespatchOrderDto> findList(SearchWmsOutDespatchOrder searchWmsOutDespatchOrder);
}
