package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseBatchRulesDto;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseBatchRulesMapper;
import com.fantechs.provider.base.mapper.BaseHtBatchRulesMapper;
import com.fantechs.provider.base.service.BaseBatchRulesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseBatchRulesServiceImpl extends BaseService<BaseBatchRules> implements BaseBatchRulesService {

    @Resource
    private BaseBatchRulesMapper baseBatchRulesMapper;

    @Override
    public List<BaseBatchRulesDto> findList(SearchBaseBatchRules searchBaseBatchRules) {
        return baseBatchRulesMapper.findList(searchBaseBatchRules);
    }
}
