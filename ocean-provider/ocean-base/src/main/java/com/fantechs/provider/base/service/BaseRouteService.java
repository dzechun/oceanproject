package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.dto.basic.imports.BaseRouteImport;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseRouteService extends IService<BaseRoute> {

    List<BaseRoute> findList(Map<String, Object> map);

    //根据工艺路线是否被修改进行操作（有修改则做新增操作，无修改则不操作）
    int addOrUpdateRoute(BaseRoute baseRoute);

    int configureRout(BaseRoute baseRoute);

    Map<String, Object> importExcel(List<BaseRouteImport> baseRouteImports) throws ParseException;

    BaseRoute addOrUpdate (BaseRoute baseRoute);
}
