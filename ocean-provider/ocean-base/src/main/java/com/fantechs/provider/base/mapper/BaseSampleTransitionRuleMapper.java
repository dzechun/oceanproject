package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSampleTransitionRuleMapper extends MyMapper<BaseSampleTransitionRule> {
    List<BaseSampleTransitionRuleDto> findList(Map<String, Object> map);
}
