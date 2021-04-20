package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleTransitionRule;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtSampleTransitionRuleMapper;
import com.fantechs.provider.base.service.BaseHtSampleTransitionRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/20.
 */
@Service
public class BaseHtSampleTransitionRuleServiceImpl extends BaseService<BaseHtSampleTransitionRule> implements BaseHtSampleTransitionRuleService {

    @Resource
    private BaseHtSampleTransitionRuleMapper baseHtSampleTransitionRuleMapper;

    @Override
    public List<BaseHtSampleTransitionRule> findList(Map<String, Object> map) {
        return baseHtSampleTransitionRuleMapper.findList(map);
    }
}
