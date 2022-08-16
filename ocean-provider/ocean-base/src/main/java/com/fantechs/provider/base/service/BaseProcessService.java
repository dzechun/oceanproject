package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseProcessImport;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/25.
 */

public interface BaseProcessService extends IService<BaseProcess> {

    List<BaseProcess> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseProcessImport> baseProcessImports);

    BaseProcess addOrUpdate (BaseProcess baseProcess);
}
