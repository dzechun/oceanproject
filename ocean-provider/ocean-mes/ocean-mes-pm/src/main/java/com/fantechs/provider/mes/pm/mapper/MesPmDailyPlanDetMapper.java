package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmDailyPlanDetMapper extends MyMapper<MesPmDailyPlanDet> {
    List<MesPmDailyPlanDetDto> findList(SearchMesPmDailyPlanDet searchMesPmDailyPlanDet);

}