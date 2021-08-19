package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroupReDc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentDataGroupReDcMapper extends MyMapper<EamHtEquipmentDataGroupReDc> {
    List<EamHtEquipmentDataGroupReDcDto> findList(Map<String,Object> map);
}