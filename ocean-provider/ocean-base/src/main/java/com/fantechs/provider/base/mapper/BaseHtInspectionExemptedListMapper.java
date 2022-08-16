package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRuleType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtInspectionExemptedListMapper extends MyMapper<BaseHtInspectionExemptedList> {
    List<BaseHtInspectionExemptedList> findHtList(Map<String, Object> map);
}