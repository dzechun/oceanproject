package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessPhenotypeImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/07.
 */

public interface BaseBadnessPhenotypeService extends IService<BaseBadnessPhenotype> {
    List<BaseBadnessPhenotypeDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseBadnessPhenotypeImport> baseBadnessPhenotypeImports);

    int saveByApi(List<BaseBadnessPhenotype> baseBadnessPhenotypes);
}
