package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmHtDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MesPmHtDailyPlanMapper extends MyMapper<MesPmHtDailyPlan> {

    List<MesPmHtDailyPlan> findList(SearchMesPmDailyPlan searchMesPmDailyPlan);

}