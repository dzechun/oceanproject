package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRuleDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSampleTransitionRuleDetMapper extends MyMapper<BaseSampleTransitionRuleDet> {
    List<BaseSampleTransitionRuleDetDto> findList(Map<String, Object> map);
}
