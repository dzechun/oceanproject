package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplePlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtSamplePlanMapper;
import com.fantechs.provider.base.service.BaseHtSamplePlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseHtSamplePlanServiceImpl extends BaseService<BaseHtSamplePlan> implements BaseHtSamplePlanService {

    @Resource
    private BaseHtSamplePlanMapper baseHtSamplePlanMapper;

    @Override
    public List<BaseHtSamplePlan> findList(Map<String, Object> map) {
        return baseHtSamplePlanMapper.findList(map);
    }
}
