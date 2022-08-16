package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseCalendarWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseCalendarWorkShiftMapper extends MyMapper<BaseCalendarWorkShift> {

    List<BaseCalendarWorkShiftDto> findList(Map<String, Object> map);
}