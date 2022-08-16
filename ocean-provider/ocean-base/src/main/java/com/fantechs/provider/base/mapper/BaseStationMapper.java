package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseStationMapper extends MyMapper<BaseStation> {
    List<BaseStation> findList(Map<String, Object> map);
}