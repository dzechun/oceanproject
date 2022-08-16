package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRuleDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/20.
 */

public interface BaseSampleTransitionRuleDetService extends IService<BaseSampleTransitionRuleDet> {
    List<BaseSampleTransitionRuleDetDto> findList(Map<String, Object> map);
}
