package com.fantechs.provider.tem.mapper;

import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TemVehicleMapper extends MyMapper<TemVehicle> {
    List<TemVehicleDto> findList(Map<String, Object> map);
}