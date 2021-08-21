package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProject;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquPointInspectionProjectMapper extends MyMapper<EamEquPointInspectionProject> {

    List<EamEquPointInspectionProjectDto> findList(Map<String, Object> map);
}