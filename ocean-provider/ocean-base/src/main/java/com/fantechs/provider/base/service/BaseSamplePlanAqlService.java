package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAql;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface BaseSamplePlanAqlService extends IService<BaseSamplePlanAql> {
    List<BaseSamplePlanAqlDto> findList(Map<String, Object> map);

    int batchUpdate(List<BaseSamplePlanAql> list);

}
