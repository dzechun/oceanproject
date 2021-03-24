package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktakingDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/22.
 */

public interface WmsInnerStocktakingDetService extends IService<WmsInnerStocktakingDet> {
    List<WmsInnerStocktakingDetDto> findList(Map<String, Object> map);
}
