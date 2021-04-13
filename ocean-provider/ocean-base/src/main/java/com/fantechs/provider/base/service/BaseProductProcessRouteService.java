package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseProductProcessRoute;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessRouteImport;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/30.
 */

public interface BaseProductProcessRouteService extends IService<BaseProductProcessRoute> {

    List<BaseProductProcessRoute> findList(SearchBaseProductProcessRoute searchBaseProductProcessRoute);

    Map<String, Object> importExcel(List<BaseProductProcessRouteImport> baseProductProcessRouteImports);
}
