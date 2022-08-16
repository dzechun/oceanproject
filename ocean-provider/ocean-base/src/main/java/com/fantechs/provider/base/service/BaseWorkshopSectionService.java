package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseWorkshopSectionImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2020/09/25.
 */

public interface BaseWorkshopSectionService extends IService<BaseWorkshopSection> {
    List<BaseWorkshopSection> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseWorkshopSectionImport> baseWorkshopSectionImports);

    BaseWorkshopSection addOrUpdate (BaseWorkshopSection baseWorkshopSection);
}
