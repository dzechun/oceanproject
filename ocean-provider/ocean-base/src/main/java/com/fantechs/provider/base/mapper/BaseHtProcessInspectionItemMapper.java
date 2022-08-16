package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessInspectionItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtProcessInspectionItemMapper extends MyMapper<BaseHtProcessInspectionItem> {
    List<BaseHtProcessInspectionItem> findHtList(Map<String,Object> map);
}