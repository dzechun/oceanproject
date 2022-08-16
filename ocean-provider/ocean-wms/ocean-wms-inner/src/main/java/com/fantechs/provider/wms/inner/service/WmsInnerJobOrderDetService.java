package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */

public interface WmsInnerJobOrderDetService extends IService<WmsInnerJobOrderDet> {
    List<WmsInnerJobOrderDetDto> findList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet);

    int batchUpdate(List<WmsInnerJobOrderDet> list);

    int pickDisQty(List<WmsInnerJobOrderDet> wmsInnerJobOrderDets);
}
