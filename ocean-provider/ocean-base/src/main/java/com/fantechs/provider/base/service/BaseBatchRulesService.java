package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseBatchRulesDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBatchRulesImport;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/25.
 */

public interface BaseBatchRulesService extends IService<BaseBatchRules> {
    List<BaseBatchRulesDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseBatchRulesImport> baseBatchRulesImports);
}
