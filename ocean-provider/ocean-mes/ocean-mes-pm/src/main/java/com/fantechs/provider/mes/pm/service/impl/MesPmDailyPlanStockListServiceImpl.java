package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanStockListDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanStockList;
import com.fantechs.common.base.support.BaseService;

import com.fantechs.provider.mes.pm.mapper.MesPmDailyPlanStockListMapper;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanStockListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class MesPmDailyPlanStockListServiceImpl extends BaseService<MesPmDailyPlanStockList> implements MesPmDailyPlanStockListService {

    @Resource
    private MesPmDailyPlanStockListMapper mesPmDailyPlanStockListMapper;

    @Override
    public List<MesPmDailyPlanStockListDto> findList(SearchMesPmDailyPlanStockList searchMesPmDailyPlanStockList) {
        return mesPmDailyPlanStockListMapper.findList(searchMesPmDailyPlanStockList);
    }

}
