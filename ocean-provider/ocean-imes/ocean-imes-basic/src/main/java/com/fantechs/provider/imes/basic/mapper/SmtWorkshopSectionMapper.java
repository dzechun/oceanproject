package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtWorkshopSectionMapper extends MyMapper<SmtWorkshopSection> {
    List<SmtWorkshopSection> findList(SearchSmtWorkshopSection searchSmtWorkshopSection);
}