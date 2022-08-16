package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/30.
 */

public interface BaseHtProductProcessRouteService extends IService<BaseHtProductProcessRoute> {

    List<BaseHtProductProcessRoute> findList(Map<String, Object> map);
}
