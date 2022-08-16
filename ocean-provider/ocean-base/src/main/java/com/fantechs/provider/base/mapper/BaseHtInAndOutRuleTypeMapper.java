package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRuleType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtInAndOutRuleTypeMapper extends MyMapper<BaseHtInAndOutRuleType> {
    List<BaseHtInAndOutRuleType> findHtList(Map<String, Object> map);
}