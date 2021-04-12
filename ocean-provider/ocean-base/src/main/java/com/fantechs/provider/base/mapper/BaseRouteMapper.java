package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseRouteMapper extends MyMapper<BaseRoute> {
    List<BaseRoute> findList(SearchBaseRoute searchBaseRoute);
}