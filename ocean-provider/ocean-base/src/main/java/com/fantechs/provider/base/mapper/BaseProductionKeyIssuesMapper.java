package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProductionKeyIssues;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProductionKeyIssuesMapper extends MyMapper<BaseProductionKeyIssues> {
    List<BaseProductionKeyIssues> findList(Map<String,Object> map);
}