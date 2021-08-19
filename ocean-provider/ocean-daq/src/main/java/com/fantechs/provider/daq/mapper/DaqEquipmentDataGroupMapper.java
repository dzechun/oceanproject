package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroup;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentDataGroupMapper extends MyMapper<DaqEquipmentDataGroup> {
    List<DaqEquipmentDataGroupDto> findList(Map<String,Object> map);
}