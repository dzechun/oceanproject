package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtBadnessCauseMapper extends MyMapper<BaseHtBadnessCause> {
    List<BaseHtBadnessCause> findHtList(Map<String, Object> map);
}