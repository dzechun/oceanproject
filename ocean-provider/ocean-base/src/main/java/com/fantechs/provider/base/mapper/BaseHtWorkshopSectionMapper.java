package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtWorkshopSectionMapper extends MyMapper<BaseHtWorkshopSection> {
    List<BaseHtWorkshopSection> findList(SearchBaseWorkshopSection searchBaseWorkshopSection);
}