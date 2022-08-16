package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWorkShiftTimeMapper extends MyMapper<BaseWorkShiftTime> {
    List<BaseWorkShiftTime> findList(Map<String, Object> map);
}
