package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupReDc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentDataGroupReDcMapper extends MyMapper<EamEquipmentDataGroupReDc> {
    List<EamEquipmentDataGroupReDcDto> findList(Map<String,Object> map);
}