package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentDataGroupMapper extends MyMapper<EamEquipmentDataGroup> {
    List<EamEquipmentDataGroupDto> findList(Map<String,Object> map);
}