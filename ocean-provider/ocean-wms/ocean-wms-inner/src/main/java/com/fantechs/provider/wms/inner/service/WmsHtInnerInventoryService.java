package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsHtInnerInventory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */

public interface WmsHtInnerInventoryService extends IService<WmsHtInnerInventory> {
    List<WmsHtInnerInventory> findList(Map<String, Object> map);
}
