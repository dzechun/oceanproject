package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */

public interface SmtProductProcessRouteService extends IService<SmtProductProcessRoute> {

    List<SmtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute);
}
