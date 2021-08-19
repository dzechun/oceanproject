package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentReEs;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentReEsMapper extends MyMapper<EamHtEquipmentReEs> {
    List<EamHtEquipmentReEsDto> findList(Map<String,Object> map);
}