package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface BaseHtWorkshopSectionService extends IService<BaseHtWorkshopSection> {
    List<BaseHtWorkshopSection> findList(Map<String, Object> map);
}
