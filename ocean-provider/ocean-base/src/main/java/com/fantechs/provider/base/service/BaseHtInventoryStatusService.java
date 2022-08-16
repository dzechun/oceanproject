package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */

public interface BaseHtInventoryStatusService extends IService<BaseHtInventoryStatus> {
    List<BaseHtInventoryStatus> findHtList(Map<String, Object> map);
}
