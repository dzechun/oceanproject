package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRuleDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtInAndOutRuleDetMapper extends MyMapper<BaseHtInAndOutRuleDet> {
    List<BaseHtInAndOutRuleDet> findHtList(Map<String, Object> map);
}