package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplingPlan;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseHtSamplingPlanService extends IService<BaseHtSamplingPlan> {
    List<BaseHtSamplingPlan> findList(Map<String, Object> map);
}
