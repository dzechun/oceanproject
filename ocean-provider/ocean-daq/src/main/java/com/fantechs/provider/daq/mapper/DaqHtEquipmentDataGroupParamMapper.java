package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroupParam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentDataGroupParamMapper extends MyMapper<EamHtEquipmentDataGroupParam> {
    List<EamHtEquipmentDataGroupParamDto> findList(Map<String,Object> map);
}