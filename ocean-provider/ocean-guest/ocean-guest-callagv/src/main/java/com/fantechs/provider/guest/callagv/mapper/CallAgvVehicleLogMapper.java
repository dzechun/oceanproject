package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvVehicleLogMapper extends MyMapper<CallAgvVehicleLog> {
    List<CallAgvVehicleLogDto> findList(Map<String, Object> map);
}