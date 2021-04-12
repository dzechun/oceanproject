package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseProductProcessRouteMapper extends MyMapper<BaseProductProcessRoute> {

    List<BaseProductProcessRoute> findList(SearchBaseProductProcessRoute searchBaseProductProcessRoute);
}