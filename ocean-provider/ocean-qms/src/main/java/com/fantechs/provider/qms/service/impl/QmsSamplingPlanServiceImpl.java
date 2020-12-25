package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsSamplingPlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsSamplingPlanMapper;
import com.fantechs.provider.qms.service.QmsSamplingPlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@Service
public class QmsSamplingPlanServiceImpl extends BaseService<QmsSamplingPlan> implements QmsSamplingPlanService {

    @Resource
    private QmsSamplingPlanMapper qmsSamplingPlanMapper;


    @Override
    public List<QmsSamplingPlan> findList(Map<String, Object> map) {
        return qmsSamplingPlanMapper.findList(map);
    }
}
