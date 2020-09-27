package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.HtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchWorkshopSection;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface HtWorkshopSectionService extends IService<HtWorkshopSection> {
    List<HtWorkshopSection> findHtList(SearchWorkshopSection searchWorkshopSection);
}
