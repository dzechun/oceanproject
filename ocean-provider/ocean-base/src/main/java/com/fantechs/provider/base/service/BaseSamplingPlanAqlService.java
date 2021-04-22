package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAql;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseSamplingPlanAqlService extends IService<BaseSamplingPlanAql> {
    List<BaseSamplingPlanAqlDto> findList(Map<String, Object> map);
}
