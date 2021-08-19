package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentReEsDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentReEs;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentReEsMapper extends MyMapper<DaqEquipmentReEs> {
    List<DaqEquipmentReEsDto> findList(Map<String,Object> map);
}