package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplePlan;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtSamplePlanMapper extends MyMapper<BaseHtSamplePlan> {
    List<BaseHtSamplePlan> findList(Map<String, Object> map);
}
