package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtRouteMapper extends MyMapper<BaseHtRoute> {
    List<BaseHtRoute> findList(SearchBaseRoute searchBaseRoute);
}