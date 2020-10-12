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
}
