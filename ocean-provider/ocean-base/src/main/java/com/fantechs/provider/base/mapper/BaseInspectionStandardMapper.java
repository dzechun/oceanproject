package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInspectionStandardMapper extends MyMapper<BaseInspectionStandard> {
    List<BaseInspectionStandard> findList(Map<String,Object> map);
}