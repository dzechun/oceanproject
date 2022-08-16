package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.MesPmHtDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by leifengzhi on 2021/06/03.
 */

public interface MesPmHtDailyPlanService extends IService<MesPmHtDailyPlan> {
    List<MesPmHtDailyPlan> findList(SearchMesPmDailyPlan searchMesPmDailyPlan);
}
