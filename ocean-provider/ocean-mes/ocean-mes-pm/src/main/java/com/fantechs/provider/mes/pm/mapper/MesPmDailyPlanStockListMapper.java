package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanStockListDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanStockList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MesPmDailyPlanStockListMapper extends MyMapper<MesPmDailyPlanStockList> {
    List<MesPmDailyPlanStockListDto> findList(SearchMesPmDailyPlanStockList searchMesPmDailyPlanStockList);
}