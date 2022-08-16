package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProcessInspectionItemItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProcessInspectionItemItemMapper extends MyMapper<BaseProcessInspectionItemItem> {
    List<BaseProcessInspectionItemItem> findList(Map<String,Object> map);
}