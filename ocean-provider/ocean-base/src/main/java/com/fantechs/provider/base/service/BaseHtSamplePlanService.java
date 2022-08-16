package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplePlan;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseHtSamplePlanService extends IService<BaseHtSamplePlan> {
    List<BaseHtSamplePlan> findList(Map<String, Object> map);
}
