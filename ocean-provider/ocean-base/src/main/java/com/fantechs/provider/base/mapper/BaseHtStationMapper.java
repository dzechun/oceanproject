package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtStationMapper extends MyMapper<BaseHtStation> {
    List<BaseHtStation> findList(SearchBaseStation searchBaseStation);
}