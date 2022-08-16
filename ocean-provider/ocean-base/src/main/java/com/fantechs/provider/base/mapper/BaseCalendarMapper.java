package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseCalendarDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseCalendarMapper extends MyMapper<BaseCalendar> {

    List<BaseCalendarDto> findList(Map<String, Object> map);
}