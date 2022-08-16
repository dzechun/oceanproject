package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtInspectionStandardMapper extends MyMapper<BaseHtInspectionStandard> {
    List<BaseHtInspectionStandard> findHtList(Map<String,Object> map);
}