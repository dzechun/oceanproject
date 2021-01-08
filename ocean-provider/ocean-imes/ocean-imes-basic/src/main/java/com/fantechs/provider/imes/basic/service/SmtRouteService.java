package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtRouteService extends IService<SmtRoute> {

    List<SmtRoute> findList(SearchSmtRoute searchSmtRoute);

    //根据工艺路线是否被修改进行操作（有修改则做新增操作，无修改则不操作）
    int addOrUpdateRoute(SmtRoute smtRoute);
}
