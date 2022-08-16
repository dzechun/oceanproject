package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseBadnessCauseDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessCauseImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface BaseBadnessCauseService extends IService<BaseBadnessCause> {
    List<BaseBadnessCauseDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseBadnessCauseImport> baseBadnessCauseImports);

    int saveByApi (BaseBadnessCause baseBadnessCauses);
}
