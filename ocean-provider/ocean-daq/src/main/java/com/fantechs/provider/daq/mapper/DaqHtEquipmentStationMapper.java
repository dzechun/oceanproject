package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentStationDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentStation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentStationMapper extends MyMapper<DaqHtEquipmentStation> {
    List<DaqHtEquipmentStationDto> findList(Map<String,Object> map);
}