package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductionKeyIssues;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtProductionKeyIssuesMapper extends MyMapper<BaseHtProductionKeyIssues> {
    List<BaseHtProductionKeyIssues> findHtList(Map<String, Object> map);
}