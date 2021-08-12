package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProject;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMaintainProjectMapper extends MyMapper<EamJigMaintainProject> {
    List<EamJigMaintainProjectDto> findList(Map<String,Object> map);
}