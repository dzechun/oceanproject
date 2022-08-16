package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleTransitionRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtSampleTransitionRuleMapper extends MyMapper<BaseHtSampleTransitionRule> {
    List<BaseHtSampleTransitionRule> findList(Map<String, Object> map);
}
