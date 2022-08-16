package com.fantechs.mapper;


import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.dto.QaInspectionCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QaInspectionConditionMapper extends MyMapper<QaInspectionCondition> {
    List<QaInspectionCondition> findQaInspectionCondition(Map<String, Object> map);
}
