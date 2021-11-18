package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentJigListDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJigList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentJigListMapper extends MyMapper<EamEquipmentJigList> {
    List<EamEquipmentJigListDto> findList(Map<String,Object> map);

    List<EamEquipmentJigListDto> findExportList(Map<String,Object> map);
}