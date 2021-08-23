package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProject;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentMaintainProjectMapper extends MyMapper<EamEquipmentMaintainProject> {
    List<EamEquipmentMaintainProjectDto> findList(Map<String, Object> map);
}