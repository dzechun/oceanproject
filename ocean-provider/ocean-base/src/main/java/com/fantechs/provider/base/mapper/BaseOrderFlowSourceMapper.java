package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSource;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrderFlowSourceMapper extends MyMapper<BaseOrderFlowSource> {
    List<BaseOrderFlowSource> findList(Map<String, Object> map);
}