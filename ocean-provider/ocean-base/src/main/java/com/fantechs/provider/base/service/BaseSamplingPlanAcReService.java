package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAcRe;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseSamplingPlanAcReService extends IService<BaseSamplingPlanAcRe> {
    List<BaseSamplingPlanAcReDto> findList(Map<String, Object> map);
}
