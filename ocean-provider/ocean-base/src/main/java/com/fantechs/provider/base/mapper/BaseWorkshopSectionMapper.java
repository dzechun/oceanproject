package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseWorkshopSectionMapper extends MyMapper<BaseWorkshopSection> {
    List<BaseWorkshopSection> findList(Map<String, Object> map);
}