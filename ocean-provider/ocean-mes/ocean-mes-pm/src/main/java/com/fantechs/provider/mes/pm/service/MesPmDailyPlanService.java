package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */

public interface MesPmDailyPlanService extends IService<MesPmDailyPlan> {

    List<MesPmDailyPlanDto> findList(SearchMesPmDailyPlan searchMesPmDailyPlan);

    List<MesPmDailyPlanDto> findDaysList(SearchMesPmDailyPlan searchMesPmDailyPlan) throws ParseException;

    int batchRemove(List<MesPmDailyPlan> list);

}
