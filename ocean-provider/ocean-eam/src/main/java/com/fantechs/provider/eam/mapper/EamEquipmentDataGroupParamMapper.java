package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupParam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentDataGroupParamMapper extends MyMapper<EamEquipmentDataGroupParam> {
    List<EamEquipmentDataGroupParamDto> findList(Map<String,Object> map);
}