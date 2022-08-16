package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/25.
 */

public interface BaseHtBatchRulesService extends IService<BaseHtBatchRules> {
    List<BaseHtBatchRules> findList(Map<String, Object> map);
}
