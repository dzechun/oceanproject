package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroupParam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentDataGroupParamMapper extends MyMapper<DaqHtEquipmentDataGroupParam> {
    List<DaqHtEquipmentDataGroupParamDto> findList(Map<String,Object> map);
}