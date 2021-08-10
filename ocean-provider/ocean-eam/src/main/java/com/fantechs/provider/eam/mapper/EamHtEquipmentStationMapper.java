package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentStationDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentStationMapper extends MyMapper<EamHtEquipmentStation> {
    List<EamHtEquipmentStationDto> findList(Map<String,Object> map);
}