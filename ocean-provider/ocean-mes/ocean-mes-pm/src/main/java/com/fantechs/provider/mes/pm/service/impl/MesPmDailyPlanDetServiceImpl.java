package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmDailyPlanDetMapper;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class MesPmDailyPlanDetServiceImpl extends BaseService<MesPmDailyPlanDet> implements MesPmDailyPlanDetService {

    @Resource
    private MesPmDailyPlanDetMapper mesPmDailyPlanDetMapper;

    @Override
    public List<MesPmDailyPlanDetDto> findList(SearchMesPmDailyPlanDet searchMesPmDailyPlanDet) {
        return mesPmDailyPlanDetMapper.findList(searchMesPmDailyPlanDet);
    }

}
