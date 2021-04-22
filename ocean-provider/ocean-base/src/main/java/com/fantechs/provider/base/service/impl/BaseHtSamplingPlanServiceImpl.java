package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplingPlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtSamplingPlanMapper;
import com.fantechs.provider.base.service.BaseHtSamplingPlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseHtSamplingPlanServiceImpl extends BaseService<BaseHtSamplingPlan> implements BaseHtSamplingPlanService {

    @Resource
    private BaseHtSamplingPlanMapper baseHtSamplingPlanMapper;

    @Override
    public List<BaseHtSamplingPlan> findList(Map<String, Object> map) {
        return baseHtSamplingPlanMapper.findList(map);
    }
}
