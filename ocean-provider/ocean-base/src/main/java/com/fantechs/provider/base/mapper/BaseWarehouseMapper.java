package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseWarehouseMapper extends MyMapper<BaseWarehouse> {
    List<BaseWarehouse> findList(SearchBaseWarehouse searchBaseWarehouse);

    //更新编码批量更新仓库
    int batchUpdateByCode(List<BaseWarehouse> baseWarehouses);
}