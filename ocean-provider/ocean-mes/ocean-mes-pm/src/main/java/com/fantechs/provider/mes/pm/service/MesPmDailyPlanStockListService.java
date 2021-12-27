package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanStockListDto;

import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanStockList;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/21.
 */

public interface MesPmDailyPlanStockListService extends IService<MesPmDailyPlanStockList> {
    List<MesPmDailyPlanStockListDto> findList(SearchMesPmDailyPlanStockList searchMesPmDailyPlanStockList);

    //Map<String, Object> importExcel(List<MesPmDailyPlanStockList> list);
}
