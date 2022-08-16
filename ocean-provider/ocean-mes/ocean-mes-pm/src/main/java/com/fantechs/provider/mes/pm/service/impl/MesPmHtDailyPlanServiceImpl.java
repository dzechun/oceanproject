package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.MesPmHtDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmHtDailyPlanMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtDailyPlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/03.
 */
@Service
public class MesPmHtDailyPlanServiceImpl extends BaseService<MesPmHtDailyPlan> implements MesPmHtDailyPlanService {

    @Resource
    private MesPmHtDailyPlanMapper mesPmHtDailyPlanMapper;

    @Override
    public List<MesPmHtDailyPlan> findList(SearchMesPmDailyPlan searchMesPmDailyPlan) {
        return mesPmHtDailyPlanMapper.findList(searchMesPmDailyPlan);
    }
}
