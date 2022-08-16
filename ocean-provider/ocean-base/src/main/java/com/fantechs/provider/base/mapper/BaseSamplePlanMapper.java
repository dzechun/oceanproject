package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlan;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSamplePlanMapper extends MyMapper<BaseSamplePlan> {
    List<BaseSamplePlanDto> findList(Map<String, Object> map);
}
