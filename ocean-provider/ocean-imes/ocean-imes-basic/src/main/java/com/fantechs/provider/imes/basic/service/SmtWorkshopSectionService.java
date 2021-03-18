package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.imports.SmtWorkshopSectionImport;
import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2020/09/25.
 */

public interface SmtWorkshopSectionService extends IService<SmtWorkshopSection> {
    List<SmtWorkshopSection> findList(SearchSmtWorkshopSection searchSmtWorkshopSection);
    Map<String, Object> importExcel(List<SmtWorkshopSectionImport> smtWorkshopSectionImports);
}
