package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroupReDc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentDataGroupReDcMapper extends MyMapper<DaqHtEquipmentDataGroupReDc> {
    List<DaqHtEquipmentDataGroupReDcDto> findList(Map<String,Object> map);
}