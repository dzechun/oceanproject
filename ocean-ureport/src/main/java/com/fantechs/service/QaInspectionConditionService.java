package com.fantechs.service;


import com.fantechs.common.base.support.IService;
import com.fantechs.dto.QaInspectionCondition;

import java.util.List;
import java.util.Map;

public interface QaInspectionConditionService extends IService<QaInspectionCondition> {

    List<QaInspectionCondition> findQaInspectionCondition(Map<String, Object> map);

}
