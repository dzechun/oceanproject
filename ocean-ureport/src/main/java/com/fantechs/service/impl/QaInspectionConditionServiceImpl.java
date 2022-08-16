package com.fantechs.service.impl;

import com.fantechs.common.base.support.BaseService;
import com.fantechs.dto.QaInspectionCondition;
import com.fantechs.mapper.QaInspectionConditionMapper;
import com.fantechs.service.QaInspectionConditionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class QaInspectionConditionServiceImpl  extends BaseService<QaInspectionCondition> implements QaInspectionConditionService {

    @Resource
    private QaInspectionConditionMapper qaInspectionConditionMapper;

    @Override
    public List<QaInspectionCondition> findQaInspectionCondition(Map<String, Object> map) {
        return qaInspectionConditionMapper.findQaInspectionCondition(map);
    }
}
