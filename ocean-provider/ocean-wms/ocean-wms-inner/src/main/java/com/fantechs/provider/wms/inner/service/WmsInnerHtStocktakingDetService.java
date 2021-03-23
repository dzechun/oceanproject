package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktakingDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/22.
 */

public interface WmsInnerHtStocktakingDetService extends IService<WmsInnerHtStocktakingDet> {
    List<WmsInnerHtStocktakingDet> findHtList(Map<String, Object> map);
}
