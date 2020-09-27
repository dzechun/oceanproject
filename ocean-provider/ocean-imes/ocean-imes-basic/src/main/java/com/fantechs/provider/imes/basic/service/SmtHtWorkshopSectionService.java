package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SmtHtWorkshopSectionService extends IService<SmtHtWorkshopSection> {
    List<SmtHtWorkshopSection> findList(SearchSmtWorkshopSection searchSmtWorkshopSection);
}
