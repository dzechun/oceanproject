package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStationDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentStationMapper extends MyMapper<EamEquipmentStation> {
    List<EamEquipmentStationDto> findList(Map<String,Object> map);
}