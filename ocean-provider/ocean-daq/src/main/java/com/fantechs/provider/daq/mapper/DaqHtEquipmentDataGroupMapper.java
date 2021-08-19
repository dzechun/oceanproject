package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroup;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentDataGroupMapper extends MyMapper<DaqHtEquipmentDataGroup> {
    List<DaqHtEquipmentDataGroupDto> findList(Map<String,Object> map);
}