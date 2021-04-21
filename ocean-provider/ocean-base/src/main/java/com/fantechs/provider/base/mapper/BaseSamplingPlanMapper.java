package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlan;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSamplingPlanMapper extends MyMapper<BaseSamplingPlan> {
    List<BaseSamplingPlanDto> findList(Map<String, Object> map);
}
