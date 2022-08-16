package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSamplePlanImport;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlan;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseSamplePlanService extends IService<BaseSamplePlan> {
    List<BaseSamplePlanDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseSamplePlanImport> baseSamplePlanImports);
}
