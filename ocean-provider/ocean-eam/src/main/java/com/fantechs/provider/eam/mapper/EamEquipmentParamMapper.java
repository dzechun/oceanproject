package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentParamMapper extends MyMapper<EamEquipmentParam> {
    List<EamEquipmentParamDto> findList(Map<String,Object> map);
}