package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtBatchRulesMapper;
import com.fantechs.provider.base.service.BaseHtBatchRulesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseHtBatchRulesServiceImpl extends BaseService<BaseHtBatchRules> implements BaseHtBatchRulesService {

    @Resource
    private BaseHtBatchRulesMapper baseHtBatchRulesMapper;

    @Override
    public List<BaseHtBatchRules> findList(SearchBaseBatchRules searchBaseBatchRules) {
        return baseHtBatchRulesMapper.findHtList(searchBaseBatchRules);
    }
}
