package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentReEs;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentReEsMapper extends MyMapper<DaqHtEquipmentReEs> {
    List<DaqHtEquipmentReEsDto> findList(Map<String,Object> map);
}