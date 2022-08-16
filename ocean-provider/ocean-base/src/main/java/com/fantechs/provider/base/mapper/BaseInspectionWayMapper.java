package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInspectionWayMapper extends MyMapper<BaseInspectionWay> {
    List<BaseInspectionWay> findList(Map<String,Object> map);
}