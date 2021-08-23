package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainProjectItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentMaintainProjectItemMapper extends MyMapper<EamHtEquipmentMaintainProjectItem> {
    List<EamEquipmentMaintainProjectItemDto> findList(Map<String, Object> map);
}