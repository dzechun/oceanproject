package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInspectionItemMapper extends MyMapper<BaseInspectionItem> {
    List<BaseInspectionItem> findList(Map<String,Object> map);
    List<BaseInspectionItem> findDetList(Map<String,Object> map);
}