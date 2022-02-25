package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */

public interface WmsInnerShiftWorkDetService extends IService<WmsInnerJobOrderDet> {
    List<WmsInnerJobOrderDetDto> findList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet);

    int batchUpdate(List<WmsInnerJobOrderDet> list);

    int pickDisQty(List<WmsInnerJobOrderDet> wmsInnerJobOrderDets);

    /**
     * 移位单明细批量删除
     * @param ids
     * @return
     */
    int batchDeleteByShiftWork(String ids);
}
