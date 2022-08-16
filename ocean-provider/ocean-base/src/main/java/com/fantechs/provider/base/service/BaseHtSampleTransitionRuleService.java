package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleTransitionRule;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/20.
 */

public interface BaseHtSampleTransitionRuleService extends IService<BaseHtSampleTransitionRule> {
    List<BaseHtSampleTransitionRule> findList(Map<String, Object> map);
}
