package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.WorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchWorkshopSection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2020/09/25.
 */

public interface WorkshopSectionService extends IService<WorkshopSection> {
    List<WorkshopSection> findList(SearchWorkshopSection searchWorkshopSection);

}
