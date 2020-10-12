package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtRouteProcessService extends IService<SmtRouteProcess> {

    int configureRout(List<SmtRouteProcess> list);

    List<SmtRouteProcess> findConfigureRout(Long routeId);
}
