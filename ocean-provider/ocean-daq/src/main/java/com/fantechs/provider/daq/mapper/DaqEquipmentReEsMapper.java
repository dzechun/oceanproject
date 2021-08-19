package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentReEsDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentReEs;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentReEsMapper extends MyMapper<EamEquipmentReEs> {
    List<EamEquipmentReEsDto> findList(Map<String,Object> map);
}