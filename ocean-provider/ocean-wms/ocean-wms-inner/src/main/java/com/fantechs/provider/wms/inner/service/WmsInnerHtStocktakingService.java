package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktaking;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/22.
 */

public interface WmsInnerHtStocktakingService extends IService<WmsInnerHtStocktaking> {
    List<WmsInnerHtStocktaking> findHtList(Map<String, Object> map);
}
