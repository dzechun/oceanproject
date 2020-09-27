package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.HtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface HtWorkshopSectionMapper extends MyMapper<HtWorkshopSection> {
    List<HtWorkshopSection> selectHtSection(SearchWorkshopSection searchWorkshopSection);
}