package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentStationDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentStation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentStationMapper extends MyMapper<DaqEquipmentStation> {
    List<DaqEquipmentStationDto> findList(Map<String,Object> map);
}