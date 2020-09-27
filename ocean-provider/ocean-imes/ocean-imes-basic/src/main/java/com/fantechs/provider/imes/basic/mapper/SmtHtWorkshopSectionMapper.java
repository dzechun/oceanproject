package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtWorkshopSectionMapper extends MyMapper<SmtHtWorkshopSection> {
    List<SmtHtWorkshopSection> findList(SearchSmtWorkshopSection searchSmtWorkshopSection);
}