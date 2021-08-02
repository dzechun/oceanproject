package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroup;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentDataGroupMapper extends MyMapper<EamHtEquipmentDataGroup> {
    List<EamHtEquipmentDataGroupDto> findList(Map<String,Object> map);
}