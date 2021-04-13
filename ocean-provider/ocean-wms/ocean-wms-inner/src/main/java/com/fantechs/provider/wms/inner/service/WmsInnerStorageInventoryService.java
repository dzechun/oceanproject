package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/02.
 */

public interface WmsInnerStorageInventoryService extends IService<WmsInnerStorageInventory> {

    List<WmsInnerStorageInventoryDto> findList(Map<String, Object> map);

    int out(WmsInnerStorageInventory wmsInnerStorageInventory);
}
