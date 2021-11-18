package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialListDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentMaterialListMapper extends MyMapper<EamEquipmentMaterialList> {
    List<EamEquipmentMaterialListDto> findList(Map<String,Object> map);
    List<EamEquipmentMaterialListDto> findExportList(Map<String,Object> map);

}