package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamMaintainProject;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamMaintainProjectMapper extends MyMapper<EamMaintainProject> {
    List<EamMaintainProjectDto> findList(Map<String,Object> map);
}