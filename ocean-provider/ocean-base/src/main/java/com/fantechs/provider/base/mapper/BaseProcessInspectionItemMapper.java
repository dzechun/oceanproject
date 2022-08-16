package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProcessInspectionItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProcessInspectionItemMapper extends MyMapper<BaseProcessInspectionItem> {
    List<BaseProcessInspectionItem> findList(Map<String,Object> map);
}