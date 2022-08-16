package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtWarehouseAreaMapper extends MyMapper<BaseHtWarehouseArea> {
    List<BaseHtWarehouseArea> findHtList (Map<String,Object> map);
}