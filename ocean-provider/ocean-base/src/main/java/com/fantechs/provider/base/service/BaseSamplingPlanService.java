package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlan;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseSamplingPlanService extends IService<BaseSamplingPlan> {
    List<BaseSamplingPlanDto> findList(Map<String, Object> map);
}
