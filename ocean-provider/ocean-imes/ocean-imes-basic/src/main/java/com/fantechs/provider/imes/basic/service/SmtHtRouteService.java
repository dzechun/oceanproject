package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtHtRouteService extends IService<SmtHtRoute> {

    List<SmtHtRoute> findList(SearchSmtRoute searchSmtRoute);
}
