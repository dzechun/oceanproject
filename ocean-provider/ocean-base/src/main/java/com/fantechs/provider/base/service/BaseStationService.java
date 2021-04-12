package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/27.
 */

public interface BaseStationService extends IService<BaseStation> {

    List<BaseStation> findList(SearchBaseStation searchBaseStation);
    Map<String, Object> importExcel(List<BaseStation> baseStations);
}
