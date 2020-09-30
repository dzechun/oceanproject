package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */

public interface SmtHtProductProcessRouteService extends IService<SmtHtProductProcessRoute> {

    List<SmtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute);
}
