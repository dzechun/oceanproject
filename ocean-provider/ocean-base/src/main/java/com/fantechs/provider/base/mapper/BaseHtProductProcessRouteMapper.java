package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtProductProcessRouteMapper extends MyMapper<BaseHtProductProcessRoute> {

    List<BaseHtProductProcessRoute> findList(Map<String, Object> map);
}