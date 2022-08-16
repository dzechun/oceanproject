package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWorkShiftMapper extends MyMapper<BaseWorkShift> {

    List<BaseWorkShiftDto> findList(Map<String, Object> map);
}