package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtWorkshopSectionMapper extends MyMapper<BaseHtWorkshopSection> {
    List<BaseHtWorkshopSection> findList(Map<String, Object> map);
}