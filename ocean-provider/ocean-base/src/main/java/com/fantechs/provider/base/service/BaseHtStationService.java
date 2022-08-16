package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/27.
 */

public interface BaseHtStationService extends IService<BaseHtStation> {

    List<BaseHtStation> findList(Map<String, Object> map);
}
