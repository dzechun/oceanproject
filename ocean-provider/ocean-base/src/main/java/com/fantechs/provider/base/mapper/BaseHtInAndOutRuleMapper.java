package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseHtInAndOutRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtInAndOutRuleMapper extends MyMapper<BaseHtInAndOutRule> {
    List<BaseHtInAndOutRuleDto> findList(Map<String,Object> map);
}