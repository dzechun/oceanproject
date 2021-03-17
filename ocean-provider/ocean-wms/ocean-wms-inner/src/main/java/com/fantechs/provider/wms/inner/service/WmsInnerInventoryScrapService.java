package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtInventoryScrap;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/10.
 */

public interface WmsInnerInventoryScrapService extends IService<WmsInnerInventoryScrap> {
    List<WmsInnerInventoryScrapDto> findList(Map<String, Object> map);

    List<WmsInnerHtInventoryScrap> findHtList(Map<String, Object> dynamicConditionByEntity);

    int PDASubmit(WmsInnerInventoryScrap wmsInnerInventoryScrap);
}
