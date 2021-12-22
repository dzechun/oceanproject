package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by leifengzhi on 2021/12/21.
 */

public interface MesPmDailyPlanDetService extends IService<MesPmDailyPlanDet> {
    List<MesPmDailyPlanDetDto> findList(SearchMesPmDailyPlanDet searchMesPmDailyPlanDet);

    //Map<String, Object> importExcel(List<MesPmDailyPlanDet> list);
}
