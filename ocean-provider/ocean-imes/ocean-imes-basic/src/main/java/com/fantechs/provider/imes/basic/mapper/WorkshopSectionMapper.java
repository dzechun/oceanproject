package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.WorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface WorkshopSectionMapper extends MyMapper<WorkshopSection> {
    List<WorkshopSection> findList(SearchWorkshopSection searchWorkshopSection);
}