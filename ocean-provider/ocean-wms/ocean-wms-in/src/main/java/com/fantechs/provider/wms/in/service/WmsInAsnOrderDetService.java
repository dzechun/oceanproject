package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.entity.wms.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/04/29.
 */

public interface WmsInAsnOrderDetService extends IService<WmsInAsnOrderDet> {
    List<WmsInAsnOrderDetDto> findList(SearchWmsInAsnOrderDet searchWmsInAsnOrderDet);
}
