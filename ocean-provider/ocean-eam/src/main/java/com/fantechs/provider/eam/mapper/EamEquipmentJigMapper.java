package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentJigDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentJigMapper extends MyMapper<EamEquipmentJig> {
    List<EamEquipmentJigDto> findList(Map<String,Object> map);
}