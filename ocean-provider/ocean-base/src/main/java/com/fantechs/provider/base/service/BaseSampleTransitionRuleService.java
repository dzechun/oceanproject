package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSampleTransitionRuleImport;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRule;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/20.
 */

public interface BaseSampleTransitionRuleService extends IService<BaseSampleTransitionRule> {
    List<BaseSampleTransitionRuleDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseSampleTransitionRuleImport> baseSampleTransitionRuleImports);
}
