package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInAndOutRuleDetMapper extends MyMapper<BaseInAndOutRuleDet> {
    List<BaseInAndOutRuleDetDto> findList(Map<String,Object> map);
}