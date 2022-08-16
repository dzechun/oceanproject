package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseHtRouteService extends IService<BaseHtRoute> {

    List<BaseHtRoute> findList(Map<String, Object> map);
}
