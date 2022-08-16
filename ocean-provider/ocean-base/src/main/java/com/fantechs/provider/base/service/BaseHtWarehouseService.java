package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;

import java.util.List;
import java.util.Map;

public interface BaseHtWarehouseService {
    List<BaseHtWarehouse> findHtList(Map<String, Object> map);
}
