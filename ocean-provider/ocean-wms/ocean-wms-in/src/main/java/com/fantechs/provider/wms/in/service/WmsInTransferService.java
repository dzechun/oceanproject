package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;

import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/6/17
 */
public interface WmsInTransferService {
    List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder);

    int save(WmsInAsnOrder wmsInAsnOrder);
}
