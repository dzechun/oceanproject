package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupReDc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentDataGroupReDcMapper extends MyMapper<DaqEquipmentDataGroupReDc> {
    List<DaqEquipmentDataGroupReDcDto> findList(Map<String,Object> map);
}