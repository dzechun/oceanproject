package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtWarehouseMapper extends MyMapper<BaseHtWarehouse> {
    List<BaseHtWarehouse> findHtList(SearchBaseWarehouse searchBaseWarehouse);
}
