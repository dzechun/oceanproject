package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseRouteProcessService extends IService<BaseRouteProcess> {

    int configureRout(List<BaseRouteProcess> list);

    List<BaseRouteProcess> findConfigureRout(Long routeId);

    List<BaseRouteProcess> findList(Map<String, Object> map);


    int configureProcess(Map<String, Object> map);
}
