package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleType;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInAndOutRuleTypeMapper extends MyMapper<BaseInAndOutRuleType> {
    List<BaseInAndOutRuleType> findList(Map<String, Object> map);
}