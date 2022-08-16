package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtStationMapper extends MyMapper<BaseHtStation> {
    List<BaseHtStation> findList(Map<String, Object> map);
}