package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAcRe;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseSamplePlanAcReService extends IService<BaseSamplePlanAcRe> {
    List<BaseSamplePlanAcReDto> findList(Map<String, Object> map);
}
