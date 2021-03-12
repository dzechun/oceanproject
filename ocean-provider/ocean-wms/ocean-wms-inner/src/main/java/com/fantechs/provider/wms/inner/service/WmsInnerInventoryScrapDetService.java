package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/10.
 */

public interface WmsInnerInventoryScrapDetService extends IService<WmsInnerInventoryScrapDet> {
    List<WmsInnerInventoryScrapDetDto> findList(Map<String, Object> map);
}
