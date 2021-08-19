package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupParam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentDataGroupParamMapper extends MyMapper<DaqEquipmentDataGroupParam> {
    List<DaqEquipmentDataGroupParamDto> findList(Map<String,Object> map);
}