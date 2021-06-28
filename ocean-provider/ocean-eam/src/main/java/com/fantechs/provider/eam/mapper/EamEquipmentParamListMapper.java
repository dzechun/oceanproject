package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentParamListDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParamList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentParamListMapper extends MyMapper<EamEquipmentParamList> {
    List<EamEquipmentParamListDto> findList(Map<String,Object> map);
}